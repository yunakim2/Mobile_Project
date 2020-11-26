package com.examples.mobileProject.chart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.examples.mobileProject.adapter.AnalysisAdapter;
import com.examples.mobileProject.adapter.CallAdapter;
import com.examples.mobileProject.adapter.PhotoAdapter;
import com.examples.mobileProject.analysis.AnalysisData;
import com.examples.mobileProject.realmDB.CalendarData;
import com.examples.mobileProject.realmDB.realmDB;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.examples.mobileProject.R.layout.calendar_dialog;
import static com.examples.mobileProject.R.layout.call_dialog;
import static com.examples.mobileProject.R.layout.empty_calllog_dialog;
import static java.security.AccessController.getContext;

public class AnalysisChartActivity extends AppCompatActivity {
    boolean isSucceed = true;
    static boolean isCreated = false;
    View dialogView;
    Button btnContact, btnCallCancel, btnCancle;
    TextView tvChartTitle1, tvChartTitle2, tvEmotionCal, tvCallBtn, tvPhotoTitle;
    LineChart chart ;
    ImageView imgEmotion;
    int startDate;
    static ArrayList<String> callStr = new ArrayList<String>();
    static ArrayList<CallData> callData = new ArrayList<CallData>();
    static ArrayList<String> DisplayNameStr = new ArrayList<String>();
    private static ArrayList<ImgData> img = new ArrayList<ImgData>();
    private static realmDB myDB;
    private Realm realm;
    RecyclerView mRecyclerView = null ;
    PhotoAdapter mAdapter = null ;
    RecyclerView mCallRecycler = null ;
    CallAdapter mCallAdapter = null ;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_chart);

        myDB = new realmDB();
        realm = Realm.getDefaultInstance();
        myDB.setRealm(realm);


        tvChartTitle1 = findViewById(R.id.tvChartTitle);
        tvChartTitle2 = findViewById(R.id.tvChartWeek);
        tvEmotionCal = findViewById(R.id.tvChartEmotion);
        tvCallBtn = findViewById(R.id.tvChartCall);
        imgEmotion = findViewById(R.id.imgEmotion);
        tvPhotoTitle = findViewById(R.id.txtPhoto);

        tvCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(getApplicationContext(),PERMISSIONS)){
                    getPermission();

                } else {
                    if(!isCreated) {
                        isSucceed = getCallHistory(); isCreated = true;
                    }
                    initCall();
                }

            }
        });



        Intent intent = getIntent();
        startDate = intent.getIntExtra("startDate",0);
        String title = intent.getStringExtra("title");

        tvChartTitle1.setText(title);
        tvChartTitle2.setText(title);

        chart = findViewById(R.id.chartBar);

        try {
            initChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPhoto();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(Build.VERSION.SDK_INT>=23) {
            if(grantResults.length > 0  && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.d("PERMITTION","Permission: "+permissions[0]+ "was "+grantResults[0]);

            }
            // 퍼미션이 승인 거부되면
            else {
                Toast.makeText(this,"전화 걸기 기능을이용할 수 없습니다. \n권한 설정을 허용해주세요!",Toast.LENGTH_SHORT).show();
                Log.d("PERMITTION","Permission denied");
            }

        }
    }

    private void getPermission(){

        ActivityCompat.requestPermissions(this,
                new String[] {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CALL_LOG
        }, 1000);


    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void initCall() {
                String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};

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

                    AlertDialog dialog = null;
                    dialog = showCallDialog(dialog);

                    mCallAdapter = new CallAdapter(callData,dialog.getContext());
                    btnCallCancel = dialogView.findViewById(R.id.btnCallCancel);
                    mCallRecycler = dialogView.findViewById(R.id.recycler2);
                    mCallRecycler.setAdapter(mCallAdapter);
                    mCallRecycler.setLayoutManager(new LinearLayoutManager(dialog.getContext())) ;
                    mCallAdapter.notifyDataSetChanged() ;


                    mCallAdapter.setOnItemClickListener(new CallAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"+AnalysisChartActivity.callStr.get(pos)));
                            startActivity(intent);
                        }
                    });
                    AlertDialog finalDialog = dialog;
                    btnCallCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finalDialog.dismiss();
                        }
                    });
                }
    }
    private void initPhoto() {
        mAdapter = new PhotoAdapter(img, this);
        mRecyclerView = findViewById(R.id.rvPhoto);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)) ;
        mAdapter.notifyDataSetChanged() ;

        if(img.size()!=0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvPhotoTitle.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            tvPhotoTitle.setVisibility(View.INVISIBLE);

        }
    }

    private void initChart() throws Exception {
        ArrayList  neg = new ArrayList();
        ArrayList  pos = new ArrayList();
        ArrayList  date = new ArrayList();
        Float SumNeg =0.0f;
        Float SumPos = 0.0f;

        RealmResults<CalendarData> calendarData = myDB.getWeekDiaryList(startDate,Integer.parseInt(addDate(Integer.toString(startDate),0,0,+7)));
        img.clear();
        for(int i =0 ; i<calendarData.size() ; i++) {
            System.out.println(calendarData.get(i).getDate());
            System.out.println(calendarData.get(i).getNegative());
            System.out.println(calendarData.get(i).getPositive());
            String intDate = Integer.toString(calendarData.get(i).getDate());

            if(calendarData.get(i).getImg()!=null) {
                img.add(new ImgData(calendarData.get(i).getImg()));
            }


            String inputDate = intDate.substring(4,6)+"/"+intDate.substring(6,8);

            neg.add(new BarEntry(calendarData.get(i).getNegative(),i));
            pos.add(new BarEntry(calendarData.get(i).getPositive(), i));
            SumNeg += calendarData.get(i).getNegative();
            SumPos+=calendarData.get(i).getPositive();
            date.add(inputDate);
        }

        SumNeg = SumNeg/calendarData.size();
        SumPos = SumPos/calendarData.size();
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
    public AlertDialog showDialog(AlertDialog dialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(empty_calllog_dialog, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        return dialog;
    }

    public AlertDialog showCallDialog(AlertDialog dialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(call_dialog, null);
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
    private static String addDate(String dt, int y, int m, int d) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date date = format.parse(dt);
        cal.setTime(date);
        cal.add(Calendar.YEAR, y);
        cal.add(Calendar.MONTH, m);
        cal.add(Calendar.DATE, d);
        return format.format(cal.getTime()); }


}