package com.example.android.donotbelateapp.ui;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.donotbelateapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ParseSignUpActivity extends ActionBarActivity {

    @InjectView(R.id.signup_email) EditText mEmail;
    @InjectView(R.id.signup_first_name) EditText mFirstName;
    @InjectView(R.id.signup_last_name) EditText mLastName;
    @InjectView(R.id.signup_password) EditText mPassword;
    @InjectView(R.id.signup_confirm_password) EditText mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_sign_up);
        ButterKnife.inject(this);
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
            // TODO: replace this temperaly dialog with custom one for different types of errors.
            AlertDialog.Builder builder = new AlertDialog.Builder(ParseSignUpActivity.this)
                    .setTitle("Sign Up Error")
                    .setMessage("Some of the fields are empty")
                    .setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
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
