package com.example.android.donotbelateapp;

import android.app.Application;

import com.example.android.donotbelateapp.model.parseCom.Meeting;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Slava on 02/06/2015.
 */

    // Learning:
    // Till I didn't create this class, Parse.enableLocalDatastore(this) was
    // crushing the app while changing device orientation with IllegalStateException.
    // Also while creating this type class, need to add
    // android:name="com.example.android.donotbelateapp.GlobalApplication"
    // in manifest.

public class GlobalApplication extends Application {

    // Global variables.
    private List<ParseObject> mTodaysMeetings;
    private List<ParseObject> mFutureMeetings;
    private List<ParseUser> mFriends;

    public List<ParseUser> getFriends() {
        return mFriends;
    }

    public void setFriends(List<ParseUser> friends) {
        mFriends = friends;
    }

    public List<ParseObject> getFutureMeetings() {
        return mFutureMeetings;
    }

    public void setFutureMeetings(List<ParseObject> futureMeetings) {
        mFutureMeetings = futureMeetings;
    }

    public List<ParseObject> getTodaysMeetings() {
        return mTodaysMeetings;
    }

    public void setTodaysMeetings(List<ParseObject> todaysMeetings) {
        mTodaysMeetings = todaysMeetings;
    }

    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Meeting.class);

        Parse.initialize(this, ParseConstants.APPLICATION_ID,
                ParseConstants.CLIENT_KEY);
    }
}
