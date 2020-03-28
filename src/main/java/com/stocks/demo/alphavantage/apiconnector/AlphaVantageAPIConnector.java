package com.stocks.demo.alphavantage.apiconnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.demo.alphavantage.utils.APIConstants;
import com.stocks.demo.exception.ApiRequestException;
import com.stocks.demo.model.Stock;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.stocks.demo.alphavantage.utils.APIConstants.*;


public class AlphaVantageAPIConnector {


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
        String url = String.format(BASE_URL + STOCK_LIST_FROM_SEARCH_SUBURL, SYMBOL_SEARCH, searchKeyword, API_KEY);

        JSONObject jsonObject = getJSONFromURL(url);


        ObjectMapper objectMapper = new ObjectMapper();

        if (jsonObject != null) {
            try {
                List<Stock> stockList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray(STOCK_KEYWORD_SEARCH);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject stockJSONObject = new JSONObject(jsonArray.getJSONObject(index).toString().replaceAll("[1-9]\\. ", ""));
                    Stock stock = objectMapper.readValue(stockJSONObject.toString(), Stock.class);
                    stockList.add(stock);
                }
                return stockList;
            } catch (IOException io) {
                return null;
            }
        }
        return null; //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data

    }

    public double getStockPriceIntraDay(String stockSymbol) {

        String url = String.format(APIConstants.BASE_URL + APIConstants.STOCK_PRICE_INTRA_DAY_SUBURL, APIConstants.TIME_SERIES_INTRADAY, stockSymbol, APIConstants.API_KEY);

        JSONObject jsonObject = getJSONFromURL(url);

        ObjectMapper objectMapper = new ObjectMapper();

        if (jsonObject != null) {
            try {
                JsonNode jsonNodeRoot = objectMapper.readTree(jsonObject.toString());
                String lastRefreshed = jsonNodeRoot.get(META_DATA).get(LAST_REFRESHED).asText();
                JsonNode price = jsonNodeRoot.get(TIME_SERIES_5_MIN).get(lastRefreshed);
                return price.get(OPEN_PRICE).asDouble();
            } catch (IOException e) {
                return 0d;
            }
        }
        return 0d; //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
    }
}

