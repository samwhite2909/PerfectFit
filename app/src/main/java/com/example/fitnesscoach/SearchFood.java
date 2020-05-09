package com.example.fitnesscoach;

//Model class required by FirestoreUI, creates the object to represent a food in the search list.
public class SearchFood {
    private String foodName;
    private double portionSize;
    private double calPerPortion;

    public SearchFood() {
    }

    public SearchFood(String foodName, double portionSize, double calPerPortion) {
        this.foodName = foodName;
        this.portionSize = portionSize;
        this.calPerPortion = calPerPortion;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPortionSize() {
        return portionSize;
    }

    public double getCalPerPortion() {
        return calPerPortion;
    }
}
