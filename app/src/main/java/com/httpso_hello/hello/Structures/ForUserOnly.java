package com.httpso_hello.hello.Structures;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mixir on 09.06.2018.
 */

public class ForUserOnly implements Parcelable {

    public User user;

    public ForUserOnly() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, 0);
    }

    protected ForUserOnly(Parcel in) {
        this.user = in.readParcelable(user.getClass().getClassLoader());
    }

    public static final Parcelable.Creator<ForUserOnly> CREATOR = new Parcelable.Creator<ForUserOnly>() {
        @Override
        public ForUserOnly createFromParcel(Parcel source) {
            return new ForUserOnly(source);
        }

        @Override
        public ForUserOnly[] newArray(int size) {
            return new ForUserOnly[size];
        }
    };
}
