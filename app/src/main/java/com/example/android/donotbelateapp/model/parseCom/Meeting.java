package com.example.android.donotbelateapp.model.parseCom;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Slava on 02/07/2015.
 */

@ParseClassName(ParseConstants.KEY_FRIENDS_RELATION) // Due to Parse.com
public class Meeting extends ParseObject {

    public Meeting() {
        super();
    }

    public void setSubject(String subject) {
        put(ParseConstants.KEY_SUBJECT, subject);
    }

    public String getSubject() {
        return getString(ParseConstants.KEY_SUBJECT);
    }

    public void setDetails(String details) {
        put(ParseConstants.KEY_DETAILS, details);
    }

    public String getDetailes() {
        return getString(ParseConstants.KEY_DETAILS);
    }

    public void setDateTime(Date dateTime) {
        put(ParseConstants.KEY_DATETIME, dateTime);
    }

    public Date getDateTime() {
        return getDate(ParseConstants.KEY_DATETIME);
    }

    public void setLocation(String location) {
        put(ParseConstants.KEY_LOCATION, location);
    }

    public String getLocation() {
        return getString(ParseConstants.KEY_LOCATION);
    }

    public void setInitializer() {
        put(ParseConstants.KEY_INITIALIZER, ParseUser.getCurrentUser().getObjectId());
    }

    public void setInitializer(String initializer) {
        put(ParseConstants.KEY_INITIALIZER, initializer);
    }

    public String getInitializer() {
        return getString(ParseConstants.KEY_INITIALIZER);
    }
}
