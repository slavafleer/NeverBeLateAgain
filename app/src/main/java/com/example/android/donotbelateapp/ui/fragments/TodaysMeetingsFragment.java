package com.example.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.adapters.MeetingAdapter;
import com.example.android.donotbelateapp.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class TodaysMeetingsFragment extends Fragment {
    @InjectView(R.id.fragmentTodaysMeetingSpinner) ProgressBar mSpinner;
    @InjectView(R.id.fragmentTodaysMeetingsRecyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.fragmentTodaysMeetingEmpty) TextView mEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_todays_meetings, container, false);
        ButterKnife.inject(this, rootView);
        mSpinner.setVisibility(View.INVISIBLE);
        mEmpty.setVisibility(View.INVISIBLE);

        MeetingAdapter adapter = new MeetingAdapter(MainActivity.mTodaysMeetings);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);



        return rootView;
    }

}
