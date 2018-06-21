package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 24.07.2017.
 */

public class Constant {

    //Domen
    public static String host = "http://t-hello.ru/";
    //Uploads
    public static String upload = "https://o-hello.com/upload/";
    // Standart avatar
    public static String default_avatar = upload + "https://o-hello.com/upload/default/avatar.jpg";
    //Metrika Api Key
    public static String metrika_api_key = "71dba453-2cd4-4256-b676-77d249383d44";


    // Uri for auth methods
    private static String authController = host + "auth/";

    public static String auth_uri = authController + "login";
    public static String logout_uri = authController + "logout";
    public static String registr_uri = authController + "register";


    // Uri for users methods
    private static String usersController = host + "users/";

    public static String users_get_small_user_info_uri = usersController + "getStartInfo";
    public static String users_edit_profile_uri = usersController + "edit";
    public static String users_get_profile_uri = usersController;
    public static String users_search_profiles_uri = usersController;


    // Uri for board methods
    private static String boardController = host + "board/";

    public static String board_get_board_uri = boardController;


    // Uri for confirmation methods
    private static String confirmationController = host + "confirmation/";

    public static String confirmation_start_uri = confirmationController + "create";
    public static String confirmation_finish_uri = confirmationController + "confirm";


    // Uri for simpations methods
    private static String simpationsController = host + "simpations/";

    public static String flirtiki_get_flirtik_uri = simpationsController;


    // Uri for guests methods
    private static String guestsController = host + "guests/";


    // Uri for friends methods
    private static String friendsController = host + "friends/";

    public static String friends_get_all_friends_uri = friendsController;
    public static String friends_get_online_friends_uri = friendsController + "online";
    public static String friends_get_incoming_friends_uri = friendsController + "incoming";
    public static String friends_add_friend_uri = friendsController + "add";
    public static String friends_delete_friend_uri = friendsController + "remove";











    //Методы
    public static String set_token_uri = "auth.set_token";
    public static String auth_restore_uri = "auth.restore";

    public static String messages_get_contacts_uri = "messages.get_contacts";
    public static String messages_get_messages_uri = "messages.get_messages";
    public static String messages_send_message_uri = "messages.send_message";
    public static String messages_get_notices_uri = "messages.get_notices";
    public static String messages_delete_contact_uri = "messages.delete_contact";
    public static String messages_delete_message_uri = "messages.delete_message";
    public static String messages_refresh_contacts_uri = "messages.refresh_contacts";
    public static String messages_refresh_messages_uri = "messages.refresh_messages";
    public static String messages_get_state_messages_uri = "messages.get_state_messages";
    public static String messages_get_read_state_messages_uri = "messages.get_read_state_messages";
    public static String messages_add_file_to_message_uri = "messages.add_file_to_message";

    public static String board_add_item_uri = "content.add_item";

    public static String users_update_avatar = "users.update_avatar";
    public static String users_get_balance_uri = "users.get_balance";
    public static String users_get_counts_uri = "users.get_counts";
    public static String users_get_ignore_list_uri = "users.get_ignore_list";
    public static String users_ignor_contact_uri = "users.ignore_user";
    public static String users_delete_user_ignore_uri = "users.delete_user_ignore";

    public static String content_delete_item_uri = "content.delete_item";
    public static String content_get_item_board_uri = "content.get_item.board";

    public static String gifts_get_gifts_uri = "gifts.get_gifts";

    public static String photos_add_photo = "photos.add_photo";
    public static String photos_delete_photo = "photos.delete_photo";

    public static String paid_services_raising_uri = "paid_services.raising";
    public static String paid_services_paid_raising_uri = "paid_services.paid_raising";
    public static String paid_services_add_balance_uri = "paid_services.add_balance";
    public static String paid_services_paid_gift_uri = "paid_services.paid_gift";
    public static String paid_services_remove_points_uri = "paid_services.paid_view_guests";

    public static String guests_get_guests_uri = "guests.get_guests";

    public static String rating_get_info_uri = "rating.get_info";
    public static String rating_send_like_uri = "rating.vote";

    public static String comments_get_comments_uri = "comments.get_comments";
    public static String comments_send_comments_uri = "comments.add_comment";
    public static String comments_get_counts_comments_uri = "comments.get_count_comments";
    public static String comments_delete_comments_uri = "comments.delete_comment";

    public static String flirtiki_get_info_uri = "flirtiki.get_flirtiki";

    public static String groups_get_groups_uri = "groups.get_groups";
    public static String groups_get_members_uri = "groups.get_members";
    public static String groups_subscribe_uri = "groups.subscribe";
    public static String groups_create_group_uri = "groups.add_group";
    public static String groups_moderate_group_item_uri = "groups.moderate_group_item";
    public static String groups_edit_group_uri = "groups.edit_group";
    public static String groups_update_group_avatar_uri = "groups.update_group_avatar";
    public static String groups_request_delete_group_uri = "groups.request_delete_group";
    public static String groups_delete_group_uri = "groups.delete_group";

    public static String temp_files_save_file = "temp_files.save_file";
    public static String temp_files_delete_file = "temp_files.delete_file";

    public static String complaint_add_complaint_uri = "complaint.add_complaint";

    public static String logs_add_uri = "logs.add";
}

