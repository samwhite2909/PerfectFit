package com.example.fitnesscoach;

public class LeaderboardItem {
    private String name;
    private int score;

    public LeaderboardItem() {
    }

    public LeaderboardItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
