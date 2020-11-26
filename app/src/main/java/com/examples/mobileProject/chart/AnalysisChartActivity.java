package com.examples.mobileProject.chart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
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

import static com.examples.mobileProject.R.layout.calendar_dialog;
import static com.examples.mobileProject.R.layout.empty_calllog_dialog;

public class AnalysisChartActivity extends AppCompatActivity {
    boolean isSucceed = true;
    static boolean isCreated = false;
    View dialogView;
    Button btnContact, btnCancle;
    TextView tvChartTitle1, tvChartTitle2, tvEmotionCal, tvCallBtn, tvSolutionBtn;
    LineChart chart ;
    ImageView imgEmotion;
    ArrayList<AnalysisDayData> data = new ArrayList<AnalysisDayData>();
    static ArrayList<String> callStr = new ArrayList<String>();
    static ArrayList<CallData> callData = new ArrayList<CallData>();
    static ArrayList<String> DisplayNameStr = new ArrayList<String>();
    String number;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);
        if(!isCreated) {
            isSucceed = getCallHistory(); isCreated = true;
        }


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

        initChart();


        tvCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};
                Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);
                if(callData.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "통화기록 없음!", Toast.LENGTH_SHORT).show();
                    AlertDialog dialog = null;
                    dialog = showDialog(dialog);
                    btnContact = dialogView.findViewById(R.id.btnContact);
                    btnCancle = dialogView.findViewById(R.id.btnCancle);
                    AlertDialog finalDialog = dialog;
                    btnContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"));
                            startActivity(intent);

                        }
                    });
                    btnCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finalDialog.dismiss();
                        }
                    });
                }
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
    private AlertDialog showDialog(AlertDialog dialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(empty_calllog_dialog, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        return dialog;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean getCallHistory(){
        boolean success = true;
        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};
        String name = "";

        String number;
        int cnt=0;
        //오류 잡기 => calllog 목록 수 -1 만큼 반복
        c.moveToFirst();
        while(c.moveToNext()){
            cnt ++;
        }
        System.out.println(cnt);
        c.moveToFirst();
        try {
            for (int i = 0; i < cnt; i++) {
                number = c.getString(2);


                String pn = number;
                Uri uriForContactName = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(pn));
                Cursor c2 = getBaseContext().getContentResolver().query(uriForContactName, projection, null, null, null); //전화번호부 접근


                if (c.getInt(1) == CallLog.Calls.OUTGOING_TYPE && (!callStr.contains(number)) && (c2.moveToFirst())) {
                    //발신 목록 중, 발신한 전화번호가 연락처 목록에 있는 경우에만, 중복번호를 제외하고 list에 넣음.
                    callStr.add(number);
                    name = c2.getString(0);
                    DisplayNameStr.add(name);

                    System.out.println(name);
                    System.out.println(number);

                    callData.add(new CallData(name,getDrawable(R.drawable.call_icon)));
                    //i++;
                }
                c.moveToNext();
            }
        }catch (CursorIndexOutOfBoundsException e){
            success = false;
            throw e;
        }
        c.close();
        return success;
    }

}