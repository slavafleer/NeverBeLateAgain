package com.example.android.donotbelateapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.Meeting;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.example.android.donotbelateapp.model.parseCom.ParseHelper;
import com.example.android.donotbelateapp.utils.UtilStrings;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MeetingActivity extends ActionBarActivity {

    @InjectView(R.id.meetingSubject) TextView mSubject;
    @InjectView(R.id.meeting_Details) TextView mDetails;
    @InjectView(R.id.meetingDate) TextView mDate;
    @InjectView(R.id.meetingTime) TextView mTime;
    @InjectView(R.id.meetingRemain) TextView mRemain;
    @InjectView(R.id.meetingLocation) TextView mLocation;
    @InjectView(R.id.meetingGoing) TextView mGoing;
    @InjectView(R.id.meetingUserStatus) TextView mStatus;

    private String mUserStatus;
    private Meeting mMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String meetingId = intent.getStringExtra(ParseConstants.KEY_MEETING_ID);
        mMeeting = ParseHelper.getMeeting(this, meetingId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMeeting != null) {
            mSubject.setText(mMeeting.getString(ParseConstants.KEY_SUBJECT));
            mDetails.setText(mMeeting.getString(ParseConstants.KEY_DETAILS));
            Date date = mMeeting.getDate(ParseConstants.KEY_DATETIME);
            mDate.setText(UtilStrings.getDate(date));
            mTime.setText(UtilStrings.getTime(date));
            mRemain.setText(UtilStrings.timeLeft(date));
            mLocation.setText(mMeeting.getString(ParseConstants.KEY_LOCATION));
            //TODO: need to display Full Names of invitees/going in this field.
//            mGoing.setText(mMeeting.getString(ParseConstants.KEY_GOING));
            mStatus.setText(ParseHelper.getUserStatus(this, mMeeting));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
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

    // TODO: need to give option to change the status and send it to Parse.com
    @OnClick(R.id.meetingUserStatus)
    public void onClickUserStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Choose your arriving status")
                .setItems(R.array.meeting_user_status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentUserId = ParseUser.getCurrentUser().getObjectId();
                        switch (which) {
                            case 0:
                                mMeeting.addGoing(currentUserId);
                                mMeeting.removeNotGoing(
                                        mMeeting.<String>getList(ParseConstants.KEY_NOT_GOING),
                                        currentUserId
                                );
                                mMeeting.removeMaybe(
                                        mMeeting.<String>getList(ParseConstants.KEY_MAYBE),
                                        currentUserId
                                );
                                mStatus.setText(getString(R.string.user_status_going));
                                break;
                            case 1:
                                mMeeting.addNotGoing(currentUserId);
                                mMeeting.removeGoing(
                                        mMeeting.<String>getList(ParseConstants.KEY_GOING),
                                        currentUserId
                                );
                                mMeeting.removeMaybe(
                                        mMeeting.<String>getList(ParseConstants.KEY_MAYBE),
                                        currentUserId
                                );
                                mStatus.setText(getString(R.string.user_status_not_going));
                                break;
                            case 2:
                                mMeeting.addMaybe(currentUserId);
                                mMeeting.removeGoing(
                                        mMeeting.<String>getList(ParseConstants.KEY_GOING),
                                        currentUserId
                                );
                                mMeeting.removeNotGoing(
                                        mMeeting.<String>getList(ParseConstants.KEY_NOT_GOING),
                                        currentUserId
                                );
                                mStatus.setText(getString(R.string.user_status_maybe));
                                break;
                        }
                        try {
                            mMeeting.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
