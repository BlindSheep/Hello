package com.httpso_hello.hello.Structures;


/**
 * Created by mixir on 30.07.2017.
 */

public class Message {

    public int id;
    public String content;
    public int user_id;
    public int from_id;
    public int contact_id;
    public int to_id;
    public String date_pub;
    public String date_delete;
    public boolean is_show;
    public boolean is_deleted;
    public boolean is_deleted_to;
    public boolean is_deleted_from;
    public int is_new;
    public long deviceMessageId;

    //attachments
    public Attachment[] attachments;

    public Message(){

    }

    public Message(int id, String content, int user_id){
        this.id = id;
        this.content = content;
        this.user_id = user_id;
    }
}
