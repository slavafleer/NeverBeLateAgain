package com.example.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.donotbelateapp.R;

/**
 * Created by Slava on 09/06/2015.
 */
public class FutureMeetingsFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fututre_meetings, container, false);
        return rootView;
    }
}
