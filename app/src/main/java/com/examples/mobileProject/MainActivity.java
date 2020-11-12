package com.examples.mobileProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.examples.mobileProject.Calendar.CalendarActivity;
import com.examples.mobileProject.TextTranslation.TextTranslationActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCalendar , btnAnalization , btnTextTranslation;


    public static final int sub = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalendar = findViewById(R.id.btnMainCal);
        btnAnalization  = findViewById(R.id.btnMainAnal);
        btnTextTranslation = findViewById(R.id.btnMainText);


        init();
        func();
    }


    private void init() {

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivityForResult(intent,sub);
            }
        });



        btnTextTranslation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextTranslationActivity.class);
                startActivityForResult(intent,sub);
            }
        });

    }
    private void func()
    {

    }

}