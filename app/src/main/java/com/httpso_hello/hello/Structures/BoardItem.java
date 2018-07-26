package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 02.08.2017.
 */

public class BoardItem {

    public int id;
    public String dateApproved;
    public int rating;
    public Image[] photos;
    public User User;
    public int userId;
    public boolean isVoted;
    public String content;
    public int countReaded;
    public String city_cache;
    public int comments;
    public boolean isAnonim;

    public int groupId;
    public Groups groupInfo;

    public BoardItem(){

    };

}
