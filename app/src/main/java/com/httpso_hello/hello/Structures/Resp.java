package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 25.07.2017.
 */

public class Resp extends SuperStructure {

    public int user_id;
    public String auth_token;
    public User user_info;
    // New fields

    public NewProfile profile;

    public int id;
    public String nickname;
    public String birthDate;
    public Image avatar;
    public boolean isLocked;
    public boolean isActivated;
    public boolean isDeleted;

    public Resp(){

    }



}
