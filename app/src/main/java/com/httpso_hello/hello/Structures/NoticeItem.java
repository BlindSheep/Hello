package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 24.08.2017.
 */

public class NoticeItem {

    public int id; // id уведомления
    public int user_id; // ИДентфикатор пользователя которому отправлено уведомление
    public String date_pub; // Дата создания уведомления
    public String content; // Текст уведомления пока пустой, оставлен для совместимости с сайтом
    public String target_controller; // Контроллер содержимого которого касается уведомление
    public String content_type; // Тип контента которого касается уведомление
    public int target_id; // Идентификатор содержимого которого касается комментарий
    public int profile_id; // Ижентификатор пользоватялея который является инициатором уведомления
    public User sender_user; // Информация о пользователе, который отправил уведмоление
    public int type_notice; // Целое число указывающее на тип события инициирующее уведомление: 1 - лайк, 2 - комментарий, 3 - подарок
    public Image target_preview; // Превью изображения которого косается уведомления, заполенено в случае если уведомление касается фотографии
    public String target_content; // Контент цели, которой косается уведомление. Заполнеяется в случае если уведомление касается коментария или объявления, или подарка
    public String comment_content; // Если уведомление инициировано комментарием то заполняется текстом комментария
    public int is_new;


    public NoticeItem(){

    }
}
