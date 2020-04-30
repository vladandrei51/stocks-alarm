package com.stocks.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Component
public class Stock {

    @NotBlank
    private final String symbol; //unique

    private final String name;

    private final String type;

    private final String region;

    private final String marketOpen;

    private final String marketClose;

    private final String timezone;

    private final String currency;

    private final float matchScore;

    public Stock(String symbol) {
        this(symbol, "", "", "", "", "", "", "", 0f);

    }

    public Stock(@JsonProperty("symbol") String symbol,
                 @JsonProperty("name") String name,
                 @JsonProperty("type") String type,
                 @JsonProperty("region") String region,
                 @JsonProperty("marketOpen") String marketOpen,
                 @JsonProperty("marketClose") String marketClose,
                 @JsonProperty("timezone") String timezone,
                 @JsonProperty("currency") String currency,
                 @JsonProperty("matchScore") float matchScore) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return symbol.equals(stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
