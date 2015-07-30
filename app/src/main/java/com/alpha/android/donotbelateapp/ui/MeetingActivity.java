package com.alpha.android.donotbelateapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alpha.android.donotbelateapp.R;
import com.alpha.android.donotbelateapp.model.parseCom.Meeting;
import com.alpha.android.donotbelateapp.model.parseCom.ParseConstants;
import com.alpha.android.donotbelateapp.model.parseCom.ParseHelper;
import com.alpha.android.donotbelateapp.utils.UtilStrings;
import com.alpha.android.donotbelateapp.utils.UtilsUi;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

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
    @InjectView(R.id.meetingInviteesLeft) LinearLayout mLeftInvitees;
    @InjectView(R.id.meetingInviteesMiddle) LinearLayout mMiddleInvitees;
    @InjectView(R.id.meetingInviteesRight) LinearLayout mRightInvitees;
    @InjectView(R.id.meetingUserStatus) TextView mStatus;

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
            updateInvitees(mMeeting);
            mStatus.setText(ParseHelper.getUserStatus(this, mMeeting));
        }
    }

    private void updateInvitees(Meeting meeting) {
        List<ParseUser> invitees = ParseHelper.getInvitees(meeting);

        // Displaying invitees in Invitees Field
        int i = 0;
        mLeftInvitees.removeAllViews();
        mMiddleInvitees.removeAllViews();
        mRightInvitees.removeAllViews();
        for(ParseUser invitee : invitees) {
                String fullName = invitee.getString(ParseConstants.KEY_FIRSTNAME) + " " +
                        invitee.getString(ParseConstants.KEY_LASTNAME);
                if(i % 3 == 0) {
                    mLeftInvitees.addView(UtilsUi.createTextButton(this, fullName));
                } else if(i % 3 == 1) {
                    mMiddleInvitees.addView(UtilsUi.createTextButton(this, fullName));
                } else {
                    mRightInvitees.addView(UtilsUi.createTextButton(this, fullName));
                }
                i++;
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

        switch (id) {
            case R.id.action_settings:
                return true;

            case android.R.id.home: // Action Bar Back Button
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Brings dialog for changing User Status for the meeting.
    @OnClick(R.id.meetingUserStatus)
    public void onClickUserStatus() {
        // If we need to apply style/them to AlertDialog.
//                 AlertDialog.Builder builder = new AlertDialog.Builder(
//        new ContextThemeWrapper(this, R.style.niceBackground)
//        )
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}


