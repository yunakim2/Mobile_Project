package com.examples.mobileProject.calendar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import static com.examples.mobileProject.R.*;
import static com.examples.mobileProject.R.drawable.*;
import static com.examples.mobileProject.R.drawable.sad_brown;
import static com.examples.mobileProject.R.layout.calendar_dialog;


public class CalendarFragment extends Fragment {
    public static ArrayList<ClipData.Item> items = new ArrayList<>();
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
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

        return inflater.inflate(layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

//                dialogView = (View) View.inflate(getContext(), calendar_dialog, null);
//                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
//                AlertDialog dialog = dlg.create();

                edtDiary = dialogView.findViewById(id.edtDiary);
                imgGallery = dialogView.findViewById(id.imgGallery);
                imgPhoto =  dialogView.findViewById(id.imgPhoto);
                imgEmotion = dialogView.findViewById(id.imgEmotion);
                tvDlgTitle = dialogView.findViewById(id.tvDate);
                btnDlgOK = dialogView.findViewById(id.btnOK);
                btnDlgCancel = dialogView.findViewById(id.btnCancle);
                btnDlgAnalysis = dialogView.findViewById(id.btnAnalysis);
//                dialog.setView(dialogView);
                String date = Integer.toString(curYear)+"년 "+Integer.toString(curMonth)+"월 "+Integer.toString(curDay)+"일";
                openDB(curYear,curMonth,curDay);
//                dialog.show();
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
                            Toast.makeText(getContext(),  "file_" +fileName+"이 저장됨", Toast.LENGTH_SHORT).show();



                        }
                        catch (IOException e){
                            Toast.makeText(getContext(),"err", Toast.LENGTH_SHORT).show();
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
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);

                    // Show classification result on screen
                    showResult(text, results);
                });
    }
    private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI

        getActivity().runOnUiThread( () -> {
            float resPos , resNeg;
            Result result = results.get(0);
            System.out.println(result.getTitle() + result.getConfidence());
            if(result.getTitle().equals("Negative")) {
                resNeg = result.getConfidence();
                resPos = 1- resNeg;
            } else {
                resPos = result.getConfidence();
                resNeg = 1-resPos;
            }
            //resPos,resNeg 값과 그때 날짜를 db에 저장.
            saveDB(resPos,resNeg);
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
    private void openDB(int curYear, int curMonth, int curDay) {
        int dbDate = 1000*curYear+100*curMonth+curDay;
        try {
            sqlDB = myHelper.getReadableDatabase();
            Cursor cursor = sqlDB.rawQuery("SELECT * FROM emotionTBL",null);
            if(cursor!=null) {
                if(cursor.moveToFirst()) {
                    do {
                        if(dbDate == cursor.getInt(cursor.getColumnIndex("dbDate"))) {
                            float resPos = cursor.getFloat(cursor.getColumnIndex("dbPos"));
                            float resNeg = cursor.getFloat(cursor.getColumnIndex("dbNeg"));
                            initEmoji(resPos,resNeg);
                            break;
                        }
                    } while(cursor.moveToNext());
                }
            }

        } catch (SQLiteException e) {
            System.out.println(e);
        }


    }
    private void saveDB(float resPos, float resNeg){
        int dbDate = 1000*curYear+100*curMonth+curDay;
        boolean find = false;

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT dbDate FROM emotionTBL", null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) == dbDate){
                Log.e("err","4");
                sqlDB.execSQL("UPDATE emotionTBL SET dbPos = '"+resPos+"', "+"dbNeg = '"+resNeg+"' WHERE dbDate = '"+dbDate+"';");
                sqlDB.close();
                Log.e("err","5");
                find = true; break;
            }
        }
        //새로운 날짜에 작성시 insert
        if(find == false) {
            Log.e("err","1");
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO emotionTBL VALUES ('"+dbDate+"', '"+resPos+"', '"+resNeg+"');");
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