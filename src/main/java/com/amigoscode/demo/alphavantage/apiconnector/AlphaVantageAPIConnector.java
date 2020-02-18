package com.amigoscode.demo.alphavantage.apiconnector;

import com.amigoscode.demo.exception.ApiRequestException;
import com.amigoscode.demo.model.Stock;
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

    public List<Stock> getStockListFromSearch(String searchKeyword) {
        final String JSON_ARRAY_KEY = "bestMatches";
        String url = String.format(BASE_URL + "/query?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", searchKeyword, API_KEY);
        JSONObject jsonObject = null;
        boolean connectedSuccessfully = false;
        try {
            jsonObject = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            connectedSuccessfully = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connectedSuccessfully) {
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

        JSONObject jsonObject = null;
        boolean connectedSuccessfully = false;

        try {
            jsonObject = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            connectedSuccessfully = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connectedSuccessfully) {
            String timeKey = jsonObject.keySet().stream().filter(key -> key.contains(KEY_LIKE)).findFirst().orElseGet(null);
            jsonObject = jsonObject.getJSONObject(timeKey);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String latestDate = jsonObject.keySet().stream().max(Comparator.comparing(s -> LocalDateTime.parse(s, formatter))).get();
            return Float.parseFloat(new JSONObject(jsonObject.getJSONObject(latestDate).toString().replaceAll("[1-5]\\. ", "")).get("open").toString());
        }
        return 0f;
    }
}

