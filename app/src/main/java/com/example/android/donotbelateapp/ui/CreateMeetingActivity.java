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
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.donotbelateapp.GlobalApplication;
import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.Meeting;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.example.android.donotbelateapp.utils.UtilsUi;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
    @InjectView(R.id.createMeetingInviteesLinearLayout) GridLayout mInviteesLinearLayout;

    private Calendar mDateTime = Calendar.getInstance();
    private ArrayList<String> mInviteesList = new ArrayList<>();
    private List<ParseUser> mFriends;
    private Meeting mMeeting;
    private ParseRelation<ParseUser> mInviteesRelation;
    private GlobalApplication Global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        ButterKnife.inject(this);
        Global = (GlobalApplication) getApplication();

        mFriends = Global.getFriends();

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
            mMeeting.addGoing(ParseUser.getCurrentUser().getObjectId());
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

        String date = mDate.getText().toString();
        if(! date.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                currentDate.setTime(format.parse(date));
                year = currentDate.get(Calendar.YEAR);
                month = currentDate.get(Calendar.MONTH);
                day = currentDate.get(Calendar.DAY_OF_MONTH);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(
                CreateMeetingActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                int month = selectedMonth + 1; // Jan = 0
                NumberFormat numberFormat = new DecimalFormat("00");
                mDate.setText(numberFormat.format(selectedDay) + "/" +
                        numberFormat.format(month) + "/" + selectedYear);

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
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        String time = mTime.getText().toString();
        if(! time.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            try {
                currentTime.setTime(format.parse(time));
                hour = currentTime.get(Calendar.HOUR_OF_DAY);
                minute = currentTime.get(Calendar.MINUTE);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(
                CreateMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                NumberFormat numberFormat = new DecimalFormat("00");
                mTime.setText(numberFormat.format(selectedHour) + ":" +
                        numberFormat.format(selectedMinute));

                mDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                mDateTime.set(Calendar.MINUTE, selectedMinute);
            }
        },hour,minute, true); // 24h format
        timePicker.setTitle("Choose Time");
        timePicker.show();
    }

    @OnClick(R.id.createMeetingInvitees)
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

            // Displaying invitees in Invitees Field
            String inviteesListToString = "";
            for(String invitee : mInviteesList) {
                for(ParseUser friend : mFriends) {
                    if(friend.getObjectId().equals(invitee)) {
                        // For TextView.
                        inviteesListToString +=
                                " <" + friend.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                                friend.getString(ParseConstants.KEY_LASTNAME) + "> ";

                        //TODO: change to horizontal and vertical layouts and play with weight
                        // For GridLayout.
                        String fullName = friend.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                                friend.getString(ParseConstants.KEY_LASTNAME);
                        mInviteesLinearLayout.addView(UtilsUi.createTextButton(this, fullName));

                        mInviteesRelation.add(friend);
                    }
                }
            }
            mInvitees.setText(inviteesListToString);
        }
    }
}
