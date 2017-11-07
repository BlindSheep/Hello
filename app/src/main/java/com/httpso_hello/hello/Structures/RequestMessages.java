package com.httpso_hello.hello.Structures;

import java.io.Serializable;

/**
 * Created by mixir on 01.08.2017.
 */

public class RequestMessages {

    public Message messages[];// Сообщения из переписки
    public Message sendedUnreadedMessagesIDs[]; // ID отправленных, не прочитанных сообщений
    public Image userAvatar;// Ссылки на аватарку контакта
    public Error error;// Обхект ошибки если есть
    public String dateLastUpdate; // Дата последнего обновления
    public boolean contact_is_online; // Онлайн или нет контакт
    public boolean is_writing; // Печатает или нет

    public RequestMessages(){

    }
}
