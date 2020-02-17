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
    private final int timeout;

    public AlphaVantageAPIConnector(int timeout) throws ApiRequestException {
        this.timeout = timeout; //The following values are supported: 1min, 5min, 15min, 30min, 60min
//        if (timeout == 1 || timeout == 5 || timeout == 15 || timeout == 30 || timeout ==60){
//            throw new ApiRequestException("For timeout the following values are supported: 1min, 5min, 15min, 30min, 60min");
//        }
    }

    public List<Stock> getStockListFromSearch(String searchKeyword) throws IOException {
        final String JSON_ARRAY_KEY = "bestMatches";
        String url = String.format(BASE_URL + "/query?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", searchKeyword, API_KEY);
        JSONObject jsonObject = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));

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

    public String getStockPriceIntraDay(String stockSymbol) throws IOException {
        final String KEY_LIKE = "Time";
        String url = String.format(BASE_URL + "/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=%s&apikey=%s", stockSymbol, this.timeout + "min", API_KEY);

        JSONObject jsonObject = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
        String timeKey = jsonObject.keySet().stream().filter(key -> key.contains(KEY_LIKE)).findFirst().orElseGet(null);
        jsonObject = jsonObject.getJSONObject(timeKey);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ArrayList<String> dateKeys = new ArrayList<>(jsonObject.keySet());
        dateKeys.sort(Comparator.comparing(s -> LocalDateTime.parse(s, formatter)));
        return new JSONObject(jsonObject.getJSONObject(dateKeys.get(dateKeys.size() - 1)).toString().replaceAll("[1-5]\\. ", "")).get("open").toString();
    }
}

