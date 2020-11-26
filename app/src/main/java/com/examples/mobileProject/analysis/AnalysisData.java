package com.examples.mobileProject.analysis;

import android.graphics.drawable.Drawable;

public class AnalysisData {
    private String title ;
    private Drawable iconDrawable;
    private Drawable color;
    private int startDate;

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public AnalysisData(String title, Drawable iconDrawable, Drawable color, int startDate) {
        this.title = title;
        this.iconDrawable = iconDrawable;
        this.color = color;
        this.startDate = startDate;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public Drawable getColor() {
        return color;
    }

    public void setColor(Drawable color) {
        this.color = color;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AnalysisData(String title) {
        this.title = title;
    }
}
