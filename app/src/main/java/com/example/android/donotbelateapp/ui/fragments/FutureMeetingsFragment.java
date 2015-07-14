package com.example.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.donotbelateapp.GlobalApplication;
import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.adapters.MeetingAdapter;
import com.parse.ParseObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class FutureMeetingsFragment extends Fragment {
    private GlobalApplication Global;

    @InjectView(R.id.fragmentFutureMeetingsRecyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.fragmentFutureMeetingEmpty) TextView mEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_future_meetings, container, false);
        ButterKnife.inject(this, rootView);
        Global = (GlobalApplication) getActivity().getApplication();

        mEmpty.setVisibility(View.INVISIBLE);

        List<ParseObject> futureMeetings = Global.getFutureMeetings();
        MeetingAdapter adapter = new MeetingAdapter(futureMeetings);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

}
