package com.example.android.donotbelateapp.modul;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Slava on 22/06/2015.
 */
public class Invitees implements Parcelable {

    ArrayList<String> mInviteesList;


    public ArrayList<String> getInviteesList() {
        return mInviteesList;
    }

    public void setInviteesList(ArrayList<String> inviteesList) {
        mInviteesList = inviteesList;
    }

    public void addToInviteesList(String invityId) {
        mInviteesList.add(invityId);
    }

    protected Invitees(Parcel in) {
        if (in.readByte() == 0x01) {
            mInviteesList = new ArrayList<String>();
            in.readList(mInviteesList, String.class.getClassLoader());
        } else {
            mInviteesList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mInviteesList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mInviteesList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Invitees> CREATOR = new Parcelable.Creator<Invitees>() {
        @Override
        public Invitees createFromParcel(Parcel in) {
            return new Invitees(in);
        }

        @Override
        public Invitees[] newArray(int size) {
            return new Invitees[size];
        }
    };
}
