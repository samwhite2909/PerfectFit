package com.example.fitnesscoach;

public class FoodSuggestion {
    private String foodName;
    private double calPerPortion;
    private double portionSize;

    public FoodSuggestion(String foodName, double calPerPortion, double portionSize) {
        this.foodName = foodName;
        this.calPerPortion = calPerPortion;
        this.portionSize = portionSize;
    }

    public FoodSuggestion() {
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCalPerPortion() {
        return calPerPortion;
    }

    public double getPortionSize() {
        return portionSize;
    }
}
