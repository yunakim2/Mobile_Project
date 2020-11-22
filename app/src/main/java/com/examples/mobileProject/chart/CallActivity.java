package com.examples.mobileProject.chart;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.examples.mobileProject.R;

import java.util.ArrayList;

public class CallActivity extends AppCompatActivity {
    static ArrayList<String> callStr = new ArrayList<String>();
    static ArrayList<String> DisplayNameStr = new ArrayList<String>();
    TextView txtCall_1, txtCall_2, txtCall_3;
    ImageButton btnCall_1, btnCall_2, btnCall_3;
    static boolean isCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_dialog);

        if(!isCreated) {
            getCallHistory(); isCreated = true;
        }
        btnCall_1 = (ImageButton) findViewById(R.id.imgBtnCall_1);
        btnCall_2 = (ImageButton) findViewById(R.id.imgBtnCall_2);
        btnCall_3 = (ImageButton) findViewById(R.id.imgBtnCall_3);
        txtCall_1 = (TextView) findViewById(R.id.txtCall_1);
        txtCall_2 = (TextView) findViewById(R.id.txtCall_2);
        txtCall_3 = (TextView) findViewById(R.id.txtCall_3);

        if(!callStr.isEmpty()) {
            txtCall_1.setText("call to "+DisplayNameStr.get(0));
            txtCall_2.setText("call to "+DisplayNameStr.get(1));
            txtCall_3.setText("call to "+DisplayNameStr.get(2));
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
        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};
        String name = "";

        String number;


        c.moveToFirst();

        for (int i = 0; i < 3; ) {
            number = c.getString(2);


            String pn = number;
            Uri uriForContactName = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(pn));
            Cursor c2 = getBaseContext().getContentResolver().query(uriForContactName, projection,null,null,null);


            if (c.getInt(1) == CallLog.Calls.OUTGOING_TYPE && (!callStr.contains(number)) && (c2.moveToFirst())) {
                //발신 목록 중, 발신한 전화번호가 연락처 목록에 있는 경우에만, 중복번호를 제외하고 list에 넣음.
                callStr.add(number);
                name = c2.getString(0);
                DisplayNameStr.add(name);

                System.out.println(name);
                System.out.println(number);
                i++;
            }

            c.moveToNext();
        }
        c.close();
    }
}
