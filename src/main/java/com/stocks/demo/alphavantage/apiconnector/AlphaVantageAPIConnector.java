package com.stocks.demo.alphavantage.apiconnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.demo.DemoApplication;
import com.stocks.demo.alphavantage.utils.APIConstants;
import com.stocks.demo.exception.ApiRequestException;
import com.stocks.demo.model.Stock;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.stocks.demo.alphavantage.utils.APIConstants.*;


public class AlphaVantageAPIConnector {

    private final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public AlphaVantageAPIConnector() throws ApiRequestException {
    }

    private boolean isJsonValid(JSONObject jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return !objectMapper.readTree(jsonObject.toString()).has(LIMIT_REACHED_NODE);
        } catch (IOException e) {
            return false;
        }
    }

    private JSONObject getJSONFromURL(String url) throws IOException {
        JSONObject readJson = new JSONObject(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));

        if (readJson != null && isJsonValid(readJson))
            return readJson;
        else {
            throw new IOException();
        }

    }

    @Bean
    public Stock getStockInfoFromKeyword(String keyword) {
        String url = String.format(BASE_URL + BASIC_FUNCTION_WITH_KEYWORDS, SYMBOL_SEARCH, keyword, API_KEY);
        ObjectMapper objectMapper = new ObjectMapper();
        Stock stock;
        boolean found = false;

        try {
            JSONObject jsonObject = getJSONFromURL(url);
            JSONArray jsonArray = jsonObject.getJSONArray(STOCK_KEYWORD_SEARCH);
            int index = 0;
            while (!found || index > jsonArray.length()) {
                JSONObject stockJSONObject = new JSONObject(jsonArray.getJSONObject(index).toString().replaceAll("[1-9]\\. ", ""));
                stock = objectMapper.readValue(stockJSONObject.toString(), Stock.class);
                if (stock.getSymbol().equalsIgnoreCase(keyword)) {
                    logger.info("getStockInfoFromKeyword(" + keyword + ") found stock!");
                    found = true;
                    return stock;

                }
                index++;
            }


        } catch (Exception ignore) {
        }

        logger.error("getStockInfoFromKeyword(" + keyword + ") was unable to find any stocks with the given parameter!");
        return new Stock(keyword);

    }

    public List<Stock> getStockListFromSearch(String searchKeyword) {
        String url = String.format(BASE_URL + BASIC_FUNCTION_WITH_KEYWORDS, SYMBOL_SEARCH, searchKeyword, API_KEY);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Stock> stockList = new ArrayList<>();
        try {
            JSONObject jsonObject = getJSONFromURL(url);
            JSONArray jsonArray = jsonObject.getJSONArray(STOCK_KEYWORD_SEARCH);
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject stockJSONObject = new JSONObject(jsonArray.getJSONObject(index).toString().replaceAll("[1-9]\\. ", ""));
                Stock stock = objectMapper.readValue(stockJSONObject.toString(), Stock.class);
                stockList.add(stock);
            }
            return stockList;
        } catch (Exception ignored) {
            return null;
        }
    }

    public double getStockPrice(String stockSymbol) {

        String url = String.format(APIConstants.BASE_URL + BASIC_FUNCTION_WITH_SYMBOL, GLOBAL_QUOTE_FUNCTION, stockSymbol, APIConstants.API_KEY);

        ObjectMapper objectMapper = new ObjectMapper();

        double stockPrice;

        try {
            JSONObject jsonObject = getJSONFromURL(url);
            JsonNode jsonNodeRoot = objectMapper.readTree(jsonObject.toString());
            stockPrice = jsonNodeRoot.get(GLOBAL_QUOTE_KEY).get(GLOBAL_QUOTE_PRICE).asDouble();
            logger.info("getStockPrice(" + stockSymbol + ")  = " + stockPrice);
            return stockPrice;
        } catch (Exception ignored) {
            logger.error("API not called successfully for getStockPrice(" + stockSymbol + ")");
            return 0d;
        }
    }
}

