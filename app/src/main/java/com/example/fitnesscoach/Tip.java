package com.example.fitnesscoach;

public class Tip {
    private String tipTitle;
    private String tipTip;
    private String poster;

    public Tip() {
    }

    public Tip(String tipTitle, String tipTip, String poster) {
        this.tipTitle = tipTitle;
        this.tipTip = tipTip;
        this.poster = poster;
    }

    public String getTipTitle() {
        return tipTitle;
    }

    public String getTipTip() {
        return tipTip;
    }

    public String getPoster() {
        return poster;
    }
}
