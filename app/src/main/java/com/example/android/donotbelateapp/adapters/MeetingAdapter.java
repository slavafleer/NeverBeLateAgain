package com.example.android.donotbelateapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.donotbelateapp.R;
import com.example.android.donotbelateapp.model.parseCom.ParseConstants;
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

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        public TextView mItemSerialNumberLabel;
        public TextView mItemSubjectLabel;
        public TextView mItemDateTimeTopic;
        public TextView mItemDateLabel;
        public TextView mItemTimeLabel;
        public TextView mItemLocationLabel;
        public TextView mItemReminderTopic;
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

            mItemSerialNumberLabel = (TextView)itemView.findViewById(R.id.itemSerialNumberLabel);
            mItemSubjectLabel = (TextView)itemView.findViewById(R.id.itemSubjectLabel);
            mItemDateTimeTopic = (TextView)itemView.findViewById(R.id.itemDateTimeTopic);
            mItemDateLabel = (TextView)itemView.findViewById(R.id.itemDateLabel);
            mItemTimeLabel = (TextView)itemView.findViewById(R.id.itemTimeLabel);
            mItemLocationLabel = (TextView)itemView.findViewById(R.id.itemLocationLabel);
            mItemReminderTopic = (TextView)itemView.findViewById(R.id.itemReminderTopic);
            mItemReminderLabel = (TextView)itemView.findViewById(R.id.itemReminderLabel);
            mItemUserStatus = (TextView)itemView.findViewById(R.id.itemUserStatus);
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
            mItemReminderLabel.setText(timeLeft(date));
        }
    }

    // TODO: need recheck that timeleft shown correct, pay attention to days.
    private String timeLeft(Date dateTime) {
        String answer = "";
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();
        long timeLeft = dateTime.getTime() - currentDate.getTime();
        long secondsLeft = timeLeft / 1000;
        int minutesLeft = (int) (secondsLeft / 60);
        if(minutesLeft < 60) {
            answer = minutesLeft + "m";
        } else {
            int hoursLeft = minutesLeft / 60;
            int minutes = minutesLeft % 60;
            if(hoursLeft < 24) {
                if(minutes == 0) {
                    answer = hoursLeft + "h";
                } else {
                    answer = hoursLeft + "h " + minutes + "m";
                }
            } else {
                int daysLeft = hoursLeft / 24;
                int hours = hoursLeft % 24;
                if(minutes == 0) {
                    if(hours == 0) {
                        answer = daysLeft + "d";
                    } else {
                        answer = daysLeft + "d " + hoursLeft%24 + "h";
                    }
                } else {
                    if(hours == 0) {
                        answer = daysLeft + "d " + minutesLeft%60 + "m";
                    } else {
                        answer = daysLeft + "d " + hoursLeft%24 + "h " + minutesLeft%60 + "m";
                    }
                }
            }
        }

        return answer;
    }

}
