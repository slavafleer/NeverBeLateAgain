package com.example.android.donotbelateapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
import com.example.android.donotbelateapp.R;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Slava on 30/06/2015.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

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

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        public TextView mItemSerialNumberLabel;
        public TextView mItemSubjectLabel;
        public TextView mItemDateLabel;
        public TextView mItemTimeLabel;
        public TextView mItemLocationLabel;
        public ImageView mItemInviteesIcon;
        public ImageView mItemInfoIcon;
        public TextView mItemReminderLabel;
        public ImageView mItemApprovingIcon;


        public MeetingViewHolder(View itemView) {
            super(itemView);

            mItemSerialNumberLabel = (TextView)itemView.findViewById(R.id.itemSerialNumberLabel);
            mItemSubjectLabel = (TextView)itemView.findViewById(R.id.itemSubjectLabel);
            mItemDateLabel = (TextView)itemView.findViewById(R.id.itemDateLabel);
            mItemTimeLabel = (TextView)itemView.findViewById(R.id.itemTimeLabel);
            mItemLocationLabel = (TextView)itemView.findViewById(R.id.itemLocationLabel);
            mItemInviteesIcon = (ImageView)itemView.findViewById(R.id.itemInviteesIcon);
            mItemInfoIcon = (ImageView)itemView.findViewById(R.id.itemInfoIcon);
            mItemReminderLabel = (TextView)itemView.findViewById(R.id.itemReminderLabel);
            mItemApprovingIcon = (ImageView)itemView.findViewById(R.id.itemApprovingIcon);
        }

        public void bindMeeting(ParseObject meeting, int position) {
            mItemSerialNumberLabel.setText(++position + "");
            mItemSubjectLabel.setText(meeting.getString(ParseConstants.KEY_SUBJECT));
            Date date = meeting.getDate(ParseConstants.KEY_DATETIME);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String dateString = dateFormat.format(date);
            String timeString = timeFormat.format(date);
            mItemDateLabel.setText(dateString);
            mItemTimeLabel.setText(timeString);
            mItemLocationLabel.setText(meeting.getString(ParseConstants.KEY_LOCATION));
            Calendar currentCalendar = Calendar.getInstance();
            Date currentDate = currentCalendar.getTime();
            long timeLeft = date.getTime() - currentDate.getTime();
            int secondsLeft = (int)timeLeft / 1000;
            int minutesLeft = secondsLeft / 60;
            int hoursLeft = minutesLeft / 60;
            int daysLeft = hoursLeft / 24;
            //TODO: to build exactly how much time left, meentime in hours
            mItemReminderLabel.setText(hoursLeft + "h");
        }
    }


}