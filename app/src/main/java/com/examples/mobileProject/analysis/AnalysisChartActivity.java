package com.examples.mobileProject.analysis;

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
import android.widget.TextView;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalysisChartActivity extends AppCompatActivity {
    ImageButton btnCall_1, btnCall_2, btnCall_3;
    TextView txtCall_1, txtCall_2, txtCall_3;
    ArrayList<String> callStr = new ArrayList<String>();
    BarChart chart ;
    ArrayList<AnalysisDayData> data = new ArrayList<AnalysisDayData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        btnCall_1 = (ImageButton) findViewById(R.id.imgBtnCall_1);
        btnCall_2 = (ImageButton) findViewById(R.id.imgBtnCall_2);
        btnCall_3 = (ImageButton) findViewById(R.id.imgBtnCall_3);
        txtCall_1 = (TextView) findViewById(R.id.txtCall_1);
        txtCall_2 = (TextView) findViewById(R.id.txtCall_2);
        txtCall_3 = (TextView) findViewById(R.id.txtCall_3);

        Intent intent = getIntent();
        data = (ArrayList<AnalysisDayData>) intent.getSerializableExtra("datas");
        chart = findViewById(R.id.chartBar);
        getCallHistory();
        initChart();

        btnCall_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("tel:/"+ callStr.get(0)));
                startActivity(mIntent);
            }
        });
        btnCall_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("tel:/"+ callStr.get(1)));
                startActivity(mIntent);
            }
        });
        btnCall_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("tel:/"+ callStr.get(2)));
                startActivity(mIntent);
            }
        });

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
    public void getCallHistory(){
        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};

        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        if(c==null) Toast.makeText(getApplicationContext(),"통화기록 없음", Toast.LENGTH_SHORT).show();


        String number;
        c.moveToFirst();

        for(int i=0;i<3;){
            number = c.getString(2);
            if(c.getInt(1)==CallLog.Calls.OUTGOING_TYPE && (!callStr.contains(number))) {
                callStr.add(number); i++;
            }
            c.moveToNext();
        }

        c.close();
        txtCall_1.setText(callStr.get(0).toString());
        txtCall_2.setText(callStr.get(1).toString());
        txtCall_3.setText(callStr.get(2).toString());
    }

}