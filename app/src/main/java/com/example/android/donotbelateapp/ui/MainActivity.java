package com.example.android.donotbelateapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.ui.fragments.FriendsFragment;
import com.example.android.donotbelateapp.ui.fragments.FutureMeetingsFragment;
import com.example.android.donotbelateapp.ui.fragments.TodaysMeetingsFragment;
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
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;
    public static String[] mFullNames;
    public static List<ParseObject> mTodaysMeetings;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            // If no user logged in, first screen must be Login.
            navigateToLogin();
        }



        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    // The getting of users List from Parse, done from FriendsFragment Parent (MainActivity)
    // for preventing crashing of the app due to chance of changing fragment before data was received.
    @Override
    protected void onResume() {
        super.onResume();

        getUserFriends();

        getTodaysMeetings();
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
                            MainActivity.this,
                            getString(R.string.friend_list_updating_error_title),
                            e.getMessage());
                    dialog.show();
                }
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
        Log.v(TAG,"From: " + fromDate.toString());
        Log.v(TAG, "Till: " + tillDate.toString());

        // Requesting for meetings just from initializer and where he was invited in
        // in near future (today).
        // Query for user created meetings.
        ParseQuery<ParseObject> initializerMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        initializerMeetingsQuery.whereEqualTo(ParseConstants.KEY_INITIALIZER, mCurrentUser.getObjectId());
        initializerMeetingsQuery.whereLessThan(ParseConstants.KEY_DATETIME, tillDate);
        initializerMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Query for user was invited meetings.
        ParseQuery<ParseObject> inviteedMeetingsQuery = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        inviteedMeetingsQuery.whereEqualTo(ParseConstants.KEY_INVITEES ,mCurrentUser.getObjectId());
        initializerMeetingsQuery.whereLessThan(ParseConstants.KEY_DATETIME, tillDate);
        initializerMeetingsQuery.whereGreaterThan(ParseConstants.KEY_DATETIME, fromDate);

        // Combined query.
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(initializerMeetingsQuery);
        queries.add(inviteedMeetingsQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> meetings, ParseException e) {
                if(e == null) {
                    // Success
                    mTodaysMeetings = meetings;
                } else {
                    // Failed.
                    Log.e(TAG, "Error: ", e);
                }
                int pause = 1;
            }
        });
    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(this, ParseLoginActivity.class);
        // For skipping MainActivity when going back
        // and exiting from the app.
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_create_meeting:
                Intent createMeetingIntent = new Intent(MainActivity.this, CreateMeetingActivity.class);
                startActivity(createMeetingIntent);
                break;

            case R.id.action_edit_fiends:
                Intent intent = new Intent(MainActivity.this, EditFriendsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_settings :
                break;

            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch(position) {
                case 0:
                    return new TodaysMeetingsFragment();
                case 1:
                    return new FutureMeetingsFragment();
                case 2:
                    return new FriendsFragment();
            }

            // For fixing no return error
            return new FriendsFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_todays_meetings).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_future_meetings).toUpperCase(l);
                case 2:
                    return getString(R.string.title_fragment_friends).toUpperCase(l);
            }
            return null;
        }
    }
}
