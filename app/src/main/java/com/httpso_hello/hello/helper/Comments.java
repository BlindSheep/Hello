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
            final String targetController,
            final int targetId,
            final Activity activity,
            final GetCommentsCallback getCommentsCallback){
        Log.d("board", "Enter");
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.comments_get_comments_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("comments", response);
                        if (response != null) {
                            ReqComments comment = gson.fromJson(response, ReqComments.class);
                            if (comment.error==null){
                                setNewToken(comment.token);
                                getCommentsCallback.onSuccess(comment.comments, activity);
                                return;
                            }
                            getCommentsCallback.onError(comment.error.code, comment.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("targetController", targetController);
                params.put("targetId", Integer.toString(targetId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.getComments");
    }

    public void sendComments(
            final String targetController,
            final int targetId,
            final int idAns,
            final String content,
            final SendCommentsCallback sendCommentsCallback){
        Log.d("board", "Enter");
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.comments_add_comment_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("comments", response);
                        if (response != null) {
                            UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                            if (res.error==null){
                                setNewToken(res.token);
                                sendCommentsCallback.onSuccess(res.response);
                                return;
                            }
                            sendCommentsCallback.onError(res.error.code, res.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("targetId", Integer.toString(targetId));
                params.put("targetController", targetController);
                params.put("content", content);
                if (idAns != 0) params.put("parent_user_id", Integer.toString(idAns));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.sendComments");
    }

    public void getCountsComments(
            final String targetController,
            final int targetId,
            final GetCountsCommentsCallback getCountsCommentsCallback){
        Log.d("board", "Enter");
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.comments_get_counts_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("comments", response);
                        if (response != null) {
                            Counts count_comments = gson.fromJson(response, Counts.class);
                            setNewToken(count_comments.token);
                            getCountsCommentsCallback.onSuccess(count_comments.count);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("targetController", targetController);
                params.put("targetId", Integer.toString(targetId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.getComments");
    }

    public void deleteComments(
            final int id,
            final DeleteCommentCallback deleteCommentCallback){
        Log.d("board", "Enter");
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.comments_delete_comment_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("comments", response);
                        if (response != null) {
                            UniversalResponse res = gson.fromJson(response, UniversalResponse.class);
                            if (res.error==null){
                                setNewToken(res.token);
                                deleteCommentCallback.onSuccess();
                                return;
                            }
                            deleteCommentCallback.onError(res.error.code, res.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("id", Integer.toString(id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "comments.deleteComments");
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
