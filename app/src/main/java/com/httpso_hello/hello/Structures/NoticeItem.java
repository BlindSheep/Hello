package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 24.08.2017.
 */

public class NoticeItem {

    public int id;
    public int user_id;
    public String date_pub;
    public String content;
    public String target_controller; // Контроллер содержимого которого касается уведомление
    public String content_type; // Тип контента которого касается уведомление
    public int target_id; // Идентификатор содержимого которого касается комментарий
    public int profile_id;
    public int type_notice;


    public NoticeItem(){

    }
}
