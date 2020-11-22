package com.examples.mobileProject.analysis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.examples.mobileProject.adapter.AnalysisAdapter;
import com.examples.mobileProject.calendar.myDBHelper;
import com.examples.mobileProject.chart.AnalysisChartActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AnalysisFragment extends Fragment {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    RecyclerView mRecyclerView = null ;
    AnalysisAdapter mAdapter = null ;
    ArrayList<AnalysisData> analysis = new ArrayList<AnalysisData>();

    ArrayList<AnalysisDayData> weekData_1 = new ArrayList<AnalysisDayData>();
    ArrayList<AnalysisDayData> weekData_2 = new ArrayList<AnalysisDayData>();
    ArrayList<AnalysisDayData> weekData_3 = new ArrayList<AnalysisDayData>();
    ArrayList<AnalysisDayData> weekData_4 = new ArrayList<AnalysisDayData>();

    public static ArrayList<AnalysisDayData> weekData = new ArrayList<AnalysisDayData>();
    public static int currentDate, currentMonth, currentYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myHelper = new myDBHelper(getContext());;
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = requireView().findViewById(R.id.recycler1);
        mAdapter = new AnalysisAdapter(analysis,getContext());

        mAdapter.setOnItemClickListener(new AnalysisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Toast.makeText(getContext(),"pos : "+pos,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AnalysisChartActivity.class);
                if(pos == 0) {
                    intent.putExtra("datas",weekData_1);
                    intent.putExtra("title","최근 일주일");
                }
                if(pos == 1) {
                    intent.putExtra("datas",weekData_2);
                    intent.putExtra("title","지난 2주");
                }
                if(pos == 2) {
                    intent.putExtra("datas",weekData_2);
                    intent.putExtra("title","지난 3주");
                }
                if(pos == 3) {
                    intent.putExtra("datas",weekData_3);
                    intent.putExtra("title","지난 4주");
                }
                if(pos == 4) {
                    intent.putExtra("datas",weekData_4);
                    intent.putExtra("title","지난 5주");
                }
                startActivity(intent);

            }
        });
        initDataClear();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        mAdapter.notifyDataSetChanged() ;
        collectDates();

    }
    private void initDataClear() {
        weekData_1.clear();
        weekData_2.clear();
        weekData_3.clear();
        weekData_4.clear();
        weekData.clear();
        analysis.clear();
    }
    private void collectDates(){
        Date currentTime = Calendar.getInstance().getTime();
        currentDate = currentTime.getDate(); currentMonth = currentTime.getMonth()+1; currentYear = 2020;

        sqlDB = myHelper.getReadableDatabase();
        int date; float pos; float neg;
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM emotionTBL", null);
        while(cursor.moveToNext()) {
            date = cursor.getInt(0);
            pos = cursor.getFloat(1);
            neg = cursor.getFloat(2);

            weekData.add(new AnalysisDayData(date, pos, neg));
        }
        sortDates();
    }
    private long calcDateBetweenAnB(String date1, String date2){
        try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            //calDateDays = Math.abs(calDateDays);

            System.out.println("두 날짜의 날짜 차이: "+calDateDays);
            return calDateDays;
        }
        catch(ParseException e)
        {
            // 예외 처리
        }
        return 0;
    }
    private void sortDates(){


        String getDayStr, inputStr;
        String curDateStr = Integer.toString(currentYear)+"-"+Integer.toString(currentMonth)+"-"+Integer.toString(currentDate);
        long sub;

        for(int i =0 ; i<weekData.size();i++) {
            getDayStr = Integer.toString(weekData.get(i).date); //format 맞춰줘야함 (yyyy-mm-dd)
            inputStr = getDayStr.substring(0,3)+"0-"+getDayStr.substring(3,5)+"-"+getDayStr.substring(5,7);

            System.out.println(curDateStr);
            System.out.println(inputStr);
            sub = calcDateBetweenAnB(curDateStr, inputStr);
            if(sub<7) weekData_1.add(weekData.get(i));
            else if(sub<14) weekData_2.add(weekData.get(i));
            else if(sub<21) weekData_3.add(weekData.get(i));
            else if(sub<28) weekData_4.add(weekData.get(i));
      }

        System.out.println(weekData_1.size());
        System.out.println(weekData_2.size());
        System.out.println(weekData_3.size());
        System.out.println(weekData_4.size());


        if(weekData_1.size()!=0) {
            analysis.add( new AnalysisData("최근 일주일",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));

        }
        if(weekData_2.size()!=0) {
            analysis.add( new AnalysisData("지난 2주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink)));

        }
        if(weekData_3.size()!=0) {
            analysis.add( new AnalysisData("지난 3주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));

        }
        if(weekData_4.size()!=0) {
            analysis.add( new AnalysisData("지난 4주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink)));
        }
    }
}