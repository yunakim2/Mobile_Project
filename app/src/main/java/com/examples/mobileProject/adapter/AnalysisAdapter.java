package com.examples.mobileProject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.mobileProject.R;
import com.examples.mobileProject.analysis.AnalysisData;

import java.util.ArrayList;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder> {

    private ArrayList<AnalysisData> mList;
    private Context context;

    @NonNull
    @Override
    public AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis_list,parent,false);
        AnalysisViewHolder viewHolder = new AnalysisViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.emoji.setImageDrawable(mList.get(position).getIconDrawable());
        holder.icon.setBackground(mList.get(position).getColor());

//        holder.emoji.setColorFilter(ContextCompat.getColor(context,R.color.colorGrey));

    }

    @Override
    public int getItemCount() {
        return (null!= mList? mList.size():0);
    }

    public class  AnalysisViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected ConstraintLayout icon;
        protected ImageView emoji;

        public AnalysisViewHolder(View view) {
            super(view);
            this.title = view.findViewById(R.id.tvRecyclerTitle);
            this.icon = view.findViewById(R.id.imgRecyclerIcon);
            this.emoji = view.findViewById(R.id.imgEmoji);

        }
    }

    public AnalysisAdapter(ArrayList<AnalysisData> list,Context context) {
        this.mList = list;
        this.context = context;

    }

}
