package com.examples.mobileProject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.mobileProject.R;
import com.examples.mobileProject.analysis.AnalysisData;
import com.examples.mobileProject.chart.CallData;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    private ArrayList<CallData> mList;
    private Context context;

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_list,parent,false);
        CallViewHolder viewHolder = new CallViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        holder.txt.setText(mList.get(position).getTitle());
        holder.btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_call_24));

//        holder.emoji.setColorFilter(ContextCompat.getColor(context,R.color.colorGrey));

    }

    @Override
    public int getItemCount() {
        return (null!= mList? mList.size():0);
    }

    public class  CallViewHolder extends RecyclerView.ViewHolder{
        protected TextView txt;
        protected ImageView btn;

        public CallViewHolder(View view) {
            super(view);
            this.txt = view.findViewById(R.id.txtCall);
            this.btn = view.findViewById(R.id.imgBtnCall);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION) mListener.onItemClick(view, pos);

                }
            });
        }
    }

    public CallAdapter(ArrayList<CallData> list,Context context) {
        this.mList = list;
        this.context = context;

    }
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
