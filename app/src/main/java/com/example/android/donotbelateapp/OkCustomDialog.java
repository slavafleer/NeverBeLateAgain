package com.example.android.donotbelateapp;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Slava on 02/06/2015.
 */
public class OkCustomDialog {
    Context mContext;
    String mTitle;
    String mMessage;


    public OkCustomDialog(Context context, String title, String message) {
        mContext = context;
        mTitle = title;
        mMessage = message;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
