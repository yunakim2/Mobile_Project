package com.examples.mobileProject.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.examples.mobileProject.R;
import com.examples.mobileProject.texttranslation.PapagoTextTranslate;

import org.tensorflow.lite.examples.mobileProject.client.Result;
import org.tensorflow.lite.examples.mobileProject.client.TextClassificationClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CalendarFragment extends Fragment {

    String fileName,imgfileName;
    static int curDay, curMonth, curYear;
    View dialogView;
    EditText edtDiary;
    CalendarView calendar;
    ImageView imgGallery, imgPhoto, imgTranslation;
    public static final int GET_IMG= 1000;
    Bitmap imgBitmap;
    private static Handler handler;
    private static TextClassificationClient client;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                dialogView = (View) View.inflate(getContext(), R.layout.dialog1, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                edtDiary = dialogView.findViewById(R.id.edtDiary);
                imgGallery = dialogView.findViewById(R.id.imgGallery);
                imgPhoto =  dialogView.findViewById(R.id.imgPhoto);
                imgTranslation = dialogView.findViewById(R.id.imgTranslation);

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

                imgTranslation.setOnClickListener(new View.OnClickListener() {
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

                dlg.setTitle(curYear+"년 "+curMonth+"월 "+curDay+"일");
                dlg.setIcon(R.drawable.cat);
                dlg.setView(dialogView);
                dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            FileOutputStream outFs = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);

                            String str = edtDiary.getText().toString();
                            outFs.write(str.getBytes());
                            if(imgBitmap !=null) {
                                FileOutputStream outImgFs = getActivity().openFileOutput(imgfileName, Context.MODE_PRIVATE);
                                imgBitmap.compress(Bitmap.CompressFormat.PNG,0,outImgFs);
                            }
                            outFs.close();
                            Toast.makeText(getContext(),fileName+"이 저장됨", Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e){
                            Toast.makeText(getContext(),"err", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dlg.show();

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
            String textToShow = "Input: " + inputText + "\nOutput:\n";
            for (int i = 0; i < results.size(); i++) {
                Result result = results.get(i);
                textToShow += String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
            }
            textToShow += "---------\n";
            edtDiary.setText(textToShow);
        });
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