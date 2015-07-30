package com.alpha.android.donotbelateapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ResetPasswordActivity extends ActionBarActivity {

    @InjectView(R.id.resetPasswordEmail) EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.resetPasswordSubmitButton)
    public void onClickResetButton() {
        String email = mEmail.getText().toString();
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // An email was successfully sent with reset instructions.
                    Toast.makeText(ResetPasswordActivity.this,
                            getString(R.string.reset_instruction_toast),
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                    // Show error to user
                    OkCustomDialog dialog = new OkCustomDialog(
                            ResetPasswordActivity.this,
                            getString(R.string.reset_password_error_title),
                            e.getMessage());
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reset_password, menu);
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
}
