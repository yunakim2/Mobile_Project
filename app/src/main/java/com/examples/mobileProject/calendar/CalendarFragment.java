package com.examples.mobileProject.calendar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import com.examples.mobileProject.R;
import com.examples.mobileProject.texttranslation.PapagoTextTranslate;

import org.tensorflow.lite.examples.mobileProject.client.Result;
import org.tensorflow.lite.examples.mobileProject.client.TextClassificationClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.app.Activity.RESULT_OK;


public class CalendarFragment extends Fragment {
    public static ArrayList<ClipData.Item> items = new ArrayList<>();
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    static float resPos = 0;
    String fileName,imgfileName;
    static int curDay, curMonth, curYear;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myHelper = new myDBHelper(getContext());;
        //myHelper.updateItems();

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calendar = (CalendarView)getView().findViewById(R.id.calendarView);
        client = new TextClassificationClient(getContext());
        handler = new Handler();

        initCalendarDlg();

    }

    private void initCalendarDlg() {
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getContext(), year+"년"+month+"월"+dayOfMonth+"일", Toast.LENGTH_SHORT).show();
                curDay = dayOfMonth; curMonth = month+1; curYear = year;
                dialogView = (View) View.inflate(getContext(), R.layout.calendar_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                AlertDialog dialog = dlg.create();
                edtDiary = dialogView.findViewById(R.id.edtDiary);
                imgGallery = dialogView.findViewById(R.id.imgGallery);
                imgPhoto =  dialogView.findViewById(R.id.imgPhoto);
                imgEmotion = dialogView.findViewById(R.id.imgEmotion);
                tvDlgTitle = dialogView.findViewById(R.id.tvDate);
                btnDlgOK = dialogView.findViewById(R.id.btnOK);
                btnDlgCancel = dialogView.findViewById(R.id.btnCancle);
                btnDlgAnalysis = dialogView.findViewById(R.id.btnAnalysis);
                dialog.setView(dialogView);
                String date = Integer.toString(curYear)+"년 "+Integer.toString(curMonth)+"월 "+Integer.toString(curDay)+"일";
                dialog.show();
                tvDlgTitle.setText(date);
                btnDlgOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            String str = edtDiary.getText().toString();
                            if(!str.isEmpty()) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        PapagoTextTranslate tranMode = new PapagoTextTranslate();
                                        String result;

                                        result = tranMode.getTranslation(str, "ko", "en");
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
                            //

                            FileOutputStream outFs = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                            outFs.write(str.getBytes());
                            if(imgBitmap !=null) {
                                FileOutputStream outImgFs = getActivity().openFileOutput(imgfileName, Context.MODE_PRIVATE);
                                imgBitmap.compress(Bitmap.CompressFormat.PNG,0,outImgFs);
                            }
                            outFs.close();
                            Toast.makeText(getContext(),resPos + "file_" +fileName+"이 저장됨", Toast.LENGTH_SHORT).show();



                        }
                        catch (IOException e){
                            Toast.makeText(getContext(),"err", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnDlgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnDlgAnalysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str = edtDiary.getText().toString();
                        if(!str.isEmpty()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    PapagoTextTranslate tranMode = new PapagoTextTranslate();
                                    String result;

                                    result = tranMode.getTranslation(str, "ko", "en");
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

                fileName = Integer.toString(curYear)+"_"+ Integer.toString(curMonth)+"_"+ Integer.toString(curDay)+".txt";
                imgfileName = Integer.toString(curYear)+"_"+ Integer.toString(curMonth)+"_"+ Integer.toString(curDay)+"_IMG.png";
                String str = readDiary(fileName);
                readImg(imgfileName);
                edtDiary.setText(str);

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

            //**********************
            initAnalization(resultText);

            //edtDiary.setText(resultText);
        }

    };

    private void initAnalization(String resultText) {

        classify(resultText);

    }
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
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);

                    // Show classification result on screen
                    showResult(text, results);
                });
    }
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        getActivity().runOnUiThread( () -> {
//            String textToShow = "Input: " + inputText + "\nOutput:\n";
//            for (int i = 0; i < results.size(); i++) {
//                Result result = results.get(i);
//                textToShow += String.format("    %s: %s\n", result.getTitle(), result.getConfidence()); //
//            }
//            textToShow += "---------\n";
//            edtDiary.setText(textToShow); //일기내용에 분석결과 pos, neg 값 표시
            //위 대신 pos값(실수값)만 resPos변수에 저장.
            Result result = results.get(0);
            resPos = result.getConfidence();
            if(result.getTitle().toString().equals("Negative"))
                resPos = 1 - resPos;

            //resPos 계산 다 하고, resPos값과 그때 날짜를 db에 저장.
            saveDB(resPos);
        });
    }
    private void saveDB(float resPos){
        int dbDate = 1000*curYear+100*curMonth+curDay;
        float dbPos = resPos;
        float dbNeg = 1- resPos;
        boolean find = false;

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT dbDate FROM emotionTBL", null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) == dbDate){
                Log.e("err","4");
                sqlDB.execSQL("UPDATE emotionTBL SET dbPos = '"+dbPos+"', "+"dbNeg = '"+dbNeg+"' WHERE dbDate = '"+dbDate+"';");
                sqlDB.close();
                Log.e("err","5");
                find = true; break;
            }
        }
         //새로운 날짜에 작성시 insert
        if(find == false) {
            Log.e("err","1");
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO emotionTBL VALUES ('"+dbDate+"', '"+dbPos+"', '"+dbNeg+"');");
            Log.e("err","2");
            sqlDB.close();
            Log.e("err","3");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GET_IMG && resultCode == RESULT_OK && data!=null && data.getData()!=null ) {
            Uri selectedImageUri = data.getData();
            imgPhoto.setImageURI(selectedImageUri);
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    void readImg(String fName){
        FileInputStream inFs;
        try {
            inFs = getActivity().openFileInput(fName);
            Bitmap bt = BitmapFactory.decodeStream(inFs);
            if (bt != null) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bt,bt.getWidth(),bt.getHeight(),true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap,0,0,scaledBitmap.getWidth(),scaledBitmap.getHeight(),matrix,true);
                imgPhoto.setImageBitmap(rotatedBitmap);
            } else {
                System.out.println("null!!!!!");
            }
        } catch (IOException e){
            System.out.println("이미지 없음");
        }


    }

    String readDiary(String fName){
        String diaryStr = null;
        FileInputStream inFs;
        try{
            System.out.println(fName);
            inFs = getActivity().openFileInput(fName);
            System.out.println(inFs);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        }
        catch (IOException e){
            edtDiary.setHint("일기 없음");
        }
        return diaryStr;
    }
}