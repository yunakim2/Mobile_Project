package com.examples.mobileProject.TextTranslation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.examples.mobileProject.R;


public class TextTranslationActivity extends AppCompatActivity {

    EditText edtText ;
    TextView tvTransText;
    Button btnTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translation);

        edtText = findViewById(R.id.editDiaryText);
        tvTransText = findViewById(R.id.tvTransText);
        btnTrans = findViewById(R.id.btnTrans);

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        PapagoTextTranslate tranMode = new PapagoTextTranslate();
                        String result ;
                        String str = edtText.getText().toString();
                        result = tranMode.getTranslation(str,"ko","en");
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString("resultWord",result);
                        Message msg = transper_handler.obtainMessage();
                        msg.setData(resultBundle);
                        transper_handler.sendMessage(msg);
                    }
                }.start();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    Handler transper_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            Bundle bundle = msg.getData();
            String resultText = bundle.getString("resultWord");
            tvTransText.setText(resultText);
        }

    };
}