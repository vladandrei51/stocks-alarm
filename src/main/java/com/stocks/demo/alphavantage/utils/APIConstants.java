package com.stocks.demo.alphavantage.utils;

public class APIConstants {
    public static final String META_DATA = "Meta Data";
    public static final String LAST_REFRESHED = "3. Last Refreshed";
    public static final String TIME_SERIES_5_MIN = "Time Series (5min)";
    public static final String OPEN_PRICE = "1. open";
    public static final String BASE_URL = "https://www.alphavantage.co";
    public static final String API_KEY = "KP6W9JRK1RJ0ETUG";
    public static final String BASIC_FUNCTION_WITH_KEYWORDS = "/query?function=%s&keywords=%s&apikey=%s";
    public static final String BASIC_FUNCTION_WITH_SYMBOL = "/query?function=%s&symbol=%s&apikey=%s";
    public static final String GLOBAL_QUOTE_FUNCTION = "GLOBAL_QUOTE";
    public static final String GLOBAL_QUOTE_KEY = "Global Quote";
    public static final String GLOBAL_QUOTE_PRICE = "05. price";

    public static final String SYMBOL_SEARCH = "SYMBOL_SEARCH";
    public static final String STOCK_KEYWORD_SEARCH = "bestMatches";

    public static final String LIMIT_REACHED_NODE = "Note";
//    public static final String LIMIT_REACHED_VALUE = "Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day";
}
