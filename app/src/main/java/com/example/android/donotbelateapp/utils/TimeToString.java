package com.example.android.donotbelateapp.utils;

/**
 * Created by Slava on 22/07/2015.
 */
public class TimeToString {

    public static String getTime(long timeLeft) {
        // Transforming long number (in milliseconds) to string of time in d h m,
        // where d for days, h for hours and m for min.
        // if one of a parameters is 0, it should not be displayed.

        String timeLeftString = "";
        long secondsLeft = timeLeft / 1000;
        int minutesLeft = (int) (secondsLeft / 60);
        if (minutesLeft < 60) {
            timeLeftString = minutesLeft + "m";
        } else {
            int hoursLeft = minutesLeft / 60;
            int minutes = minutesLeft % 60;
            if (hoursLeft < 24) {
                if (minutes == 0) {
                    timeLeftString = hoursLeft + "h";
                } else {
                    timeLeftString = hoursLeft + "h " + minutes + "m";
                }
            } else {
                int daysLeft = hoursLeft / 24;
                int hours = hoursLeft % 24;
                if (minutes == 0) {
                    if (hours == 0) {
                        timeLeftString = daysLeft + "d";
                    } else {
                        timeLeftString = daysLeft + "d " + hoursLeft % 24 + "h";
                    }
                } else {
                    if (hours == 0) {
                        timeLeftString = daysLeft + "d " + minutesLeft % 60 + "m";
                    } else {
                        timeLeftString = daysLeft + "d " + hoursLeft % 24 + "h " + minutesLeft % 60 + "m";
                    }
                }
            }
        }

        return timeLeftString;
    }
}
