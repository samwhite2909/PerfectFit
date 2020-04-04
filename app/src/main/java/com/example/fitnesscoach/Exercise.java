package com.example.fitnesscoach;

public class Exercise {

    private String exerciseName;
    private double calPerMin;

    public Exercise() {
    }

    public Exercise(String exerciseName, double calPerMin) {
        this.exerciseName = exerciseName;
        this.calPerMin = calPerMin;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public double getCalPerMin() {
        return calPerMin;
    }

    public void setCalPerMin(double calPerMin) {
        this.calPerMin = calPerMin;
    }
}
