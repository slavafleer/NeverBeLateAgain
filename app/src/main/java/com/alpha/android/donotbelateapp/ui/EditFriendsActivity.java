package com.alpha.android.donotbelateapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alpha.android.donotbelateapp.OkCustomDialog;
import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditFriendsActivity extends ActionBarActivity {

    private final static String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;

    @InjectView(R.id.editFriendsList) ListView mFriendsList;
    @InjectView(R.id.editFriendsSpinner) ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        mSpinner.setVisibility(View.INVISIBLE);

        mFriendsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mFriendsList.isItemChecked(position)) {
                    // Add friend
                    mFriendsRelation.add(mUsers.get(position));
                } else {
                    // Remove friend
                    mFriendsRelation.remove(mUsers.get(position));
                }
                // Updating in Parse
                mCurrentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Success
//                            Toast.makeText(EditFriendsActivity.this,
//                                    getString(R.string.friend_list_updated_toast),
//                                    Toast.LENGTH_LONG).show();
                        } else {
                            // Show error to user
                            OkCustomDialog dialog = new OkCustomDialog(
                                    EditFriendsActivity.this,
                                    getString(R.string.friend_list_updating_error_title),
                                    e.getMessage());
                            dialog.show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        mSpinner.setVisibility(View.VISIBLE);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.addAscendingOrder(ParseConstants.KEY_FIRSTNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // Success
                    mUsers = users;

                    // Remove current user from mUsers, for not self showing.
                    Iterator<ParseUser> iterator = mUsers.iterator();
                    while (iterator.hasNext()) {
                        String userId = iterator.next().getObjectId();
                        if (mCurrentUser.getObjectId().equals(userId)) {
                            iterator.remove();
                        }
                    }

                    int usersAmount = mUsers.size();
                    String[] fullNames = new String[usersAmount];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        fullNames[i] = user.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                                user.getString(ParseConstants.KEY_LASTNAME);
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            fullNames
                    );
                    mFriendsList.setAdapter(adapter);

                    addFriendCheckMarks();

                } else {
                    // Show an error to user
                    OkCustomDialog dialog = new OkCustomDialog(
                            EditFriendsActivity.this,
                            getString(R.string.loading_data_error_title),
                            e.getMessage());
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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

    // Marking checked friends after the list has loaded.
    private void addFriendCheckMarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e == null) {
                    // Success - look for the match.
                    for(int i = 0; i < mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);

                        for(ParseUser friend : friends) {
                            if(friend.getObjectId().equals(user.getObjectId())) {
                                mFriendsList.setItemChecked(i, true);
                            }
                        }
                    }
                    mSpinner.setVisibility(View.INVISIBLE);
                } else {
                    mSpinner.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "Error: ", e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateToStart();

        super.onBackPressed();
    }

    private void navigateToStart() {
        Intent mainIntent = new Intent(this, StartActivity.class);
        // For skipping MainActivity when going back
        // and exiting from the app.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}
