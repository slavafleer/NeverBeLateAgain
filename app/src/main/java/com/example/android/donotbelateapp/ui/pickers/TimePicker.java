package com.example.android.donotbelateapp.ui.pickers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Slava on 15/06/2015.
 */
public class TimePicker implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    private EditText mEditText;
    private Calendar mCurrentTime;
    private Context mContext;

    public TimePicker(Context context, EditText editText) {
        mContext = context;
        mEditText = editText;
        mEditText.setOnFocusChangeListener(this);
        mCurrentTime = Calendar.getInstance();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            int hour = mCurrentTime.get(Calendar.HOUR);
            int minute = mCurrentTime.get(Calendar.MINUTE);
            TimePickerDialog timePicker;
            timePicker = new TimePickerDialog(mContext, this, hour, minute, true);
            timePicker.setTitle("Choose Time");
            timePicker.show();
        }
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hour, int minute) {
            this.mEditText.setText(hour + ":" + minute);
    }
}
