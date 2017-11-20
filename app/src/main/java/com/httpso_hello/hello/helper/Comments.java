package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Coment;
import com.httpso_hello.hello.Structures.Counts;
import com.httpso_hello.hello.Structures.ReqComments;
import com.httpso_hello.hello.Structures.UniversalResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 19.08.2017.
 */

public class Comments extends Help{

    private Settings stgs;
    private Context _context;

    public Comments(Context context){
        super();
        this._context = context;
        stgs = new Settings(_context);
    }

    public void getComments(
            final String controller,
            final String content_type,
            final int target_id,
            final Activity activity,
            final GetCommentsCallback getCommentsCallback){
        Log.d("board", "Enter");
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.comments_get_comments_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("comments", response);
                            if (response != null) {
                                ReqComments comment = gson.fromJson(response, ReqComments.class);
                                if (comment.error==null){
                                    getCommentsCallback.onSuccess(comment.comments, activity);
                                    return;
                                }
                                getCommentsCallback.onError(comment.error.error_code, comment.error.error_msg);
                                return;
                            }
                            getCommentsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getCommentsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("controller", controller);
                    params.put("content_type", content_type);
                    params.put("target_id", Integer.toString(target_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.getComments");
        }
    }

    public void sendComments(
            final String controller,
            final String content_type,
            final int target_id,
            final int idAns,
            final String content,
            final SendCommentsCallback sendCommentsCallback){
        Log.d("board", "Enter");
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.comments_send_comments_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("comments", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if (res.error==null){
                                    sendCommentsCallback.onSuccess(res.response);
                                    return;
                                }
                                sendCommentsCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            sendCommentsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendCommentsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("controller", controller);
                    params.put("content_type", content_type);
                    params.put("target_id", Integer.toString(target_id));
                    params.put("content", content);
                    if (idAns != 0) params.put("parent_user_id", Integer.toString(idAns));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.sendComments");
        }
    }

    public void getCountsComments(
            final String controller,
            final String content_type,
            final int target_id,
            final GetCountsCommentsCallback getCountsCommentsCallback){
        Log.d("board", "Enter");
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.comments_get_counts_comments_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("comments", response);
                            if (response != null) {
                                Counts count_comments = gson.fromJson(response, Counts.class);
                                    getCountsCommentsCallback.onSuccess(count_comments.count_comments);
                                    return;
                            }
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("controller", controller);
                    params.put("content_type", content_type);
                    params.put("target_id", Integer.toString(target_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.getComments");
        }
    }

    public void deleteComments(
            final int id,
            final String target_controller,
            final String content_type,
            final int target_id,
            final DeleteCommentCallback deleteCommentCallback){
        Log.d("board", "Enter");
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.comments_delete_comments_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("comments", response);
                            if (response != null) {
                                UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                                if (res.error==null){
                                    deleteCommentCallback.onSuccess();
                                    return;
                                }
                                deleteCommentCallback.onError(res.error.error_code, res.error.error_msg);
                                return;
                            }
                            deleteCommentCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            deleteCommentCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("id", Integer.toString(id));
                    params.put("target_controller", target_controller);
                    params.put("content_type", content_type);
                    params.put("target_id", Integer.toString(target_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.deleteComments");
        }
    }

    public interface GetCommentsCallback{
        void onSuccess(Coment[] comments, Activity activity);
        void onError(int error_code, String error_message);
        void onInternetError();
    }

    public interface SendCommentsCallback{
        void onSuccess(boolean response);
        void onError(int error_code, String error_message);
        void onInternetError();
    }

    public interface GetCountsCommentsCallback{
        void onSuccess(int count_comments);
    }

    public interface DeleteCommentCallback{
        void onSuccess();
        void onError(int error_code, String error_message);
        void onInternetError();
    }
}
