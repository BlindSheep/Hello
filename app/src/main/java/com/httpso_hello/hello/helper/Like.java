package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.Structures.Votes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 20.08.2017.
 */

public class Like extends Help {

    private Settings stgs;
    private Context _context;

    private static Like instance;

    public Like(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized  Like getInstance(Context context){
        if(instance == null){
            instance = new Like(context);
        }
        return instance;
    }

    public void getInfo(
            final Activity activity,
            final int target_id,
            final String subject,
            final String target_controller,
            final GetInfoCallback getInfoCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.rating_get_info_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                Votes votes = gson.fromJson(response, Votes.class);
                                if(votes.error==null){
                                    getInfoCallback.onSuccess(votes.votes, activity);
                                    return;
                                }
                                getInfoCallback.onError(votes.error.error_code, votes.error.error_msg);
                                return;
                            }
                            getInfoCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getInfoCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("target_id", Integer.toString(target_id));
                    params.put("subject", subject);
                    params.put("target_controller", target_controller);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.getInfo");
        }
    }

    public void sendLike(
            final int target_id,
            final String direction,
            final String content_type,
            final String target_controller,
            final SendLikeCallback sendLikeCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.rating_send_like_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("like", response);
                            if (response != null) {
                                Votes votes = gson.fromJson(response, Votes.class);
                                if(votes.error==null){
                                    sendLikeCallback.onSuccess();
                                    return;
                                }
                                sendLikeCallback.onError(votes.error.error_code, votes.error.error_msg);
                                return;
                            }
                            sendLikeCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendLikeCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("direction", direction);
                    params.put("target_id", Integer.toString(target_id));
                    params.put("content_type", content_type);
                    params.put("target_controller", target_controller);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.send_like");
        }
    }

    public interface GetInfoCallback {
        public void onSuccess(Vote[] votes, Activity activity);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface SendLikeCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

}
