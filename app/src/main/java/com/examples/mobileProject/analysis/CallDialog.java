package com.examples.mobileProject.analysis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.examples.mobileProject.R;

public class CallDialog extends AppCompatActivity {
    private Button mNegativeButton;
    private View.OnClickListener mNegativeListener;
    TextView txtCall_1, txtCall_2, txtCall_3;
    ImageButton btnCall_1, btnCall_2, btnCall_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_dialog);
        btnCall_1 = (ImageButton) findViewById(R.id.imgBtnCall_1);
        btnCall_2 = (ImageButton) findViewById(R.id.imgBtnCall_2);
        btnCall_3 = (ImageButton) findViewById(R.id.imgBtnCall_3);
        txtCall_1 = (TextView) findViewById(R.id.txtCall_1);
        txtCall_2 = (TextView) findViewById(R.id.txtCall_2);
        txtCall_3 = (TextView) findViewById(R.id.txtCall_3);

        if(!AnalysisChartActivity.callStr.isEmpty()) {
            txtCall_1.setText(AnalysisChartActivity.callStr.get(0).toString());
            txtCall_2.setText(AnalysisChartActivity.callStr.get(1).toString());
            txtCall_3.setText(AnalysisChartActivity.callStr.get(2).toString());
        }
        btnCall_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AnalysisChartActivity.callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/" + AnalysisChartActivity.callStr.get(0)));
                    startActivity(mIntent);
                }
            }
        });
        btnCall_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AnalysisChartActivity.callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("tel:/" + AnalysisChartActivity.callStr.get(1)));
                    startActivity(mIntent);
                }
            }
        });
        btnCall_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AnalysisChartActivity.callStr.isEmpty()) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("tel:/" + AnalysisChartActivity.callStr.get(2)));
                    startActivity(mIntent);
                }
            }
        });

    }
}
