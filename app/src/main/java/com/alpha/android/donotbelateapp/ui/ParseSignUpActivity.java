package com.alpha.android.donotbelateapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alpha.android.donotbelateapp.OkCustomDialog;
import com.alpha.android.donotbelateapp.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ParseSignUpActivity extends ActionBarActivity {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    @InjectView(R.id.signup_email) EditText mEmail;
    @InjectView(R.id.signup_first_name) EditText mFirstName;
    @InjectView(R.id.signup_last_name) EditText mLastName;
    @InjectView(R.id.signup_password) EditText mPassword;
    @InjectView(R.id.signup_confirm_password) EditText mConfirmPassword;
    @InjectView(R.id.signup_spinner) ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_sign_up);
        ButterKnife.inject(this);

        mSpinner.setVisibility(View.INVISIBLE);
    }

    @OnClick (R.id.signup_button)
    public void onClickSignupButton() {
        String email = mEmail.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

        // Check if some fields are empty.
        // Confirm Password.
        // If has any error, confirm user.
        if(email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            OkCustomDialog dialog = new OkCustomDialog(
                    this,
                    getString(R.string.signup_error_dialog_title),
                    getString(R.string.signup_emty_fields_dialog_message));
            dialog.show();
        } else if( ! password.equals(confirmPassword)) {
            OkCustomDialog dialog = new OkCustomDialog(
                    this,
                    getString(R.string.signup_error_dialog_title),
                    getString(R.string.signup_confirmed_password_error_dialog_message));
            dialog.show();
        } else {
            // Sign up to Parse
            ParseUser newUser = new ParseUser();
            newUser.setUsername(email);
            newUser.put(FIRST_NAME, firstName);
            newUser.put(LAST_NAME, lastName);
            newUser.setPassword(password);

            mSpinner.setVisibility(View.VISIBLE);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    mSpinner.setVisibility(View.INVISIBLE);
                    if(e == null) {
                        // Success
                        Intent intent = new Intent(ParseSignUpActivity.this, StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Show to user the error.
                        OkCustomDialog dialog = new OkCustomDialog(
                                ParseSignUpActivity.this,
                                getString(R.string.signup_error_dialog_title),
                                e.getMessage());
                        dialog.show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_sign_up, menu);
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
