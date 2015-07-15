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

import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.Meeting;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private Calendar mDateTime = Calendar.getInstance();
    private ArrayList<String> mInviteesList = new ArrayList<>();
    private ParseUser mCurrentUser;
    private ParseRelation<ParseUser> mFriendsRelation;
    private List<ParseUser> mFriends;
    private Meeting mMeeting;
    ParseRelation<ParseUser> mInviteesRelation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        ButterKnife.inject(this);

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.addAscendingOrder(ParseConstants.KEY_FIRSTNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    // Success
                    mFriends = friends;
                }
            }
        });

        mMeeting = new Meeting();
        mInviteesRelation = mMeeting.getRelation(ParseConstants.KEY_INVITEES);
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
            notification = getString(R.string.create_meeting_subject_empty);
        } else if(date.isEmpty()) {
            notification = getString(R.string.create_meeting_date_empty);
        } else if(time.isEmpty()) {
            notification = getString(R.string.create_meeting_time_empty);
        } else if(location.isEmpty()) {
            notification = getString(R.string.create_meeting_location_empty);
        } else if(Calendar.getInstance().compareTo(mDateTime) > 0) {
            // Past dates not allowed for meeting creation.
            notification = getString(R.string.create_meeting_date_time_wrong);
        }

        if( ! notification.isEmpty()) {
            OkCustomDialog dialog = new OkCustomDialog(
                    CreateMeetingActivity.this,
                    getString(R.string.meeting_creating_error_title),
                    notification);
            dialog.show();
        } else {
            mMeeting.setSubject(subject);
            mMeeting.setDetails(details);
            mMeeting.setDateTime(mDateTime.getTime());
            mMeeting.setLocation(location);
            mMeeting.setInitializer();
            mMeeting.saveInBackground(new SaveCallback() {
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
                        Toast.makeText(CreateMeetingActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    navigateToStart();
                }
            });
        }
    }

    private void navigateToStart() {
        Intent mainIntent = new Intent(this, StartActivity.class);
        // For skipping MainActivity when going back
        // and exiting from the app.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
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
                int month = selectedMonth + 1;
                mDate.setText(selectedDay + "/" + month + "/" + selectedYear); // Jan = 0

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
        int hour = currentTime.get(Calendar.HOUR) + 12; // Done due to not recognizing pm hour
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
//                inviteesListToString += " <" + invitee + "> ";
                for(ParseUser friend : mFriends) {
                    if(friend.getObjectId().equals(invitee)) {
                        inviteesListToString +=
                                " < " + friend.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                                friend.getString(ParseConstants.KEY_LASTNAME) + " > ";
                        mInviteesRelation.add(friend);
                    }
                }
            }
            mInvitees.setText(inviteesListToString);
        }
    }
}
