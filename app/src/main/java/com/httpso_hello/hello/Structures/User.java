package com.httpso_hello.hello.Structures;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mixir on 25.07.2017.
 */

public class User implements Parcelable {

    public static final int GENDER_MAN = 1;
    public static final int GENDER_WOOMEN = 2;

    public int id;
    public String nickname;
    public String date_log;
    public boolean isLocked;
    public String auth_token;
    public int friends_count;
    public String birthDate;
    public int city;
    public Image avatar;
    public Photo[] photos;
    public String skype;
    public String phone;
    public String movies;
    public String city_cache;
    public String books;
    public String games;
    public int gender;
    public int avatar_album_id;
    public int main_album_id;
    public String reg_cel;
    public String interes;
    public int ves;
    public int rost;
    public int mat_poloz;
    public int looking_for;
    public int looking_for_age;
    public int age_do;
    public boolean isOnline;
    public int region;
    public int country;
    public String avto;
    public String music;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nickname);
        dest.writeString(this.date_log);
        dest.writeByte(this.isLocked ? (byte) 1 : (byte) 0);
        dest.writeString(this.auth_token);
        dest.writeInt(this.friends_count);
        dest.writeString(this.birthDate);
        dest.writeInt(this.city);
        dest.writeSerializable(this.avatar);
        dest.writeString(this.skype);
        dest.writeString(this.phone);
        dest.writeString(this.movies);
        dest.writeString(this.city_cache);
        dest.writeString(this.books);
        dest.writeString(this.games);
        dest.writeInt(this.gender);
        dest.writeInt(this.avatar_album_id);
        dest.writeInt(this.main_album_id);
        dest.writeString(this.reg_cel);
        dest.writeString(this.interes);
        dest.writeInt(this.ves);
        dest.writeInt(this.rost);
        dest.writeInt(this.mat_poloz);
        dest.writeInt(this.looking_for);
        dest.writeInt(this.looking_for_age);
        dest.writeInt(this.age_do);
        dest.writeByte(this.isOnline ? (byte) 1 : (byte) 0);
        dest.writeInt(this.region);
        dest.writeInt(this.country);
        dest.writeString(this.avto);
        dest.writeString(this.music);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.nickname = in.readString();
        this.date_log = in.readString();
        this.isLocked = in.readByte() != 0;
        this.auth_token = in.readString();
        this.friends_count = in.readInt();
        this.birthDate = in.readString();
        this.city = in.readInt();
        this.avatar = (Image) in.readSerializable();
        this.skype = in.readString();
        this.phone = in.readString();
        this.movies = in.readString();
        this.city_cache = in.readString();
        this.books = in.readString();
        this.games = in.readString();
        this.gender = in.readInt();
        this.avatar_album_id = in.readInt();
        this.main_album_id = in.readInt();
        this.reg_cel = in.readString();
        this.interes = in.readString();
        this.ves = in.readInt();
        this.rost = in.readInt();
        this.mat_poloz = in.readInt();
        this.looking_for = in.readInt();
        this.looking_for_age = in.readInt();
        this.age_do = in.readInt();
        this.isOnline = in.readByte() != 0;
        this.region = in.readInt();
        this.country = in.readInt();
        this.avto = in.readString();
        this.music = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
