package com.alpha.android.donotbelateapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alpha.android.donotbelateapp.GlobalApplication;
import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ChooseInviteesActivity extends ActionBarActivity {
    public final static String INVITEES_AMOUNT = "inviteesAmount";
    public final static String INVITEE = "invitee";

    private List<ParseUser> mFriends;
    private ArrayList<ParseUser> mInvitees = new ArrayList<>();
    private ParseUser mCurrentUser;
    private ParseRelation<ParseUser> mFriendsRelation;
    private String[] mFullNames;
    private ArrayList<String> mInviteesList = new ArrayList<>();
    private GlobalApplication Global;

    @InjectView(R.id.chooseInviteesList) ListView mFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_invitees);
        ButterKnife.inject(this);

        Global = (GlobalApplication)getApplication();

        // Edit invitees
        mFriendsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mFriendsList.isItemChecked(position)) {
                    // Add the friend to InviteesIds List
                    mInvitees.add(mFriends.get(position));
                } else {
                    // Remove the friend from InviteesIds List
                    mInvitees.remove(mFriends.get(position));
                }
            }
        });

        mFriends = Global.getFriends();

        int usersAmount = mFriends.size();
        String[] fullNames = new String[usersAmount];
        int i = 0;
        for (ParseUser user : mFriends) {
            fullNames[i] = user.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                    user.getString(ParseConstants.KEY_LASTNAME);
            i++;
        }
        mFullNames = fullNames;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ChooseInviteesActivity.this,
                android.R.layout.simple_list_item_checked,
                fullNames
        );
        mFriendsList.setAdapter(adapter);

        // Receiving inviteesIds that already were chosen before.
        Intent intent = getIntent();
        mInviteesList.clear();
        int inviteesAmount = intent.getIntExtra(ChooseInviteesActivity.INVITEES_AMOUNT, 0);
        for(i = 0; i < inviteesAmount; i++) {
            mInviteesList.add(intent.getStringExtra(ChooseInviteesActivity.INVITEE + i));
        }

        // Mark previous chosen invitees.
        i = 0;
        for (ParseUser friend : mFriends) {
            for (String invitee : mInviteesList) {
                if (friend.getObjectId().equals(invitee)) {
                    mFriendsList.setItemChecked(i, true);
                    mInvitees.add(mFriends.get(i));
                }
            }
            i++;
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();

        int i = 0;
        for(ParseUser invitee : mInvitees) {
            data.putExtra(INVITEE + i, invitee.getObjectId());
            i++;
        }
        data.putExtra(INVITEES_AMOUNT, i);

        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_invitees, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;

            case android.R.id.home: // Action Bar Back Button
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
