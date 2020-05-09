package com.example.fitnesscoach;

//Model class for the adding in of workout plan items, required by FirestoreUI.
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

    public String getExerciseTarget() {
        return exerciseTarget;
    }

    public int getExercisePriority() {
        return exercisePriority;
    }

}
