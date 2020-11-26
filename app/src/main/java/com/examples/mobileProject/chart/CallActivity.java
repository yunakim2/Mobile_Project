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
//    static ArrayList<String> callStr = new ArrayList<String>();
//    static ArrayList<CallData> callData = new ArrayList<CallData>();
//    static ArrayList<String> DisplayNameStr = new ArrayList<String>();
//    TextView txtCall_1, txtCall_2, txtCall_3;
//    ImageButton btnCall_1, btnCall_2, btnCall_3;
    static boolean isCreated = false;
    RecyclerView mRecyclerView = null ;
    CallAdapter mAdapter = null ;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_dialog);

//        if(!isCreated) {
//            getCallHistory(); isCreated = true;
//        }

        mRecyclerView = findViewById(R.id.recycler2);
        mAdapter = new CallAdapter(callData, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CallAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Toast.makeText(getApplicationContext(),"pos : "+pos,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"+AnalysisChartActivity.callStr.get(pos)));
                startActivity(intent);
            }
        });

    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public boolean getCallHistory(){
//        boolean success = true;
//        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME};
//        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);
//
//        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};
//        String name = "";
//
//        String number;
//        int cnt=0;
//        //오류 잡기 => calllog 목록 수 -1 만큼 반복
//        c.moveToFirst();
//        while(c.moveToNext()){
//            cnt ++;
//        }
//        System.out.println(cnt);
//        c.moveToFirst();
//        try {
//            for (int i = 0; i < cnt; i++) {
//                number = c.getString(2);
//
//
//                String pn = number;
//                Uri uriForContactName = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(pn));
//                Cursor c2 = getBaseContext().getContentResolver().query(uriForContactName, projection, null, null, null); //전화번호부 접근
//
//
//                if (c.getInt(1) == CallLog.Calls.OUTGOING_TYPE && (!callStr.contains(number)) && (c2.moveToFirst())) {
//                    //발신 목록 중, 발신한 전화번호가 연락처 목록에 있는 경우에만, 중복번호를 제외하고 list에 넣음.
//                    callStr.add(number);
//                    name = c2.getString(0);
//                    DisplayNameStr.add(name);
//
//                    System.out.println(name);
//                    System.out.println(number);
//
//                    callData.add(new CallData(name,getDrawable(R.drawable.call_icon)));
//                    //i++;
//                }
//
//                c.moveToNext();
//            }
//        }catch (CursorIndexOutOfBoundsException e){
//            success = false;
//            throw e;
//        }
//        c.close();
//        return success;
//    }
}
