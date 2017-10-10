package com.httpso_hello.hello.Structures;

import java.io.Serializable;

/**
 * Created by mixir on 23.08.2017.
 */

public class FriendItem implements Serializable{

    public int id;
    public String nickname;
    public Image avatar;
    public String city_cache;
    public String birth_date;

    public FriendItem(){

    }
}
