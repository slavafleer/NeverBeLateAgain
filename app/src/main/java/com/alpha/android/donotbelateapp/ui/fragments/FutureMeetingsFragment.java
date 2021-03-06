package com.alpha.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alpha.android.donotbelateapp.GlobalApplication;
import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.adapters.MeetingAdapter;
import com.parse.ParseObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class FutureMeetingsFragment extends Fragment {
    @InjectView(R.id.fragmentFutureMeetingsRecyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.fragmentFutureMeetingEmpty)
    TextView mEmpty;
    @InjectView(R.id.fragmentFutureHeaderSubject)
    TextView mSubject;
    @InjectView(R.id.fragmentFutureHeaderDateTime)
    TextView mDateTime;
    @InjectView(R.id.fragmentFutureHeaderLocation)
    TextView mLocation;
    @InjectView(R.id.fragmentFutureHeaderReminder)
    TextView mReminder;
    @InjectView(R.id.fragmentFutureHeaderStatus)
    TextView mStatus;
    private GlobalApplication Global;
    private MeetingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_future_meetings, container, false);
        ButterKnife.inject(this, rootView);
        Global = (GlobalApplication) getActivity().getApplication();

        List<ParseObject> futureMeetings = Global.getFutureMeetings();
        adapter = new MeetingAdapter(futureMeetings);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mSubject.setVisibility(View.INVISIBLE);
            mDateTime.setVisibility(View.INVISIBLE);
            mLocation.setVisibility(View.INVISIBLE);
            mReminder.setVisibility(View.INVISIBLE);
            mStatus.setVisibility(View.INVISIBLE);
        } else {
            mEmpty.setVisibility(View.INVISIBLE);
            mSubject.setVisibility(View.VISIBLE);
            mDateTime.setVisibility(View.VISIBLE);
            mLocation.setVisibility(View.VISIBLE);
            mReminder.setVisibility(View.VISIBLE);
            mStatus.setVisibility(View.VISIBLE);
        }
    }
}
