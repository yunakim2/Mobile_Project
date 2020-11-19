package com.examples.mobileProject.analysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.examples.mobileProject.R;

import java.util.ArrayList;

public class RecyclerImageTextAdapter extends RecyclerView.Adapter<RecyclerImageTextAdapter.ViewHolder>{
    private ArrayList<RecyclerItem> mData = null;

    RecyclerImageTextAdapter(ArrayList<RecyclerItem> list){
        mData = list;
    }
    @Override
    public RecyclerImageTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.item_analysis_list, parent, false);
        RecyclerImageTextAdapter.ViewHolder vh = new RecyclerImageTextAdapter.ViewHolder(view);
        
        return vh;
    }
    @Override
    public void onBindViewHolder(RecyclerImageTextAdapter.ViewHolder holder, int position) {

        RecyclerItem item = mData.get(position) ;

        holder.icon.setImageDrawable(item.getIcon()) ;
        holder.title.setText(item.getTitle()) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;
        TextView title ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            icon = itemView.findViewById(R.id.icon) ;
            title = itemView.findViewById(R.id.title) ;
        }
    }
}
