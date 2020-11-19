package com.examples.mobileProject.analysis;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class AnalysisTotalData {
    ArrayList<AnalysisDayData> analysisWeekData ;
    int week;

    public AnalysisTotalData(ArrayList<AnalysisDayData> analysisWeekData, int week) {
        this.analysisWeekData = analysisWeekData;
        this.week = week;
    }

    public ArrayList<AnalysisDayData> getAnalysisWeekData() {
        return analysisWeekData;
    }

    public void setAnalysisWeekData(ArrayList<AnalysisDayData> analysisWeekData) {
        this.analysisWeekData = analysisWeekData;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}
