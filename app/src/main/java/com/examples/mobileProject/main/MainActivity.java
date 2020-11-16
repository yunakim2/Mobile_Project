package com.examples.mobileProject.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.examples.mobileProject.R;
import com.examples.mobileProject.analysis.AnalysisFragment;
import com.examples.mobileProject.calendar.CalendarFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout clCalendar, clAnalysis;
    ImageView imgCalendar, imgAnalysis;
    FrameLayout flMain;

    List<Menus> list = new ArrayList<Menus>();

    public static final int sub = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clCalendar = findViewById(R.id.cl_main_calendar);
        clAnalysis = findViewById(R.id.cl_main_analysis);
        imgCalendar = findViewById(R.id.img_main_calendar);
        imgAnalysis = findViewById(R.id.img_main_analysis);

        list.add(new Menus(clCalendar, imgCalendar, new CalendarFragment(),0));
        list.add(new Menus(clAnalysis,imgAnalysis, new AnalysisFragment(),1));

        init();
    }
    private void init() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main, new CalendarFragment()).commitAllowingStateLoss();
        imgCalendar.setSelected(true);
        imgCalendar.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorBrown));
        for (Menus menus : list) {
            menus.layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    changeSelected(menus.index);
                }
            });
        }
    }
    public void changeSelected(Integer tapnum) {

        for (Menus menus : list) {
            if (menus.index == tapnum) {
                menus.img.setSelected(true);
                menus.img.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorBrown));
//                menus.img.setBackgroundColor(Color.parseColor("#998E7D"));
            } else {
                menus.img.setSelected(false);
//                menus.img.setColorFilter(R.color.colorPrimary);
                menus.img.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
//                menus.img.setBackgroundColor(Color.parseColor("#E6C0BE"));

            }
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main,list.get(tapnum).fragment)
                .commitAllowingStateLoss();


    }


}