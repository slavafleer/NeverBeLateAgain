package com.example.android.donotbelateapp.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditFriendsActivity extends ListActivity {

    protected List<ParseUser> mUsers;
    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;

    @InjectView(R.id.editFriendsSpinner) ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        mSpinner.setVisibility(View.INVISIBLE);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        mSpinner.setVisibility(View.VISIBLE);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                mSpinner.setVisibility(View.INVISIBLE);
                if (e == null) {
                    // Success
                    mUsers = users;
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
                    setListAdapter(adapter);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getListView().isItemChecked(position)) {
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
                    Toast.makeText(EditFriendsActivity.this,
                            getString(R.string.friend_list_updated_toast),
                            Toast.LENGTH_LONG).show();
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
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
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
}
