package com.example.android.donotbelateapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Slava on 22/06/2015.
 */
public class Invitee implements Parcelable {
    String mInvitee;

    public void setInvitee(String invitee) {
        mInvitee = invitee;
    }

    protected Invitee(Parcel in) {
        mInvitee = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mInvitee);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Invitee> CREATOR = new Parcelable.Creator<Invitee>() {
        @Override
        public Invitee createFromParcel(Parcel in) {
            return new Invitee(in);
        }

        @Override
        public Invitee[] newArray(int size) {
            return new Invitee[size];
        }
    };
}
