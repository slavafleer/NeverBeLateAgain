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
import com.alpha.android.donotbelateapp.ResetPasswordActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ParseLoginActivity extends ActionBarActivity {

    @InjectView(R.id.login_email) EditText mUsername;
    @InjectView(R.id.login_password) EditText mPassword;
    @InjectView(R.id.login_spinner) ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_login);
        ButterKnife.inject(this);

        mSpinner.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.login_signup_link)
    public void onClickSignupLink() {
        Intent signupIntent = new Intent(ParseLoginActivity.this, ParseSignUpActivity.class);
        startActivity(signupIntent);
    }

    @OnClick(R.id.login_button)
    public void onClickLoginButton() {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            // Check for empty fields and inform the user.
            OkCustomDialog dialog = new OkCustomDialog(
                    this,
                    getString(R.string.login_error_dialog_title),
                    getString(R.string.login_empty_fields_dialog_message));
            dialog.show();
        } else {
            // Success
            mSpinner.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    mSpinner.setVisibility(View.INVISIBLE);
                    if (parseUser != null) {
                        // Success
                        Intent intent = new Intent(ParseLoginActivity.this, StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Show the error.
                        OkCustomDialog dialog = new OkCustomDialog(
                                ParseLoginActivity.this,
                                getString(R.string.login_error_dialog_title),
                                e.getMessage()
                        );
                        dialog.show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.loginForgotPassword)
    public void onClickForgotPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_login, menu);
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
