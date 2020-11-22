package com.examples.mobileProject.chart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.examples.mobileProject.analysis.AnalysisDayData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalysisChartActivity extends AppCompatActivity {
    TextView tvChartTitle1, tvChartTitle2, tvEmotionCal, tvCallBtn, tvSolutionBtn;

    LineChart chart ;
    ImageView imgEmotion;
    ArrayList<AnalysisDayData> data = new ArrayList<AnalysisDayData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);


        tvChartTitle1 = findViewById(R.id.tvChartTitle);
        tvChartTitle2 = findViewById(R.id.tvChartWeek);
        tvEmotionCal = findViewById(R.id.tvChartEmotion);
        tvCallBtn = findViewById(R.id.tvChartCall);
        tvSolutionBtn = findViewById(R.id.tvChartSolution);
        imgEmotion = findViewById(R.id.imgEmotion);

        Intent intent = getIntent();
        data = (ArrayList<AnalysisDayData>) intent.getSerializableExtra("datas");
        String title = intent.getStringExtra("title");

        tvChartTitle1.setText(title);
        tvChartTitle2.setText(title);


        chart = findViewById(R.id.chartBar);
//        getCallHistory();
        initChart();

        tvCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};
                Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);
                if(c.getCount()==0) Toast.makeText(getApplicationContext(),"통화기록 없음!", Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(getApplicationContext(), CallActivity.class));
                }
            }
        });

        tvSolutionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SolutionActivity.class));
            }
        });

    }
    public void initChart() {
        ArrayList  neg = new ArrayList();
        ArrayList  pos = new ArrayList();
        ArrayList  date = new ArrayList();
        Float SumNeg =0.0f;
        Float SumPos = 0.0f;


        for(int i =0 ; i<data.size() ; i++) {
            System.out.println(data.get(i).date);
            System.out.println(data.get(i).neg);
            System.out.println(data.get(i).pos);
            String intDate = Integer.toString(data.get(i).date);
            String inputDate = intDate.substring(3,5)+"/"+intDate.substring(5,7);

            neg.add(new BarEntry(data.get(i).neg,i));
            pos.add(new BarEntry(data.get(i).pos, i));
            SumNeg += data.get(i).neg;
            SumPos+=data.get(i).pos;
            date.add(inputDate);
        }

        SumNeg = SumNeg/data.size();
        SumPos = SumPos/data.size();
        if(SumPos>=SumNeg) {
            tvEmotionCal.setText("긍정적");
            imgEmotion.setImageDrawable(getResources().getDrawable(R.drawable.happy_pink));
        } else {
            tvEmotionCal.setText("부정적");
            imgEmotion.setImageDrawable(getResources().getDrawable(R.drawable.sad_pink));
        }


        ArrayList dataSet = new ArrayList<>();


        LineDataSet datasetNeg = new LineDataSet(neg,"Negative");
        datasetNeg.setColor(getResources().getColor(R.color.colorBrown));
        datasetNeg.setValueTextColor(getResources().getColor(R.color.colorBlack));
        datasetNeg.setValueTextSize(10f);
        datasetNeg.setCircleColor(getResources().getColor(R.color.colorBrown));
        datasetNeg.setCircleColorHole(getResources().getColor(R.color.colorBrown));
        datasetNeg.disableDashedLine();


        LineDataSet datasetPos = new LineDataSet(pos,"Positive");
        datasetPos.setColor(getResources().getColor(R.color.colorPrimary));
        datasetPos.setValueTextColor(getResources().getColor(R.color.colorBlack));
        datasetPos.setValueTextSize(10f);
        datasetPos.setCircleColor(getResources().getColor(R.color.colorPrimary));
        datasetPos.setCircleColorHole(getResources().getColor(R.color.colorPrimary));


        dataSet.add(datasetPos);
        dataSet.add(datasetNeg);


        LineData data =new LineData(date,dataSet);
        data.setValueTextColor(R.color.colorBlack);
        data.setValueTextSize(10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setGridColor(getResources().getColor(R.color.colorGrey));
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorGrey));


        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.mAxisMaximum= 1.0f;
        yAxisLeft.mAxisMinimum=0.0f;
        yAxisLeft.setGridColor(getResources().getColor(R.color.colorWhite));


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.mAxisMinimum = 0.0f;
        yAxisRight.mAxisMaximum =1.0f;
        yAxisRight.setGridColor(getResources().getColor(R.color.colorWhite));


        chart.setDrawGridBackground(true);
        chart.setClickable(false);
        chart.setDrawBorders(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitsSystemWindows(false);
        chart.setDescription(null);

        chart.setData(data);
        chart.invalidate();
    }


}