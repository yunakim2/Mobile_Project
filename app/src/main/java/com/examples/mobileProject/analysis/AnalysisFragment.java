package com.examples.mobileProject.analysis;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examples.mobileProject.R;

import java.util.ArrayList;

public class AnalysisFragment extends Fragment {
    RecyclerView mRecyclerView = null ;
    RecyclerImageTextAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = requireView().findViewById(R.id.recycler1);
        mAdapter = new RecyclerImageTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addItem(requireActivity().getDrawable(R.drawable.cat),
                    "Box");
            addItem(requireActivity().getDrawable(R.drawable.ic_baseline_add_photo_alternate_24),
                    "Circle");
            addItem(requireActivity().getDrawable(R.drawable.ic_baseline_calendar_today_24),
                    "Third");
            mAdapter.notifyDataSetChanged() ;
        }

    }
    public void addItem(Drawable icon, String title) {
        RecyclerItem item = new RecyclerItem();

        item.setIcon(icon);
        item.setTitle(title);

        mList.add(item);
    }
}