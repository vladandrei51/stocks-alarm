package com.amigoscode.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class Alarm {
    private final UUID alarmId;

    @NotNull
    private final UUID userId;

    @NotBlank
    private final String stockSymbol; //stock identifier

    @NotNull
    private final int targetAlarmPercentage;

    private final int currentAlarmVariance;

    private final float initialStockPrice;

    private final float currentStockPrice;

    private final boolean isActive;

    public Alarm(@JsonProperty("alarmId") UUID alarmId,
                 @JsonProperty("userId") UUID userId,
                 @JsonProperty("stockSymbol") String stockSymbol,
                 @JsonProperty("targetAlarmPercentage") int targetAlarmPercentage,
                 @JsonProperty("currentAlarmVariance") int currentAlarmVariance,
                 @JsonProperty("initialStockPrice") float initialStockPrice,
                 @JsonProperty("currentStockPrice") float currentStockPrice,
                 @JsonProperty("isActive") boolean isActive) {
        this.alarmId = alarmId;
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.targetAlarmPercentage = targetAlarmPercentage;
        this.currentAlarmVariance = currentAlarmVariance;
        this.initialStockPrice = initialStockPrice;
        this.currentStockPrice = currentStockPrice;
        this.isActive = isActive;
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

    public float getInitialStockPrice() {
        return initialStockPrice;
    }

    public float getCurrentStockPrice() {
        return currentStockPrice;
    }

    public boolean isActive() {
        return isActive;
    }
}
