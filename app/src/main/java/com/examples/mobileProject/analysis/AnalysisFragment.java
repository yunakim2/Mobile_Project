package com.examples.mobileProject.analysis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.examples.mobileProject.realmDB.CalendarData;
import com.examples.mobileProject.realmDB.realmDB;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AnalysisFragment extends Fragment {
    RecyclerView mRecyclerView = null ;
    AnalysisAdapter mAdapter = null ;
    ConstraintLayout clEmpty ;
    private static realmDB myDB;
    private Realm realm;
    private static ArrayList<AnalysisData> analysis = new ArrayList<AnalysisData>();
    private static int currentDate, currentMonth, currentYear;
    private static String curDateStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myDB = new realmDB();
        realm = Realm.getDefaultInstance();
        myDB.setRealm(realm);

        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clEmpty = getActivity().findViewById(R.id.clEmpty);


        sortDates();



        mAdapter.setOnItemClickListener(new AnalysisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, AnalysisData data) {
                Intent intent = new Intent(getContext(), AnalysisChartActivity.class);
                intent.putExtra("startDate", data.getStartDate());
                intent.putExtra("title",data.getTitle());
                startActivity(intent);

            }
        });

    }
    private void sortDates(){
        analysis.clear();
        Date currentTime = Calendar.getInstance().getTime();
        currentDate = currentTime.getDate(); currentMonth = currentTime.getMonth()+1; currentYear = 2020;
        curDateStr = Integer.toString(currentYear)+Integer.toString(currentMonth)+Integer.toString(currentDate);


        int weekData_1 = myDB.getWeekDiaryListCount(Integer.parseInt(curDateStr)-7, Integer.parseInt(curDateStr));
        int weekData_2 = myDB.getWeekDiaryListCount(Integer.parseInt(curDateStr)-14, Integer.parseInt(curDateStr)-7);
        int weekData_3 = myDB.getWeekDiaryListCount(Integer.parseInt(curDateStr)-21, Integer.parseInt(curDateStr)-14);
        int weekData_4 = myDB.getWeekDiaryListCount(Integer.parseInt(curDateStr)-28, Integer.parseInt(curDateStr)-21);
        int weekData_5 = myDB.getWeekDiaryListCount(Integer.parseInt(curDateStr)-35, Integer.parseInt(curDateStr)-28);

        if(weekData_1!=0) {
            analysis.add( new AnalysisData("최근 일주일",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown),Integer.parseInt(curDateStr)-7));
        }
        if(weekData_2!=0) {
            analysis.add( new AnalysisData("지난 2주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink),Integer.parseInt(curDateStr)-14));

        }
        if(weekData_3!=0) {
            analysis.add( new AnalysisData("지난 3주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown),Integer.parseInt(curDateStr)-21));

        }
        if(weekData_4!=0) {
            analysis.add( new AnalysisData("지난 4주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink),Integer.parseInt(curDateStr)-28));
        }
        if(weekData_5!=0) {
            analysis.add( new AnalysisData("지난 5주",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink),Integer.parseInt(curDateStr)-35));

        }


        mAdapter = new AnalysisAdapter(analysis, getContext());
        mRecyclerView = requireView().findViewById(R.id.recycler1);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        mAdapter.notifyDataSetChanged() ;

        if(analysis.size()!=0) {
            clEmpty.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            clEmpty.setVisibility(View.VISIBLE);
        }


    }

}