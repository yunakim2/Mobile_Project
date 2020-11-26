package com.examples.mobileProject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.mobileProject.R;
import com.examples.mobileProject.analysis.AnalysisData;
import com.examples.mobileProject.chart.ImgData;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<ImgData> mList;
    private Context context;

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_photo,parent,false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.photo.setImageBitmap(byteArrayToBitmap(mList.get(position).getImg()));
    }

    @Override
    public int getItemCount() {
        return (null!= mList? mList.size():0);
    }

    public class  PhotoViewHolder extends RecyclerView.ViewHolder{
        protected ImageView photo;

        public PhotoViewHolder(View view) {
            super(view);
            this.photo = view.findViewById(R.id.imgRvPhoto);

        }
    }

    public PhotoAdapter(ArrayList<ImgData> list, Context context) {
        this.mList = list;
        this.context = context;

    }

    public Bitmap byteArrayToBitmap(byte[] $byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap ;
    }
}
