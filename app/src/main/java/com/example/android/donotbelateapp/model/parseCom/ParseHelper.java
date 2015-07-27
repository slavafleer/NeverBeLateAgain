package com.example.android.donotbelateapp.model.parseCom;

import android.content.Context;

import com.example.android.donotbelateapp.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Slava on 22/07/2015.
 */
public class ParseHelper {

    // Returning String of User Status for arriving to the meeting.
    public static String getUserStatus(Context context, ParseObject meeting) {
        List<String> list = meeting.getList(ParseConstants.KEY_GOING);
        for(String status : list) {
            if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                return context.getString(R.string.user_status_going);
            }
        }

        list = meeting.getList(ParseConstants.KEY_NOT_GOING);
        for(String status : list) {
            if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                return context.getString(R.string.user_status_not_going);
            }
        }

        list = meeting.getList(ParseConstants.KEY_NOT_GOING);
        for(String status : list) {
            if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                return context.getString(R.string.user_status_maybe);
            }
        }

        return context.getString(R.string.user_status_invited);
    }

}
