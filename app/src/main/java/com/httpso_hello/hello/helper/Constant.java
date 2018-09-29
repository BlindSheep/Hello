package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 24.07.2017.
 */

public class Constant {

    //Domen
    public static String host = "https://api.o-hello.com/";
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
    public static String users_get_position_uri = usersController + "getPosition";
    public static String users_remove_photo = usersController + "removePhoto";
    public static String users_set_token_uri = usersController + "setPushUpToken";
    public static String users_subscribe_uri = usersController + "subscribeOnGroup";
    public static String users_unsubscribe_uri = usersController + "unSubscribeOnGroup";


    // Uri for board methods
    private static String boardController = host + "board/";

    public static String board_get_board_uri = boardController;
    public static String board_get_item_uri = boardController + "get";


    // Uri for confirmation methods
    private static String confirmationController = host + "confirmation/";

    public static String confirmation_start_uri = confirmationController + "create";
    public static String confirmation_finish_uri = confirmationController + "confirm";


    // Uri for simpations methods
    private static String simpationsController = host + "simpations/";

    public static String flirtiki_get_flirtik_uri = simpationsController;
    public static String flirtiki_add_uri = simpationsController + "add";


    // Uri for guests methods
    private static String guestsController = host + "guests/";

    public static String guests_get_list_uri = guestsController + "list";


    // Uri for friends methods
    private static String friendsController = host + "friends/";

    public static String friends_get_all_friends_uri = friendsController + "list";
    public static String friends_get_online_friends_uri = friendsController + "online";
    public static String friends_get_incoming_friends_uri = friendsController + "incoming";
    public static String friends_add_friend_uri = friendsController + "add";
    public static String friends_delete_friend_uri = friendsController + "remove";


    //Uri for groops methods
    private static String groupController = host + "group/";
    private static String groupsController = host + "groups/";

    public static String group_get_one_group_uri = groupController;
    public static String group_add_post_uri =  groupController + "addPost";
    public static String group_delete_post_uri = groupController + "deletePost/";
    public static String group_get_subscribers_uri = groupController + "subscribers";
    public static String group_delete_group_uri = groupController + "delete";

    public static String groups_get_my_groups_uri = groupsController;
    public static String groups_get_all_groups_uri = groupsController + "search";
    public static String groups_create_uri = groupsController + "create";


    //Uri for settings methods
    private static String settingsController = host + "settings/";


    //Uri for blackList methods
    private static String blacklistController = host + "blacklist/";

    public static String blacklist_get_ignore_list_uri = blacklistController + "list";
    public static String blacklist_add_ignore_uri = blacklistController + "add";
    public static String blacklist_remove_ignore_uri = blacklistController + "remove";


    //Uri for comments methods
    private static String commentsController = host + "comments/";

    public static String comments_get_comments_uri = commentsController + "get";
    public static String comments_get_counts_uri = commentsController + "count";
    public static String comments_add_comment_uri = commentsController + "add";
    public static String comments_delete_comment_uri = commentsController + "delete";


    //Uri for billing methods
    private static String billingController = host + "billing/";

    public static String billing_pay_uri = billingController + "pay";


    //Uri for notises methods
    private static String noticesController = host + "notices/";

    public static String notices_get_notices_uri = noticesController;


    //Uri for photos methods
    private static String photosController = host + "photos/";


    //Uri for rating methods
    private static String ratingController = host + "rating/";

    public static String rating_get_votes_uri = ratingController + "votesList";
    public static String rating_up_uri = ratingController + "up";
    public static String rating_down_uri = ratingController + "down";


    //Uri for gifts methods
    private static String giftsController = host + "gifts/";

    public static String gifts_get_list_uri = giftsController + "list";




    //Оставшиеся методы
    public static String auth_restore_uri = "auth.restore";

    public static String messages_delete_contact_uri = "messages.delete_contact";
    public static String messages_delete_message_uri = "messages.delete_message";
    public static String messages_add_file_to_message_uri = "messages.add_file_to_message";

    public static String users_update_avatar = "users.update_avatar";

    public static String photos_add_photo = "photos.add_photo";

    public static String paid_services_add_balance_uri = "paid_services.add_balance";
    public static String paid_services_paid_gift_uri = "paid_services.paid_gift";

    public static String temp_files_save_file = "temp_files.save_file";
    public static String temp_files_delete_file = "temp_files.delete_file";

    public static String complaint_add_complaint_uri = "complaint.add_complaint";

    public static String groups_moderate_group_item_uri = "groups.moderate_group_item";
    public static String groups_edit_group_uri = "groups.edit_group";
    public static String groups_update_group_avatar_uri = "groups.update_group_avatar";
}

