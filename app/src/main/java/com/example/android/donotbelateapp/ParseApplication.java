package com.example.android.donotbelateapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Slava on 02/06/2015.
 */
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
