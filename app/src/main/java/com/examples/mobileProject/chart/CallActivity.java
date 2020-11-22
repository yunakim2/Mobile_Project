package com.examples.mobileProject.chart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.examples.mobileProject.R;

import java.util.ArrayList;

public class CallActivity extends AppCompatActivity {
    static ArrayList<String> callStr = new ArrayList<String>();
    private Button mNegativeButton;
    private View.OnClickListener mNegativeListener;
    TextView txtCall_1, txtCall_2, txtCall_3;
    ImageButton btnCall_1, btnCall_2, btnCall_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_dialog);
        getCallHistory();
        btnCall_1 = (ImageButton) findViewById(R.id.imgBtnCall_1);
        btnCall_2 = (ImageButton) findViewById(R.id.imgBtnCall_2);
        btnCall_3 = (ImageButton) findViewById(R.id.imgBtnCall_3);
        txtCall_1 = (TextView) findViewById(R.id.txtCall_1);
        txtCall_2 = (TextView) findViewById(R.id.txtCall_2);
        txtCall_3 = (TextView) findViewById(R.id.txtCall_3);

        if(!callStr.isEmpty()) {
            txtCall_1.setText(callStr.get(0).toString());
            txtCall_2.setText(callStr.get(1).toString());
            txtCall_3.setText(callStr.get(2).toString());
        }
        btnCall_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/" + callStr.get(0)));
                    startActivity(mIntent);
                }
            }
        });
        btnCall_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("tel:/" + callStr.get(1)));
                    startActivity(mIntent);
                }
            }
        });
        btnCall_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("tel:/" + callStr.get(2)));
                    startActivity(mIntent);
                }
            }
        });

    }
    public void getCallHistory(){
        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};

        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        if(c.getCount()==0) Toast.makeText(getApplicationContext(),"통화기록 없음", Toast.LENGTH_SHORT).show();

        else {
            String number;
            c.moveToFirst();

            for (int i = 0; i < 3; ) {
                number = c.getString(2);
                if (c.getInt(1) == CallLog.Calls.OUTGOING_TYPE && (!callStr.contains(number))) {
                    callStr.add(number);
                    i++;
                }
                c.moveToNext();
            }

            c.close();
        }
    }
}
