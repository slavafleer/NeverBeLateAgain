package com.example.android.donotbelateapp.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.donotbelateapp.ChooseInviteesActivity;
import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMeetingActivity extends ActionBarActivity {
    private static final String TAG = CreateMeetingActivity.class.getSimpleName();
    public static final int REQUESTCODE_CHOOSEINVITEES = 1;

    @InjectView(R.id.createMeetingSubject) EditText mSubject;
    @InjectView(R.id.createMeetingsDetails) EditText mDetailes;
    @InjectView(R.id.createMeetingDate) TextView mDate;
    @InjectView(R.id.createMeetingTime) TextView mTime;
    @InjectView(R.id.createMeetingLocation) EditText mLocation;
    @InjectView(R.id.createMeetingInvitees) TextView mInvitees;

    Calendar mDateTime = Calendar.getInstance();

    private ArrayList<String> mInviteesList = new ArrayList<>();

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
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();
        String location = mLocation.getText().toString();
        String notification = "";

        if(subject.isEmpty()) {
            notification = "Subject field is empty.";
        } else if(date.isEmpty()) {
            notification = "Date field is empty.";
        } else if(time.isEmpty()) {
            notification = "Time field is empty.";
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
            meeting.put(ParseConstants.KEY_DATETIME, mDateTime.getTime());
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
                mDate.setText(selectedDay + "/" + selectedMonth+1 + "/" + selectedYear); // Jan = 0

                mDateTime.set(Calendar.YEAR, selectedYear);
                mDateTime.set(Calendar.MONTH, selectedMonth);
                mDateTime.set(Calendar.DAY_OF_MONTH, selectedDay);
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
        timePicker = new TimePickerDialog(
                CreateMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mTime.setText(selectedHour + ":" + selectedMinute);

                mDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                mDateTime.set(Calendar.MINUTE, selectedMinute);
            }
        },hour,minute, true); // 24h format
        timePicker.setTitle("Choose Time");
        timePicker.show();
    }

    @OnClick(R.id.createMeetingInviteesButton)
    void onClickInviteesButton() {
        Intent inviteesIntent = new Intent(CreateMeetingActivity.this, ChooseInviteesActivity.class);

        // Returning mInviteesList back for editing.
        int i = 0;
        for(String invitee:mInviteesList) {
            inviteesIntent.putExtra(ChooseInviteesActivity.INVITEE + i, invitee);
            i++;
        }
        inviteesIntent.putExtra(ChooseInviteesActivity.INVITEES_AMOUNT, i);

        startActivityForResult(inviteesIntent, REQUESTCODE_CHOOSEINVITEES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE_CHOOSEINVITEES && resultCode == Activity.RESULT_OK){
            mInviteesList.clear();
            int inviteesAmount = data.getIntExtra(ChooseInviteesActivity.INVITEES_AMOUNT, 0);
            for(int i = 0; i < inviteesAmount; i++) {
                mInviteesList.add(data.getStringExtra(ChooseInviteesActivity.INVITEE + i));
            }

            // Displaying invitees(for now just IDs) in Invitees Field
            String inviteesListToString = "";
            for(String invitee : mInviteesList) {
                inviteesListToString += " <" + invitee + "> ";
            }
            mInvitees.setText(inviteesListToString);
        }
    }
}
