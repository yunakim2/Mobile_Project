package com.examples.mobileProject.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.examples.mobileProject.R;
import com.examples.mobileProject.realmDB.CalendarData;
import com.examples.mobileProject.realmDB.realmDB;
import com.examples.mobileProject.texttranslation.PapagoTextTranslate;

import org.tensorflow.lite.examples.mobileProject.client.Result;
import org.tensorflow.lite.examples.mobileProject.client.TextClassificationClient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;
import static com.examples.mobileProject.R.*;
import static com.examples.mobileProject.R.drawable.*;
import static com.examples.mobileProject.R.drawable.sad_brown;
import static com.examples.mobileProject.R.layout.calendar_dialog;


public class CalendarFragment extends Fragment {
    private Realm realm;
    static int curDay, curMonth, curYear;
    String curDate, content;
    View dialogView;
    EditText edtDiary;
    CalendarView calendar;
    TextView tvDlgTitle;
    Button btnDlgOK, btnDlgCancel, btnDlgAnalysis;
    ImageView imgGallery, imgPhoto, imgEmotion;
    public static final int GET_IMG= 1000;
    Bitmap imgBitmap;
    private static Handler handler;
    private static TextClassificationClient client;
    private static realmDB myDB;
    String clientId, secret;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myDB = new realmDB();
        realm = Realm.getDefaultInstance();
        myDB.setRealm(realm);
        clientId = getActivity().getResources().getString(string.papago_client_id);
        secret = getActivity().getResources().getString(string.papago_client_secret);


        calendar = (CalendarView)getView().findViewById(id.calendarView);
        client = new TextClassificationClient(getContext());
        handler = new Handler();

        initCalendarDlg();

    }
    private AlertDialog showDialog(AlertDialog dialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(calendar_dialog, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }
    private void initCalendarDlg() {
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                curDay = dayOfMonth; curMonth = month+1; curYear = year;
                AlertDialog dialog = null;
                dialog = showDialog(dialog);

                edtDiary = dialogView.findViewById(id.edtDiary);
                imgGallery = dialogView.findViewById(id.imgGallery);
                imgPhoto =  dialogView.findViewById(id.imgPhoto);
                imgEmotion = dialogView.findViewById(id.imgEmotion);
                tvDlgTitle = dialogView.findViewById(id.tvDate);
                btnDlgOK = dialogView.findViewById(id.btnOK);
                btnDlgCancel = dialogView.findViewById(id.btnCancle);
                btnDlgAnalysis = dialogView.findViewById(id.btnAnalysis);

                String date = Integer.toString(year)+"년 "+Integer.toString(month+1)+"월 "+Integer.toString(dayOfMonth)+"일";
                String curDays = "";
                if(curDay<10) {
                    curDays = "0"+Integer.toString(curDay);
                } else {
                    curDays = Integer.toString(curDay);
                }

                String curMonths;
                if(curMonth<10) {
                    curMonths = "0"+Integer.toString(curMonth);
                } else {
                    curMonths = Integer.toString(curMonth);
                }
                curDate = Integer.toString(curYear)+curMonths+curDays;
                tvDlgTitle.setText(date);
                readDiary(curDate);


                btnDlgOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            content = edtDiary.getText().toString();
                            if(!content.isEmpty()) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        PapagoTextTranslate tranMode = new PapagoTextTranslate();
                                        String result;

                                        result = tranMode.getTranslation(clientId,secret,content, "ko", "en");
                                        Bundle resultBundle = new Bundle();
                                        resultBundle.putString("resultWord", result);
                                        Message msg = transper_handler.obtainMessage();
                                        msg.setData(resultBundle);
                                        transper_handler.sendMessage(msg);
                                    }
                                }.start();
                            } else {
                                Toast.makeText(getContext(), "일기를 입력해주세요!", Toast.LENGTH_SHORT).show();
                            }

                    }
                });

                AlertDialog finalDialog = dialog;
                btnDlgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalDialog.dismiss();
                    }
                });

                btnDlgAnalysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        content = edtDiary.getText().toString();
                        if(!content.isEmpty()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    PapagoTextTranslate tranMode = new PapagoTextTranslate();
                                    String result;
                                    result = tranMode.getTranslation(clientId,secret,content, "ko", "en");
                                    Bundle resultBundle = new Bundle();
                                    resultBundle.putString("resultWord", result);
                                    Message msg = transper_handler.obtainMessage();
                                    msg.setData(resultBundle);
                                    transper_handler.sendMessage(msg);
                                }
                            }.start();
                        } else {
                            Toast.makeText(getContext(), "일기를 입력해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                imgGallery.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent,GET_IMG);

                    }
                });



            }
        });
    }
    @SuppressLint("HandlerLeak")
    Handler transper_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            Bundle bundle = msg.getData();
            String resultText = bundle.getString("resultWord");
            classify(resultText);
        }

    };
    @Override
    public void onStart() {
        super.onStart();

        handler.post(
                () -> {
                    client.load();
                });
    }
    @Override
    public void onStop() {
        super.onStop();
        handler.post(
                () -> {
                    client.unload();
                });

    }
    private void classify(final String text) {
        handler.post(
                () -> {
                    List<Result> results = client.classify(text);
                    showResult(text, results);
                });
    }
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI

        getActivity().runOnUiThread( () -> {
            float resPos , resNeg;
            Result result = results.get(0);
            if(result.getTitle().equals("Negative")) {
                resNeg = result.getConfidence();
                resPos = 1- resNeg;
            } else {
                resPos = result.getConfidence();
                resNeg = 1-resPos;
            }
            //resPos,resNeg 값과 그때 날짜를 db에 저장.
            byte [] img = null ;
            if(imgBitmap!=null) {
                img = bitmapToByteArray(imgBitmap);
            }
            myDB.updateDiary(Integer.parseInt(curDate),content,resPos,resNeg,img);
            Toast.makeText(getContext(),"일기가 저장되었습니다.",Toast.LENGTH_SHORT).show();
            initEmoji(resPos, resNeg);
        });
    }
    private void initEmoji(float resPos, float resNeg) {
        if(resPos>resNeg) {
            if(resNeg>=0.5) {
                imgEmotion.setImageDrawable(getResources().getDrawable(sad_brown));
            } else {
                imgEmotion.setImageDrawable(getResources().getDrawable(happy_pink));
            }

        } else {
            imgEmotion.setImageDrawable(getResources().getDrawable(sad_brown));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GET_IMG && resultCode == RESULT_OK && data!=null && data.getData()!=null ) {
            Uri selectedImageUri = data.getData();
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
                Glide.with(this).load(imgBitmap).into(imgPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode,resultCode,data);
    }


    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

    public Bitmap byteArrayToBitmap( byte[] $byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap ;
    }

    void readDiary(String date) {

        CalendarData data = myDB.getDiary(Integer.parseInt(date));
        if (data != null) {
            edtDiary.setText(data.getContnet());
            initEmoji(data.getPositive(), data.getNegative());
            System.out.println("date:"+Integer.toString(data.getDate()));
            System.out.println("neg : "+Float.toString(data.getNegative()));
            System.out.println("pos : "+Float.toString(data.getPositive()));
            System.out.println("content :"+data.getContnet());

            if(data.getImg()!=null) {
                imgBitmap = byteArrayToBitmap(data.getImg());
                System.out.println("img:"+imgBitmap.toString());
               Glide.with(this).load(imgBitmap).into(imgPhoto);
            }
        } else {
            edtDiary.setHint("일기를 적어주세요!");
        }
    }

}