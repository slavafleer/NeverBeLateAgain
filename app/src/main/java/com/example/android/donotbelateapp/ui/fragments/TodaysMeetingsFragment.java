package com.example.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.donotbelateapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class TodaysMeetingsFragment extends ListFragment {
    @InjectView(R.id.fragmentTodaysMeetingSpinner) ProgressBar mSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todays_meetings, container, false);
        ButterKnife.inject(this, rootView);
        mSpinner.setVisibility(View.INVISIBLE);
        return rootView;
    }
}
