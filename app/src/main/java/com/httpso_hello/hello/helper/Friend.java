package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.ForUserOnly;
import com.httpso_hello.hello.Structures.Friends;

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
            final String type,
            final Activity activity,
            final Friend.GetFriendsCallback getFriendsCallback
    ){
        String uri = Constant.friends_get_all_friends_uri;
        if (type.isEmpty()) uri = Constant.friends_get_all_friends_uri;
        else if (type.equals("online")) uri = Constant.friends_get_online_friends_uri;
        else if (type.equals("incoming")) uri = Constant.friends_get_incoming_friends_uri;
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("friends", response);
                        if (response != null) {
                            Friends friends = gson.fromJson(response, Friends.class);
                            if(friends.error == null){
                                getFriendsCallback.onSuccess(friends.friends, activity, id == stgs.getSettingInt("userId"));
                                setNewToken(friends.token);
                                return;
                            }
                            getFriendsCallback.onError(friends.error.code, friends.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                if (!type.equals("incoming")) params.put("userId ", Integer.toString(id));
                params.put("page", Integer.toString(0));
                params.put("perPage", Integer.toString(30));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "friends");
    }

    //добавление в друзья
    public void addFriend(
            final int id,
            final Friend.AddFriendsCallback addFriendsCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.friends_add_friend_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("friends", response);
                        if (response != null) {
                            Friends friends = gson.fromJson(response, Friends.class);
                            if(friends.error == null){
                                addFriendsCallback.onSuccess();
                                setNewToken(friends.token);
                                return;
                            }
                            addFriendsCallback.onError(friends.error.code, friends.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("friendId", Integer.toString(id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.add_friend");
    }

    //удаление друга
    public void deleteFriend(
            final int id,
            final Friend.DeleteFriendsCallback deleteFriendsCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.friends_delete_friend_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("friends", response);
                        if (response != null) {
                            Friends friends = gson.fromJson(response, Friends.class);
                            if(friends.error == null){
                                deleteFriendsCallback.onSuccess();
                                setNewToken(friends.token);
                                return;
                            }
                            deleteFriendsCallback.onError(friends.error.code, friends.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("friendId", Integer.toString(id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.delete_friend");
    }

    public interface GetFriendsCallback {
        void onSuccess(ForUserOnly[] allFriends, Activity activity, boolean isUserFriends);
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
}
