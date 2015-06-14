package com.example.android.donotbelateapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMeetingActivity extends ActionBarActivity {

    @InjectView(R.id.createMeetingSubject) EditText mSubject;
    @InjectView(R.id.createMeetingsDetails) EditText mDetailes;
    @InjectView(R.id.createMeetingDate) EditText mDate;
    @InjectView(R.id.createMeetingTime) EditText mTime;
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
}