package com.examples.mobileProject.analysis;

import android.graphics.drawable.Drawable;

public class AnalysisData {
    private String title ;
    private Drawable iconDrawable;
    private Drawable color;

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

    public AnalysisData(String title, Drawable iconDrawable, Drawable color) {
        this.title = title;
        this.iconDrawable = iconDrawable;
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
