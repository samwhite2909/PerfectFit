package com.example.fitnesscoach;

//Model class required by FirestoreUI to create meal planning items.
public class MealPlan {

    private String mealTitle;
    private String mealDesc;
    private int priority;

    public MealPlan (){
    }

    public MealPlan(String mealTitle, String mealDesc, int priority){
        this.mealTitle = mealTitle;
        this.mealDesc = mealDesc;
        this.priority = priority;
    }

    public String getMealTitle() {
        return mealTitle;
    }

    public String getMealDesc() {
        return mealDesc;
    }

    public int getPriority() {
        return priority;
    }
}
