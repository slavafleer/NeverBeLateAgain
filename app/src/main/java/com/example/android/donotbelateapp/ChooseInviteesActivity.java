package com.example.android.donotbelateapp;

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
import android.widget.ProgressBar;

import com.example.android.donotbelateapp.modul.Invitees;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ChooseInviteesActivity extends ActionBarActivity {

    private List<ParseUser> mFriends;
    private ArrayList<ParseUser> mInvitees = new ArrayList<>();
    private ParseUser mCurrentUser;
    private ParseRelation<ParseUser> mFriendsRelation;
    private String[] mFullNames;
    private Invitees mInviteesParcelable;

    @InjectView(R.id.chooseInviteesSpinner) ProgressBar mSpinner;
    @InjectView(R.id.chooseInviteesList) ListView mFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_invitees);
        ButterKnife.inject(this);

        mSpinner.setVisibility(View.INVISIBLE);

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

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.addAscendingOrder(ParseConstants.KEY_FIRSTNAME);
        mSpinner.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                mSpinner.setVisibility(View.INVISIBLE);
                if (e == null) {
                    // Success
                    mFriends = friends;
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
                } else {
                    // Show error to user
                    OkCustomDialog dialog = new OkCustomDialog(
                            ChooseInviteesActivity.this,
                            getString(R.string.friend_list_updating_error_title),
                            e.getMessage());
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        ArrayList<String> inviteesId = new ArrayList<>();

        for(ParseUser invity : mInvitees) {
            inviteesId.add(invity.getObjectId());
        }
        data.putExtra("string-array", inviteesId);
//        data.putStringArrayListExtra("invitees", inviteesId);
        data.putExtra("key", 777);

        mInviteesParcelable.addToInviteesList("Invitees test ID");
        data.putExtra("parcelable", mInviteesParcelable);

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
