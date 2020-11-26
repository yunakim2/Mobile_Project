package com.examples.mobileProject.chart;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.mobileProject.R;
import com.examples.mobileProject.adapter.CallAdapter;

import java.util.ArrayList;

import static com.examples.mobileProject.chart.AnalysisChartActivity.callData;

public class CallActivity extends AppCompatActivity {
    static boolean isCreated = false;
    RecyclerView mRecyclerView = null ;
    CallAdapter mAdapter = null ;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

//        if(!isCreated) {
//            getCallHistory(); isCreated = true;
//        }

        mRecyclerView = findViewById(R.id.recycler2);
        mAdapter = new CallAdapter(callData, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CallAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"+AnalysisChartActivity.callStr.get(pos)));
                startActivity(intent);
            }
        });

    }
}
