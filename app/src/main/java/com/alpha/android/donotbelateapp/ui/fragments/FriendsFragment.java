package com.alpha.android.donotbelateapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alpha.android.donotbelateapp.GlobalApplication;
import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 09/06/2015.
 */
public class FriendsFragment extends ListFragment {

    private GlobalApplication Global;
    @InjectView(R.id.fragmentFriendsEmpty) TextView mEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.inject(this, rootView);

        Global = (GlobalApplication) getActivity().getApplication();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        List<ParseUser> friends = Global.getFriends();
        int usersAmount = 0;
        if(friends != null) {
            usersAmount = friends.size();
        }
        if(usersAmount == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.INVISIBLE);
            String[] fullNames = new String[usersAmount];
            int i = 0;
            for (ParseUser user : friends) {
                fullNames[i] = user.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                        user.getString(ParseConstants.KEY_LASTNAME);
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getListView().getContext(),
                    android.R.layout.simple_list_item_1,
                    fullNames);
            setListAdapter(adapter);
        }
    }
}
