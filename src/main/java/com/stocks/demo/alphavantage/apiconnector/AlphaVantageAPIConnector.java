package com.stocks.demo.alphavantage.apiconnector;

import com.stocks.demo.exception.ApiRequestException;
import com.stocks.demo.model.Stock;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class AlphaVantageAPIConnector {
    private final String BASE_URL = "https://www.alphavantage.co";
    private final String API_KEY = "KP6W9JRK1RJ0ETUG";

    public AlphaVantageAPIConnector() throws ApiRequestException {
    }

    private JSONObject getJSONFromURL(String url) {
        try {
            return new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return null;
        }

    }

    public List<Stock> getStockListFromSearch(String searchKeyword) {
        final String JSON_ARRAY_KEY = "bestMatches";
        final String FUNCTION = "SYMBOL_SEARCH";
        String url = String.format(BASE_URL + "/query?function=%s&keywords=%s&apikey=%s", FUNCTION, searchKeyword, API_KEY);

        JSONObject jsonObject = getJSONFromURL(url);

        if (jsonObject != null) {
            List<Stock> stockList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ARRAY_KEY);
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject stockJSONObject = new JSONObject(jsonArray.getJSONObject(index).toString().replaceAll("[1-9]\\. ", ""));
                Stock stock = new Stock(
                        stockJSONObject.get("symbol").toString(),
                        stockJSONObject.get("name").toString(),
                        stockJSONObject.get("type").toString(),
                        stockJSONObject.get("region").toString(),
                        stockJSONObject.get("marketOpen").toString(),
                        stockJSONObject.get("marketClose").toString(),
                        stockJSONObject.get("timezone").toString(),
                        stockJSONObject.get("currency").toString(),
                        stockJSONObject.getFloat("matchScore")
                );
                stockList.add(stock);
            }
            return stockList;
        }
        return null;
    }

    public float getStockPriceIntraDay(String stockSymbol, int timeout) {
        final String KEY_LIKE = "Time";
        final String FUNCTION = "TIME_SERIES_INTRADAY";
        String url = String.format(BASE_URL + "/query?function=%s&symbol=%s&interval=%s&apikey=%s", FUNCTION, stockSymbol, timeout + "min", API_KEY);

        JSONObject jsonObject = getJSONFromURL(url);

        if (jsonObject != null && jsonObject.keySet().stream().anyMatch(key -> key.contains(KEY_LIKE))) {
            String timeKey = jsonObject.keySet().stream().filter(key -> key.contains(KEY_LIKE)).findFirst().get();
            jsonObject = jsonObject.getJSONObject(timeKey);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String latestDate = jsonObject.keySet().stream().max(Comparator.comparing(s -> LocalDateTime.parse(s, formatter))).get();
            String openPrice = new JSONObject(jsonObject.getJSONObject(latestDate).toString().replaceAll("[1-5]\\. ", "")).get("open").toString();
            return Float.parseFloat(openPrice);
        }
        return 0f; //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
    }
}

