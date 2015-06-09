package com.example.android.donotbelateapp.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.android.donotbelateapp.OkCustomDialog;
import com.example.android.donotbelateapp.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class EditFriendsActivity extends ListActivity {

    protected List<ParseUser> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_LASTNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null) {
                    // Success
                    mUsers = users;
                    int usersAmount = mUsers.size();
                    String[] firstNames = new String[usersAmount];
                    String[] lastNames = new String[usersAmount];
                    int i = 0;
                    for(ParseUser user : mUsers) {
                        firstNames[i] = user.getString(ParseConstants.KEY_FIRSTNAME);
                        lastNames[i] = user.getString(ParseConstants.KEY_LASTNAME);
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            lastNames
                    );
                    setListAdapter(adapter);


                } else {
                    // Show an error to user
                    OkCustomDialog dialog = new OkCustomDialog(
                            EditFriendsActivity.this,
                            getString(R.string.loading_data_error_title),
                            e.getMessage());
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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
