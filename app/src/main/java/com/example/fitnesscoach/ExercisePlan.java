package com.example.fitnesscoach;

public class ExercisePlan {
    private String exerciseTitle;
    private String exerciseTarget;
    private int exercisePriority;

    public ExercisePlan() {
    }

    public ExercisePlan(String exerciseTitle, String exerciseTarget, int exercisePriority) {
        this.exerciseTitle = exerciseTitle;
        this.exerciseTarget = exerciseTarget;
        this.exercisePriority = exercisePriority;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public String getExerciseTarget() {
        return exerciseTarget;
    }

    public void setExerciseTarget(String exerciseTarget) {
        this.exerciseTarget = exerciseTarget;
    }

    public int getExercisePriority() {
        return exercisePriority;
    }

    public void setExercisePriority(int exercisePriority) {
        this.exercisePriority = exercisePriority;
    }
}
