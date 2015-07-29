package com.alpha.android.donotbelateapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Slava on 22/07/2015.
 */
public class UtilStrings {

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

    // returns time till wanted dateTime.
    public static String timeLeft(Date dateTime) {
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();
        long timeLeft = dateTime.getTime() - currentDate.getTime();

        return UtilStrings.getTime(timeLeft);
    }

    public static String getDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(date);

        return dateString;
    }

    public static String getTime(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeString = timeFormat.format(time);

        return timeString;
    }


}
