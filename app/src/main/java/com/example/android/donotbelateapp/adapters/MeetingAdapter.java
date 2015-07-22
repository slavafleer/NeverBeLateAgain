package com.example.android.donotbelateapp.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.example.android.donotbelateapp.utils.TimeToString;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Slava on 30/06/2015.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {
    private static final String TAG = MeetingAdapter.class.getSimpleName();

    private List<ParseObject> mMeetings;

    public MeetingAdapter(List<ParseObject> meetings) {
        mMeetings = meetings;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.adapter_item_meeting, viewGroup, false);
        MeetingViewHolder viewHolder = new MeetingViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder meetingViewHolder, int i) {
        meetingViewHolder.bindMeeting(mMeetings.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    // TODO: need recheck that timeleft shown correct, pay attention to days.
    private String timeLeft(Date dateTime) {

        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();
        long timeLeft = dateTime.getTime() - currentDate.getTime();

        return TimeToString.getTime(timeLeft);
    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mRelativeLayout;
        public TextView mItemSerialNumberLabel;
        public TextView mItemSubjectLabel;
        public TextView mItemDateLabel;
        public TextView mItemTimeLabel;
        public TextView mItemLocationLabel;
        public TextView mItemReminderLabel;
        public TextView mItemUserStatus;

        public MeetingViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "clicked " + getLayoutPosition(), Toast.LENGTH_LONG).show();
                }
            });

            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.itemRelativeLayout);
            mItemSerialNumberLabel = (TextView) itemView.findViewById(R.id.itemSerialNumberLabel);
            mItemSubjectLabel = (TextView) itemView.findViewById(R.id.itemSubjectLabel);
            mItemDateLabel = (TextView) itemView.findViewById(R.id.itemDateLabel);
            mItemTimeLabel = (TextView) itemView.findViewById(R.id.itemTimeLabel);
            mItemLocationLabel = (TextView) itemView.findViewById(R.id.itemLocationLabel);
            mItemReminderLabel = (TextView) itemView.findViewById(R.id.itemReminderLabel);
            mItemUserStatus = (TextView) itemView.findViewById(R.id.itemUserStatus);
        }

        public void bindMeeting(ParseObject meeting, int position) {
            if(position % 2 == 0) {
                mRelativeLayout.setBackgroundColor(0x11000000 + Color.BLACK);
            }
            mItemSerialNumberLabel.setText(++position + ".");
            mItemSubjectLabel.setText(meeting.getString(ParseConstants.KEY_SUBJECT));
            Date date = meeting.getDate(ParseConstants.KEY_DATETIME);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String dateString = dateFormat.format(date);
            String timeString = timeFormat.format(date);
            mItemDateLabel.setText(dateString);
            mItemTimeLabel.setText(timeString);
            mItemLocationLabel.setText(meeting.getString(ParseConstants.KEY_LOCATION));
            mItemReminderLabel.setText(timeLeft(date));
        }
    }

}
