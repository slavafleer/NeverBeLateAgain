package com.example.android.donotbelateapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Slava on 02/06/2015.
 */

    // Learning:
    // Till I didn't create this class, Parse.enableLocalDatastore(this) was
    // crushing the app while changing device orientation with IllegalStateException.
    // Also while creating this type class, need to add
    // android:name="com.example.android.donotbelateapp.ParseApplication"
    // in manifest.

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "3J5hJWC6D79bZKbs8liukXR7kfsrojEKrmkrpTEW",
                "wbL5uF2kvCOzNYyZbeHZhUgbahpbw4z9R0d25Bgu");
    }
}
