package com.examples.mobileProject.analysis;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examples.mobileProject.R;
import com.examples.mobileProject.adapter.AnalysisAdapter;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AnalysisFragment extends Fragment {
    RecyclerView mRecyclerView = null ;
    AnalysisAdapter mAdapter = null ;
    @SuppressLint("ResourceType")



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecycler();

    }
    private void initRecycler() {
        @SuppressLint("ResourceType") ArrayList<AnalysisData> analysis = new ArrayList<AnalysisData>(Arrays.asList(
                new AnalysisData("2020년 11월 1주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)),
                new  AnalysisData("2020년 11월 2주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink)),
                new AnalysisData("2020년 11월 3주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)),
                new AnalysisData("2020년 11월 4주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink)),
                new AnalysisData("2020년 11월 5주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_brown)),
                new  AnalysisData("2020년 12월 1주차",requireActivity().getDrawable(R.drawable.ic_baseline_fact_check_24),requireActivity().getDrawable(R.drawable.item_recycler_bg_pink))
        ));
        mRecyclerView = requireView().findViewById(R.id.recycler1);
        mAdapter = new AnalysisAdapter(analysis,getContext());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        mAdapter.notifyDataSetChanged() ;

    }

}