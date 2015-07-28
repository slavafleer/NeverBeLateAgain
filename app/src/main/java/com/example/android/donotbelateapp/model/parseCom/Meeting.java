package com.example.android.donotbelateapp.model.parseCom;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Slava on 02/07/2015.
 */


@ParseClassName(ParseConstants.CLASS_MEETINGS) // Due to Parse.com class declaration
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

    // Add userId to Going List.
    public void addGoing(String userId) {
        addUnique(ParseConstants.KEY_GOING, userId);
    }

    // Remove userId from Going List.
    public List<String> removeGoing(List<String> list, String userId) {
        if (list != null) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (userId.equals(item)) {
                    iterator.remove();
                }
            }
        }

        return list;
    }

    // Add userId to Not Going List.
    public void addNotGoing(String userId) {
        addUnique(ParseConstants.KEY_NOT_GOING, userId);
    }

    // Remove userId from Not Going List.
    public List<String> removeNotGoing(List<String> list, String userId) {
        if (list != null) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (userId.equals(item)) {
                    iterator.remove();
                }
            }
        }

        return list;
    }

    // Add userId to Maybe List.
    public void addMaybe(String userId) {
        addUnique(ParseConstants.KEY_MAYBE, userId);
    }

    // Remove userId from Not Going List.
    public List<String> removeMaybe(List<String> list, String userId) {
        if (list != null) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (userId.equals(item)) {
                    iterator.remove();
                }
            }
        }

        return list;
    }
}
