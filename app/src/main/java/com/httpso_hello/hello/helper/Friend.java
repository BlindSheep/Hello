package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.Structures.Friends;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 23.08.2017.
 */

public class Friend extends Help {

    private Settings stgs;
    private Context _context;

    private static Friend instance;

    public Friend(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Friend getInstance(Context context){
        if(instance == null){
            instance = new Friend(context);
        }
        return instance;
    }

    //получение списка друзей
    public void getFriends(
            final int id,
            final Activity activity,
            final Friend.GetFriendsCallback getFriendsCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_get_friends_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("friends", response);
                            if (response != null) {
                                Friends friends = gson.fromJson(response, Friends.class);
                                if(friends.error == null){
                                    getFriendsCallback.onSuccess(friends.profiles_list, friends.online, friends.request_in_friends, activity);
                                    return;
                                }
                                getFriendsCallback.onError(friends.error.error_code, friends.error.error_msg);
                                return;
                            }
                            getFriendsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getFriendsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    if (id != 0) params.put("id", Integer.toString(id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.get_friends");
        }
    }

    //добавление в друзья
    public void addFriend(
            final int id,
            final Friend.AddFriendsCallback addFriendsCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_add_friend_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("friends", response);
                            if (response != null) {
                                Friends friends = gson.fromJson(response, Friends.class);
                                if(friends.error == null){
                                    addFriendsCallback.onSuccess();
                                    return;
                                }
                                addFriendsCallback.onError(friends.error.error_code, friends.error.error_msg);
                                return;
                            }
                            addFriendsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            addFriendsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("friend_id", Integer.toString(id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.add_friend");
        }
    }

    //удаление друга
    public void deleteFriend(
            final int id,
            final Friend.DeleteFriendsCallback deleteFriendsCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_delete_friend_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("friends", response);
                            if (response != null) {
                                Friends friends = gson.fromJson(response, Friends.class);
                                if(friends.error == null){
                                    deleteFriendsCallback.onSuccess();
                                    return;
                                }
                                deleteFriendsCallback.onError(friends.error.error_code, friends.error.error_msg);
                                return;
                            }
                            deleteFriendsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            deleteFriendsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("friend_id", Integer.toString(id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.delete_friend");
        }
    }

    //принять заявку
    public void acceptFriend(
            final int id,
            final Friend.AcceptFriendCallback acceptFriendCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_accept_friend_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("friends", response);
                            if (response != null) {
                                Friends friends = gson.fromJson(response, Friends.class);
                                if(friends.error == null){
                                    acceptFriendCallback.onSuccess();
                                    return;
                                }
                                acceptFriendCallback.onError(friends.error.error_code, friends.error.error_msg);
                                return;
                            }
                            acceptFriendCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            acceptFriendCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("friend_id", Integer.toString(id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.accept_friend");
        }
    }

    public interface GetFriendsCallback {
        void onSuccess(FriendItem[] allFriends, FriendItem[] onlineFriends, FriendItem[] request_in_friends, Activity activity);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface AddFriendsCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface DeleteFriendsCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface AcceptFriendCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
