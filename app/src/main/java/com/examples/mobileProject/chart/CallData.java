package com.examples.mobileProject.chart;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class CallData {
    private String title ;
    private Drawable img ;

    public CallData(String title, Drawable img) {
        this.title = title;
        this.img = img;
    }
    public String getTitle(){
        return title;
    }
    public Drawable getImg() {
        return img;
    }
}
