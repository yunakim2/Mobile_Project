package com.examples.mobileProject.analysis;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.examples.mobileProject.adapter.AnalysisAdapter;
import com.examples.mobileProject.calendar.CalendarFragment;
import com.examples.mobileProject.calendar.myDBHelper;
import com.examples.mobileProject.calendar.CalendarFragment;
import com.examples.mobileProject.analysis.AnalysisTotalData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;



@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AnalysisFragment extends Fragment {
    public static boolean isCreated = false;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    RecyclerView mRecyclerView = null ;
    AnalysisAdapter mAdapter = null ;
    @SuppressLint("ResourceType") ArrayList<AnalysisData> analysis = new ArrayList<AnalysisData>();

    @SuppressLint("ResourceType")

    public static ArrayList<Integer> aryDate = new ArrayList();
    public static ArrayList<AnalysisDayData> weekData = new ArrayList<AnalysisDayData>();
    public static AnalysisTotalData totalData;
    public static ArrayList<AnalysisTotalData> totalDataAry = new ArrayList<AnalysisTotalData>();
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

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        mAdapter.notifyDataSetChanged() ;

        mAdapter.setOnItemClickListener(new AnalysisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Toast.makeText(getContext(),"pos : "+pos,Toast.LENGTH_SHORT).show();
                //todo.

            }
        });
        if(!isCreated) collectDates();
        isCreated = true;
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
        ArrayList<AnalysisDayData> weekData_1 = new ArrayList<AnalysisDayData>();
        ArrayList<AnalysisDayData> weekData_2 = new ArrayList<AnalysisDayData>();
        ArrayList<AnalysisDayData> weekData_3 = new ArrayList<AnalysisDayData>();
        ArrayList<AnalysisDayData> weekData_4 = new ArrayList<AnalysisDayData>();

        ArrayList<AnalysisTotalData> totalData_1 = new ArrayList<AnalysisTotalData>();

        String getDayStr, inputStr;
        String curDateStr = Integer.toString(currentYear)+"-"+Integer.toString(currentMonth)+"-"+Integer.toString(currentDate);
        long sub;

        for(int i =0 ; i<weekData.size();i++) {
            getDayStr = Integer.toString(weekData.get(i).date); //format 맞춰줘야함 (yyyy-mm-dd)
            inputStr = getDayStr.toString();
            inputStr = inputStr.substring(0,3)+"0-"+inputStr.substring(3,5)+"-"+inputStr.substring(5,7);
            System.out.println(inputStr);
            System.out.println(curDateStr);


            sub = calcDateBetweenAnB(curDateStr, inputStr);
            System.out.println(sub);


            if(sub<0) ;
            else if(sub<7) weekData_1.add(weekData.get(i));
            else if(sub<14) weekData_2.add(weekData.get(i));
            else if(sub<21) weekData_3.add(weekData.get(i));
            else if(sub<28) weekData_4.add(weekData.get(i));
            else ;
            System.out.println(weekData_1);
      }

        if(!weekData_1.isEmpty()) totalDataAry.add(new AnalysisTotalData(weekData_1,1));
        if(!weekData_2.isEmpty()) totalDataAry.add(new AnalysisTotalData(weekData_2,2));
        if(!weekData_3.isEmpty()) totalDataAry.add(new AnalysisTotalData(weekData_3,3));
        if(!weekData_4.isEmpty()) totalDataAry.add(new AnalysisTotalData(weekData_4,4));

        initRecycler();

    }



    private void initRecycler() {
        System.out.println("totarysize : "+totalDataAry.size());
        for (int i =0 ; i<totalDataAry.size(); i++ ) {
            if(totalDataAry.get(i).week==1) {
                analysis.add( new AnalysisData("최근 일주일",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));
            }
            else if (totalDataAry.get(i).week==2)
            {
                analysis.add( new AnalysisData("지난 2주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));
            }
            else if (totalDataAry.get(i).week==3)
            {
                analysis.add( new AnalysisData("지난 3주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));
            }
            else if (totalDataAry.get(i).week==4)
            {
                analysis.add( new AnalysisData("지난 4주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)));
            }
        }
    }

}