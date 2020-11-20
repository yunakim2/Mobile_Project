package com.examples.mobileProject.analysis;

import java.io.Serializable;

public class AnalysisDayData implements Serializable {
    int date;
    float pos;
    float neg;


    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public float getNeg() {
        return neg;
    }

    public void setNeg(float neg) {
        this.neg = neg;
    }

    public AnalysisDayData(int date, float pos, float neg)  {
        this.date = date;
        this.pos = pos;
        this.neg = neg;
    }
}
