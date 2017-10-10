package com.httpso_hello.hello.Structures;

import java.io.Serializable;

/**
 * Created by mixir on 25.07.2017.
 */

public class User implements Serializable {

    public int id;
//  public int vk_id;
//  public int[] groups;
//  public String email;
//  public boolean need_add_email;
//  public int balance;
//  public int plan_id;
//  public boolean is_admin;
    public String nickname;
//  public String date_reg;
    public String date_log;
//  public String date_group;
//  public String ip;
    public boolean is_locked;

//  public String lock_untill;
//  public String lock_reason;
    public String auth_token;
//  public int files_count;
    public int friends_count;
//  public String time_zone;
//  public int karma;
//  public int rating;

//  public String[] theme;

//  public String[] notify_options;

//  public String privacy_options;

//  public int status_id;
//  public String status_text;
//  public int inviter_id;
//  public int invites_count;
//  public String date_invites;
    public String birth_date;
    public int city;
    public Image avatar;
    public Photo[] photos;
    public String skype;
    public String phone;
    public String movies;
    public String city_cache;
    public String sp; //надо править
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
//  public String fcm_token;
    public int looking_for;
    public int looking_for_age;
    public int age_do;
//  public String inviter_nickname;
    public boolean is_online;
    public int region;
    public int country;
//  public boolean is_moderator;
    public int friends_state;
    public int flirt_state;
    public String avto;
    public String music;

    public String[] friends;
    public String[] gifts;

    public User(){

    }
}
