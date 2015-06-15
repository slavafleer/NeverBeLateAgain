package com.example.android.donotbelateapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMeetingActivity extends ActionBarActivity {

    @InjectView(R.id.createMeetingSubject) EditText mSubject;
    @InjectView(R.id.createMeetingsDetails) EditText mDetailes;
    @InjectView(R.id.createMeetingDate) TextView mDate;
    @InjectView(R.id.createMeetingTime) TextView mTime;
    @InjectView(R.id.createMeetingLocation) EditText mLocation;
    @InjectView(R.id.createMeetingInvitees) TextView mInvitees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick (R.id.createMeetingCreateButton)
    void onClickCreateMeetingButton(){
        String subject = mSubject.getText().toString();
        String details = mDetailes.getText().toString();
        Date date = null;
        Date time = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            date = dateFormat.parse(mDate.getText().toString());
            time = timeFormat.parse(mTime.getText().toString());
        } catch (java.text.ParseException e) {
            OkCustomDialog dialog = new OkCustomDialog(
                    CreateMeetingActivity.this,
                    getString(R.string.meeting_creating_error_title),
                    e.getMessage());
            dialog.show();
        }
        String location = mLocation.getText().toString();
        String notification = null;

        if(subject.isEmpty()) {
            notification = "Subject field is empty.";
//        } else if(date == null) {
//            notification = "Date field is empty.";
//        } else if(time == null) {
//            notification = "Time field is empty.";
        } else if(location.isEmpty()) {
            notification = "Location field is empty.";
        }

        if( ! notification.isEmpty()) {
            OkCustomDialog dialog = new OkCustomDialog(
                    CreateMeetingActivity.this,
                    getString(R.string.meeting_creating_error_title),
                    notification);
            dialog.show();
        } else {
            ParseObject meeting = new ParseObject(ParseConstants.CLASS_MEETINGS);
            meeting.put(ParseConstants.KEY_SUBJECT, subject);
            meeting.put(ParseConstants.KEY_DETAILS, details);
            meeting.put(ParseConstants.KEY_DATE, date);
            meeting.put(ParseConstants.KEY_TIME, time);
            meeting.put(ParseConstants.KEY_LOCATION, location);
            meeting.put(ParseConstants.KEY_INITIALIZER, ParseUser.getCurrentUser());
            meeting.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(CreateMeetingActivity.this,
                                getString(R.string.new_meeting_created_toast),
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Show error to user
                        OkCustomDialog dialog = new OkCustomDialog(
                                CreateMeetingActivity.this,
                                getString(R.string.meeting_creating_error_title),
                                e.getMessage());
                        dialog.show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.createMeetingDate)
    void onClickDate() {
        // To show current date in the date picker
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(CreateMeetingActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                selectedMonth++; // Jan = 0
                mDate.setText("" + selectedDay + "/" + selectedMonth + "/" + selectedYear);
            }
        }, year, month, day);
        datePicker.setTitle("Choose Date");
        datePicker.show();
    }

    @OnClick(R.id.createMeetingTime)
    void onClickTime() {
        // To show current time in the time picker
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(CreateMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mTime.setText(selectedHour + ":" + selectedMinute);
            }
        },hour,minute, true); // 24h format
        timePicker.setTitle("Choose Time");
        timePicker.show();
    }
}
