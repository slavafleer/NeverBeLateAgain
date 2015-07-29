package com.alpha.android.donotbelateapp.model.parseCom;

import android.content.Context;

import com.alpha.android.donotbelateapp.OkCustomDialog;
import com.alpha.android.donotbelateapp.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Slava on 22/07/2015.
 */
public class ParseHelper {

    // Returning String of User Status for arriving to the meeting.
    public static String getUserStatus(Context context, ParseObject meeting) {
        List<String> list = meeting.getList(ParseConstants.KEY_GOING);
        if (list != null) {
            for(String status : list) {
                if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                    return context.getString(R.string.user_status_going);
                }
            }
        }

        list = meeting.getList(ParseConstants.KEY_NOT_GOING);
        if (list != null) {
            for(String status : list) {
                if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                    return context.getString(R.string.user_status_not_going);
                }
            }
        }

        list = meeting.getList(ParseConstants.KEY_MAYBE);
        if (list != null) {
            for(String status : list) {
                if(status.equals(ParseUser.getCurrentUser().getObjectId())) {
                    return context.getString(R.string.user_status_maybe);
                }
            }
        }

        return context.getString(R.string.user_status_invited);
    }

    // Requesting meeting ParseObject from Parse.com by ObjectId.
    public static Meeting getMeeting(Context context, String meetingId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_MEETINGS);
        query.whereEqualTo(ParseConstants.KEY_OBJECT_ID, meetingId);
        try {
            return (Meeting)query.getFirst();
        } catch (ParseException e) {
            // Show the error.
            OkCustomDialog dialog = new OkCustomDialog(
                    context,
                    context.getString(R.string.loading_data_error_title),
                    e.getMessage()
            );
            dialog.show();
            return null;
        }
    }

    //TODO: create getInvitees()
}
