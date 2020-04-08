package com.example.fitnesscoach;

public class CompletedExercise {
    private String exerciseName;
    private double caloriesBurned;
    private double duration;

    public CompletedExercise(String exerciseName, double caloriesBurned, double duration) {
        this.exerciseName = exerciseName;
        this.caloriesBurned = caloriesBurned;
        this.duration = duration;
    }

    public CompletedExercise() {
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public double getDuration() {
        return duration;
    }
}
