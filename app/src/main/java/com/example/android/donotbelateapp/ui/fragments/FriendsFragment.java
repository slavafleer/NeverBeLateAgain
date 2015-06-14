package com.example.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class FriendsFragment extends ListFragment {



    @InjectView(R.id.fragmentFriendsSpinner) ProgressBar mSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.inject(this, rootView);
        mSpinner.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getListView().getContext(),
                android.R.layout.simple_list_item_1,
                MainActivity.mFullNames);
        setListAdapter(adapter);
    }
}
