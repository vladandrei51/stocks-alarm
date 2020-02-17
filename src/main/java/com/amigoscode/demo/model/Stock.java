package com.amigoscode.demo.model;

import javax.validation.constraints.NotBlank;

public class Stock {

    @NotBlank
    private final String symbol; //unique

    @NotBlank
    private final String name;

    private final String type;

    private final String region;

    private final String marketOpen;

    private final String marketClose;

    private final String timezone;

    private final String currency;

    private final float matchScore;

    public Stock(@NotBlank String symbol,
                 @NotBlank String name,
                 String type,
                 String region,
                 String marketOpen,
                 String marketClose,
                 String timezone,
                 String currency,
                 float matchScore) {
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.region = region;
        this.marketOpen = marketOpen;
        this.marketClose = marketClose;
        this.timezone = timezone;
        this.currency = currency;
        this.matchScore = matchScore;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRegion() {
        return region;
    }

    public String getMarketOpen() {
        return marketOpen;
    }

    public String getMarketClose() {
        return marketClose;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCurrency() {
        return currency;
    }

    public float getMatchScore() {
        return matchScore;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", region='" + region + '\'' +
                ", marketOpen='" + marketOpen + '\'' +
                ", marketClose='" + marketClose + '\'' +
                ", timezone='" + timezone + '\'' +
                ", currency='" + currency + '\'' +
                ", matchScore=" + matchScore +
                '}';
    }
}
