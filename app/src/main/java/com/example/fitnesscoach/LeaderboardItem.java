package com.example.fitnesscoach;

//Model class used by FirestoreUI to show users scores on the leaderboard.
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
