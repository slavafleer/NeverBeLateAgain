package com.example.android.donotbelateapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MeetingActivity extends ActionBarActivity {

    @InjectView(R.id.meetingSubject)
    TextView mSubject;

    private String mUserStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        mSubject.setText(intent.getStringExtra(ParseConstants.KEY_SUBJECT));
        mUserStatus = intent.getStringExtra(ParseConstants.KEY_USER_STATUS);
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

    @OnClick(R.id.meetingGoingButton)
    public void onClickGoingButton() {
        if(mUserStatus.equals(getString(R.string.user_status_invited))) {
            // TODO: bring here meeting from cloud, change it and send back.
            // IF so, no need to bring extras from previous activities. bring all in start from cloud
        }
    }
}
