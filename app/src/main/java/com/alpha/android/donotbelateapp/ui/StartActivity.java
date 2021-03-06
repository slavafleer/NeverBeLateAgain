package com.alpha.android.donotbelateapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alpha.android.donotbelateapp.GlobalApplication;
import com.alpha.android.donotbelateapp.OkCustomDialog;
import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StartActivity extends ActionBarActivity {
    private static final String TAG = StartActivity.class.getSimpleName();
    public static String[] mFullNames;
    public static List<ParseObject> mTodaysMeetings;
    protected List<ParseUser> mFriends;
    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;
    @InjectView(R.id.startProgressSpinner)
    ProgressBar mSpinner;
    @InjectView(R.id.startLoadingDataLabel)
    TextView mLoadingLabel;
    private GlobalApplication Global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);

        Global = (GlobalApplication) getApplication();
    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(this, ParseLoginActivity.class);
        // For skipping MainActivity when going back
        // and exiting from the app.
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void navigateToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        // For skipping MainActivity when going back
        // and exiting from the app.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            // If no user logged in, first screen must be Login.
            navigateToLogin();
        } else {
            getUserFriends(); // getTodaysMeetings just in getUserFriends
        }
    }

    private void getUserFriends() {
        Log.v(TAG, "getUserFriends() activated.");
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.addAscendingOrder(ParseConstants.KEY_FIRSTNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    // Success
                    mFriends = friends;
                    Global.setFriends(friends);
                    int usersAmount = mFriends.size();
                    String[] fullNames = new String[usersAmount];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        fullNames[i] = user.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                                user.getString(ParseConstants.KEY_LASTNAME);
                        i++;
                    }
                    mFullNames = fullNames;
                } else {
                    // Show error to user
                    OkCustomDialog dialog = new OkCustomDialog(
                            StartActivity.this,
                            getString(R.string.friend_list_updating_error_title),
                            e.getMessage());
                    dialog.show();
                }

                // TODO: meantime data are retrieving in "synchronous" way
                // need to change it to asynchronous with waiting for finishing
                // of all asynchronous tasks.
                getTodaysMeetings();
            }
        });
    }

    private void getTodaysMeetings() {

        Calendar fromCalendar = Calendar.getInstance();
        Calendar tillCalendar = Calendar.getInstance();

        fromCalendar.add(Calendar.HOUR_OF_DAY, -12);
        tillCalendar.add(Calendar.HOUR_OF_DAY, +12);

        Date fromDate = fromCalendar.getTime();
        Date tillDate = tillCalendar.getTime();

        //TODO: for test
        Log.v(TAG, "Now: " + Calendar.getInstance().getTime());
        Log.v(TAG, "From: " + fromDate.toString());
        Log.v(TAG, "Till: " + tillDate.toString());

        //TODO: put in in separate module class 
        // Requesting for meetings just from initializer and where he was invited in
        // in near future (today).
        // Query for user created meetings.
        ParseQuery<ParseObject> initializerMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        initializerMeetingsQuery.whereEqualTo(ParseConstants.KEY_INITIALIZER, mCurrentUser.getObjectId());
        initializerMeetingsQuery.whereLessThan(ParseConstants.KEY_DATETIME, tillDate);
        initializerMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Query for user was invited meetings.
        ParseQuery<ParseObject> inviteedMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        inviteedMeetingsQuery.whereEqualTo(ParseConstants.KEY_INVITEES, mCurrentUser.getObjectId());
        inviteedMeetingsQuery.whereLessThan(ParseConstants.KEY_DATETIME, tillDate);
        inviteedMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Combined query.
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(initializerMeetingsQuery);
        queries.add(inviteedMeetingsQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending(ParseConstants.KEY_DATETIME);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> meetings, ParseException e) {
                if (e == null) {
                    // Success
                    Global.setTodaysMeetings(meetings);
                } else {
                    // Failed.
                    Log.e(TAG, "Error: ", e);
                }
                // TODO: meantime data are retrieving in "synchronous" way
                // need to change it to asynchronous with waiting for finishing
                // of all asynchronous tasks.
                getFutureMeetings();
            }
        });
    }

    private void getFutureMeetings() {

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.HOUR_OF_DAY, +12);
        Date fromDate = fromCalendar.getTime();

        // Requesting for meetings just from initializer and where he was invited in
        // in near future (today).
        // Query for user created meetings.
        ParseQuery<ParseObject> initializerMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        initializerMeetingsQuery.whereEqualTo(ParseConstants.KEY_INITIALIZER, mCurrentUser.getObjectId());
        initializerMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Query for user was invited meetings.
        ParseQuery<ParseObject> inviteedMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        inviteedMeetingsQuery.whereEqualTo(ParseConstants.KEY_INVITEES, mCurrentUser.getObjectId());
        inviteedMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Combined query.
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(initializerMeetingsQuery);
        queries.add(inviteedMeetingsQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending(ParseConstants.KEY_DATETIME);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> meetings, ParseException e) {
                if (e == null) {
                    // Success
                    Global.setFutureMeetings(meetings);
                } else {
                    // Failed.
                    Log.e(TAG, "Error: ", e);
                }
                // TODO: meantime data are retrieving in "synchronous" way
                // need to change it to asynchronous with waiting for finishing
                // of all asynchronous tasks.
                navigateToMain();
            }
        });
    }
}
