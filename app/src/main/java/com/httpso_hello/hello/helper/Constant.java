package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 24.07.2017.
 */

public class Constant {

    public static String api_key = "9ecd2d27f94e2a575418ba017cf4379a";
    public static String host = "https://o-hello.com/";
    public static String upload = host + "upload/";
    private static String api_version  = "api/";
    private static String start_method = "method/";

    private static String start_api_uri = host + api_version + start_method;

    //Методы
    public static String registr_uri = start_api_uri + "auth.signup";
    public static String auth_uri = start_api_uri + "auth.login";
    public static String logout_uri = start_api_uri + "auth.logout";
    public static String set_token_uri = start_api_uri + "auth.set_token";
    public static String auth_restore_uri = start_api_uri + "auth.restore";

    public static String messages_get_contacts_uri = start_api_uri + "messages.get_contacts";
    public static String messages_get_messages_uri = start_api_uri + "messages.get_messages";
    public static String messages_send_message_uri = start_api_uri + "messages.send_message";
    public static String messages_get_notices_uri = start_api_uri + "messages.get_notices";
    public static String messages_delete_contact_uri = start_api_uri + "messages.delete_contact";
    public static String messages_refresh_contacts_uri = start_api_uri + "messages.refresh_contacts";
    public static String messages_refresh_messages_uri = start_api_uri + "messages.refresh_messages";
    public static String messages_get_state_messages_uri = start_api_uri + "messages.get_state_messages";
    public static String messages_add_file_to_message_uri = start_api_uri + "messages.add_file_to_message";

    public static String board_get_board_uri = start_api_uri + "content.get.board";
    public static String board_add_item_uri = start_api_uri + "content.add_item";

    public static String users_get_profile_uri = start_api_uri + "users.get_profile";
    public static String users_get_small_user_info_uri = start_api_uri + "users.get_small_user_info";
    public static String users_edit_profile_uri = start_api_uri + "users.profile_edit";
    public static String users_search_profiles_uri = start_api_uri + "users.search_profiles";
    public static String users_add_friend_uri = start_api_uri + "users.add_friend";
    public static String users_delete_friend_uri = start_api_uri + "users.delete_friend";
    public static String users_accept_friend_uri = start_api_uri + "users.add_friend";
    public static String users_get_friends_uri = start_api_uri + "users.get_friends";
    public static String users_update_avatar = start_api_uri + "users.update_avatar";
    public static String users_get_balance_uri = start_api_uri + "users.get_balance";
    public static String users_get_online_uri = start_api_uri + "users.get_online";
    public static String users_get_counts_uri = start_api_uri + "users.get_counts";
    public static String users_get_ignore_list_uri = start_api_uri + "users.get_ignore_list";
    public static String users_ignor_contact_uri = start_api_uri + "users.ignore_user";
    public static String users_delete_user_ignore_uri = start_api_uri + "users.delete_user_ignore";

    public static String content_delete_item_uri = start_api_uri + "content.delete_item";

    public static String gifts_get_gifts_uri = start_api_uri + "gifts.get_gifts";

    public static String photos_add_photo = start_api_uri + "photos.add_photo";
    public static String photos_delete_photo = start_api_uri + "photos.delete_photo";

    public static String paid_services_raising_uri = start_api_uri + "paid_services.raising";
    public static String paid_services_paid_raising_uri = start_api_uri + "paid_services.paid_raising";
    public static String paid_services_add_balance_uri = start_api_uri + "paid_services.add_balance";
    public static String paid_services_paid_gift_uri = start_api_uri + "paid_services.paid_gift";

    public static String guests_get_guests_uri = start_api_uri + "guests.get_guests";

    public static String rating_get_info_uri = start_api_uri + "rating.get_info";
    public static String rating_send_like_uri = start_api_uri + "rating.vote";

    public static String comments_get_comments_uri = start_api_uri + "comments.get_comments";
    public static String comments_send_comments_uri = start_api_uri + "comments.add_comment";
    public static String comments_get_counts_comments_uri = start_api_uri + "comments.get_count_comments";
    public static String comments_delete_comments_uri = start_api_uri + "comments.delete_comment";

    public static String flirtiki_get_info_uri = start_api_uri + "flirtiki.get_flirtiki";
    public static String flirtiki_send_flirtik_uri = start_api_uri + "flirtiki.send_flirtik";

    public static String temp_files_save_file = start_api_uri + "temp_files.save_file";

    //Заглушка для аватарки
    public static String default_avatar = upload + "default/avatar.jpg";

    public static String metrika_api_key = "71dba453-2cd4-4256-b676-77d249383d44";
}

