package com.httpso_hello.hello.Structures;


/**
 * Created by mixir on 30.07.2017.
 */

public class Message {

    public int id;
    public String content;
    public int user_id;
    public int contact_id;
    public String createdAt;
    public boolean isNew;
    public long deviceMessageId;
    public boolean incoming;

    //attachments
    public Attachment[] photos;

    public Message(){

    }

    public Message(int id, String content, int user_id){
        this.id = id;
        this.content = content;
        this.user_id = user_id;
    }
}
