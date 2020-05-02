package com.stocks.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component
public class Alarm {
    private UUID alarmId;

    @NotNull
    private UUID userId;

    @NotBlank
    private String stockSymbol; //stock identifier

    @NotNull
    private int targetAlarmPercentage;

    private int currentAlarmVariance;

    private double initialStockPrice;

    private double currentStockPrice;

    private boolean active;

    public Alarm(@JsonProperty("alarmId") UUID alarmId,
                 @JsonProperty("userId") UUID userId,
                 @JsonProperty("stockSymbol") String stockSymbol,
                 @JsonProperty("targetAlarmPercentage") int targetAlarmPercentage,
                 @JsonProperty("currentAlarmVariance") int currentAlarmVariance,
                 @JsonProperty("initialStockPrice") float initialStockPrice,
                 @JsonProperty("currentStockPrice") float currentStockPrice,
                 @JsonProperty("isActive") boolean active) {
        this.alarmId = alarmId;
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.targetAlarmPercentage = targetAlarmPercentage;
        this.currentAlarmVariance = currentAlarmVariance;
        this.initialStockPrice = initialStockPrice;
        this.currentStockPrice = currentStockPrice;
        this.active = active;
    }

    public Alarm() {
    }

    public UUID getAlarmId() {
        return alarmId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getTargetAlarmPercentage() {
        return targetAlarmPercentage;
    }

    public int getCurrentAlarmVariance() {
        return currentAlarmVariance;
    }

    public double getInitialStockPrice() {
        return initialStockPrice;
    }

    public double getCurrentStockPrice() {
        return currentStockPrice;
    }

    public boolean isActive() {
        return active;
    }
}
