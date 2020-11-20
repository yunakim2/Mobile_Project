package com.examples.mobileProject.analysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.examples.mobileProject.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class AnalysisChartActivity extends AppCompatActivity {

    BarChart chart ;
    ArrayList<AnalysisDayData> data = new ArrayList<AnalysisDayData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);

        Intent intent = getIntent();
        data = (ArrayList<AnalysisDayData>) intent.getSerializableExtra("datas");
        chart = findViewById(R.id.chartBar);
        initChart();
    }
    public void initChart() {
        ArrayList  neg = new ArrayList();
        ArrayList  pos = new ArrayList();
        ArrayList  date = new ArrayList();

        for(int i =0 ; i<data.size() ; i++) {
            System.out.println(data.get(i).date);
            System.out.println(data.get(i).neg);
            System.out.println(data.get(i).pos);
            String intDate = Integer.toString(data.get(i).date);
            String inputDate = intDate.substring(0,3)+"0-"+intDate.substring(3,5)+"-"+intDate.substring(5,7);

            neg.add(new BarEntry(data.get(i).neg,i));
            pos.add(new BarEntry(data.get(i).pos, i));
            date.add(inputDate);
        }


        ArrayList dataSet = new ArrayList<>();


        BarDataSet datasetNeg = new BarDataSet(neg,"Negative");
        datasetNeg.setColor(getResources().getColor(R.color.colorBrown));
        datasetNeg.setValueTextColor(getResources().getColor(R.color.colorBlack));
        datasetNeg.setBarShadowColor(getResources().getColor(R.color.colorGrey));
        datasetNeg.setValueTextSize(10f);


        BarDataSet datasetPos = new BarDataSet(pos,"Positive");
        datasetPos.setColor(getResources().getColor(R.color.colorPrimary));
        datasetPos.setValueTextColor(getResources().getColor(R.color.colorBlack));
        datasetPos.setBarShadowColor(getResources().getColor(R.color.colorGrey));
        datasetPos.setValueTextSize(10f);


        dataSet.add(datasetPos);
        dataSet.add(datasetNeg);


        BarData data =new BarData(date,dataSet);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawGridBackground(false);
        chart.setClickable(false);
        chart.setDrawBorders(false);
        chart.setDrawHighlightArrow(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitsSystemWindows(false);

        chart.setData(data);
        chart.invalidate();




    }

}