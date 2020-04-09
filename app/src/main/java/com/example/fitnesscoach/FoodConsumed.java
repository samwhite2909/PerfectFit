package com.example.fitnesscoach;

public class FoodConsumed {
    private String foodName;
    private double caloriesConsumed;
    private double amountConsumed;

    public FoodConsumed() {
    }

    public FoodConsumed(String foodName, double caloriesConsumed, double amountConsumed) {
        this.foodName = foodName;
        this.caloriesConsumed = caloriesConsumed;
        this.amountConsumed = amountConsumed;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public double getAmountConsumed() {
        return amountConsumed;
    }
}
