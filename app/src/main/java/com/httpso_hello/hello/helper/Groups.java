package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.AllGroups;
import com.httpso_hello.hello.Structures.SearchProfiles;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.Structures.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 24.11.2017.
 */

public class Groups extends Help {

    private Settings stgs;
    private Context _context;

    private static Groups instance;

    public Groups(Context context){
        super(context);
        this._context = context;
        stgs = new Settings(context);

    }

    public static synchronized Groups getInstance(Context context){
        if(instance == null){
            instance = new Groups(context);
        }
        return instance;
    }

    public void getGroups(
            //если 0 - все группы, если 1 - мои группы, если 2 - то одна группа
            final int parameters,
            final int group_id,
            final String search,
            final GetGroupsCallback getGroupsCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_get_groups_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                AllGroups allGroups = gson.fromJson(response, AllGroups.class);
                                if(allGroups.error==null){
                                    getGroupsCallback.onSuccess(allGroups.groups);
                                    return;
                                }
                                getGroupsCallback.onError(allGroups.error.error_code, allGroups.error.error_msg);
                                return;
                            }
                            getGroupsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getGroupsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    if (group_id != 0) params.put("group_id", Integer.toString(group_id));
                    if (search != null) params.put("search", search);
                    params.put("parameters", Integer.toString(parameters));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getGroups");
        }
    }

    public void getFollowers(
            final int group_id,
            final GetFollowersCallback getFollowersCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_get_members_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                SearchProfiles allFollowers = gson.fromJson(response, SearchProfiles.class);
                                if(allFollowers.error==null){
                                    getFollowersCallback.onSuccess(allFollowers.profiles_list);
                                    return;
                                }
                                getFollowersCallback.onError(allFollowers.error.error_code, allFollowers.error.error_msg);
                                return;
                            }
                            getFollowersCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getFollowersCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("group_id", Integer.toString(group_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getMembers");
        }
    }

    public void subscribe(
            final int action, //1 - добавление , 0 - удаление
            final int group_id,
            final int user_id,
            final GetSubscribeCallback getSubscribeCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_subscribe_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if(res.error==null){
                                    getSubscribeCallback.onSuccess();
                                    return;
                                }
                                getSubscribeCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            getSubscribeCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getSubscribeCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("group_id", Integer.toString(group_id));
                    params.put("action", Integer.toString(action));
                    if (user_id != 0) params.put("user_id", Integer.toString(user_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getGroups");
        }
    }

    public void createGroup(
            final String title,
            final String desc,
            final boolean isModeration,
            final CreateGroupCallback createGroupCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_create_group_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if(res.error==null){
                                    createGroupCallback.onSuccess(res.id);
                                    return;
                                }
                                createGroupCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            createGroupCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            createGroupCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("title", title);
                    params.put("description", desc);
                    if (isModeration) params.put("moderate", Integer.toString(1));
                    else params.put("moderate", Integer.toString(0));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.createGroups");
        }
    }

    public void moderateGroupItem(
            final int action, //1 - опубликовать, 0 - удалить
            final int id,
            final int group_id,
            final ModerateGroupItemCallback moderateGroupItemCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_moderate_group_item_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if(res.error==null){
                                    moderateGroupItemCallback.onSuccess();
                                    return;
                                }
                                moderateGroupItemCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            moderateGroupItemCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            moderateGroupItemCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params =getParamsMap();
                    params.put("id", Integer.toString(id));
                    params.put("group_id", Integer.toString(group_id));
                    params.put("action", Integer.toString(action));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.moderateGroupsItem");
        }
    }

    public void editGroup(
            final int group_id,
            final String title,
            final String desc,
            final boolean isModeration,
            final EditGroupCallback editGroupCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_edit_group_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("group", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if(res.error==null){
                                    editGroupCallback.onSuccess();
                                    return;
                                }
                                editGroupCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            editGroupCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            editGroupCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("group_id", Integer.toString(group_id));
                    params.put("title", title);
                    params.put("description", desc);
                    if (isModeration) params.put("moderate", Integer.toString(1));
                    else params.put("moderate", Integer.toString(0));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.editGroups");
        }
    }

    public void updateGroupAvatar(
            final String base64_code_ava,
            final String ext,
            final int group_id,
            final UpdateAvatarCallback updateAvatarCallback,
            final ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.groups_update_group_avatar_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("update_avatar", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if(res.error==null){
                                    updateAvatarCallback.onSuccess();
                                    return;
                                }
                                errorCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            errorCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            errorCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("base64_code_ava", base64_code_ava);
                    params.put("ext", ext);
                    params.put("group_id", Integer.toString(group_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.editGroups");
        }
    }

    public interface UpdateAvatarCallback{
        void onSuccess();
    }

    public interface GetGroupsCallback {
        public void onSuccess(com.httpso_hello.hello.Structures.Groups[] groupItem);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface GetFollowersCallback {
        public void onSuccess(User[] users);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface GetSubscribeCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface CreateGroupCallback {
        public void onSuccess(int id);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface ModerateGroupItemCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface EditGroupCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
