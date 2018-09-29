package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.AllGroups;
import com.httpso_hello.hello.Structures.OneGroup;
import com.httpso_hello.hello.Structures.ReqCSRFToken;
import com.httpso_hello.hello.Structures.SearchProfiles;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.Structures.User;

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
            //если 0 - все группы, если 1 - мои группы
            final int parameters,
            final int group_id,
            final String search,
            final GetGroupsCallback getGroupsCallback
    ){
        String req = null;
        if (parameters == 0) req = Constant.groups_get_all_groups_uri;
        else if (parameters == 1) req = Constant.groups_get_my_groups_uri;
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                req,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            AllGroups allGroups = gson.fromJson(response, AllGroups.class);
                            if(allGroups.error==null){
                                getGroupsCallback.onSuccess(allGroups.groups);
                                setNewToken(allGroups.token);
                                return;
                            }
                            getGroupsCallback.onError(allGroups.error.code, allGroups.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                if (group_id != 0) params.put("groupId", Integer.toString(group_id));
                if (search != null) params.put("search", search);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getGroups");
    }

    public void getOneGroups(
            final int group_id,
            final String search,
            final GetOneGroupsCallback getGroupsCallback
    ){
        String req = null;
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.group_get_one_group_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            OneGroup oneGroup = gson.fromJson(response, OneGroup.class);
                            if(oneGroup.error==null){
                                getGroupsCallback.onSuccess(oneGroup);
                                setNewToken(oneGroup.token);
                                return;
                            }
                            getGroupsCallback.onError(oneGroup.error.code, oneGroup.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                if (group_id != 0) params.put("groupId", Integer.toString(group_id));
                if (search != null) params.put("search", search);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getGroups");
    }

    public void getFollowers(
            final int groupId,
            final int page,
            final GetFollowersCallback getFollowersCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.group_get_subscribers_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            SearchProfiles allFollowers = gson.fromJson(response, SearchProfiles.class);
                            if(allFollowers.error==null){
                                getFollowersCallback.onSuccess(allFollowers.users);
                                return;
                            }
                            getFollowersCallback.onError(allFollowers.error.code, allFollowers.error.message);
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
                params.put("groupId", Integer.toString(groupId));
                params.put("page", Integer.toString(page));
                params.put("perPage", Integer.toString(30));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getMembers");
    }

    public void subscribe(
            final int action, //1 - добавление , 0 - удаление
            final int groupId,
            final int user_id,
            final GetSubscribeCallback getSubscribeCallback
    ){
                String uri = "";
                if (action == 0) uri = Constant.users_unsubscribe_uri;
                else if (action == 1) uri = Constant.users_subscribe_uri;
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                            if(res.error==null){
                                getSubscribeCallback.onSuccess();
                                return;
                            }
                            getSubscribeCallback.onError(res.error.code, res.error.message);
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
                params.put("groupId", Integer.toString(groupId));
                if (user_id != 0) params.put("user_id", Integer.toString(user_id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.getGroups");
    }

    public void createGroup(
            final String title,
            final String description,
            final boolean moderate,
            final CreateGroupCallback createGroupCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.groups_create_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            OneGroup res = gson.fromJson(response, OneGroup.class);
                            if(res.error==null){
                                setNewToken(res.token);
                                createGroupCallback.onSuccess(res.groupInfo.id);
                                return;
                            }
                            createGroupCallback.onError(res.error.code, res.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("title", title);
                params.put("description", description);
                params.put("moderate", Boolean.toString(moderate));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.createGroups");
    }

    public void moderateGroupItem(
            final int action, //1 - опубликовать, 0 - удалить
            final int id,
            final int group_id,
            final ModerateGroupItemCallback moderateGroupItemCallback
    ){
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
                            moderateGroupItemCallback.onError(res.error.code, res.error.message);
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

    public void editGroup(
            final int group_id,
            final String title,
            final String desc,
            final boolean isModeration,
            final EditGroupCallback editGroupCallback
    ){
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
                            editGroupCallback.onError(res.error.code, res.error.message);
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

    public void updateGroupAvatar(
            final String base64_code_ava,
            final String ext,
            final int group_id,
            final UpdateAvatarCallback updateAvatarCallback,
            final ErrorCallback errorCallback
    ){
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
                            errorCallback.onError(res.error.code, res.error.message);
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

    public void deleteGroup(
            final int id,
            final DeleteGroupCallback deleteGroupCallback,
            final ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.group_delete_group_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("delete_group", response);
                        if (response != null) {
                            UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                            if(res.error==null){
                                deleteGroupCallback.onSuccess();
                                return;
                            }
                            errorCallback.onError(res.error.code, res.error.message);
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
                params.put("id", Integer.toString(id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "groups.requestDeleteGroup");
    }

    public interface DeleteGroupCallback{
        void onSuccess();
    }

    public interface UpdateAvatarCallback{
        void onSuccess();
    }

    public interface GetGroupsCallback {
        public void onSuccess(com.httpso_hello.hello.Structures.Groups[] groupItem);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface GetOneGroupsCallback {
        public void onSuccess(OneGroup groupItem);
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
