package com.examples.mobileProject.analysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.examples.mobileProject.R;

import java.util.ArrayList;

public class AnalysisChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);

        Intent intent = getIntent();
        ArrayList<AnalysisDayData> data = (ArrayList<AnalysisDayData>)intent.getSerializableExtra("datas");

        for(int i =0 ; i<data.size() ; i++) {
            System.out.println(data.get(i).date);
            System.out.println(data.get(i).neg);
            System.out.println(data.get(i).pos);
        }
    }
}