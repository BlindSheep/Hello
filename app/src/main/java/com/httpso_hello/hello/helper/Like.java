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
            final int targetId,
            final String targetController,
            final GetInfoCallback getInfoCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.rating_get_votes_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            Votes votes = gson.fromJson(response, Votes.class);
                            if(votes.error==null){
                                setNewToken(votes.token);
                                getInfoCallback.onSuccess(votes.votes, activity);
                                return;
                            }
                            getInfoCallback.onError(votes.error.code, votes.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("targetId", Integer.toString(targetId));
                params.put("targetController", targetController);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.getInfo");
    }

    public void sendLike(
            final int targetId,
            final String direction,
            final String targetController,
            final SendLikeCallback sendLikeCallback
    ){
        String uri = "";
        if (direction.equals("up")) uri = Constant.rating_up_uri;
        else if (direction.equals("down")) uri = Constant.rating_down_uri;
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("like", response);
                        if (response != null) {
                            Votes votes = gson.fromJson(response, Votes.class);
                            if(votes.error==null){
                                setNewToken(votes.token);
                                sendLikeCallback.onSuccess();
                                return;
                            }
                            sendLikeCallback.onError(votes.error.code, votes.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("targetId", Integer.toString(targetId));
                params.put("targetController", targetController);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.send_like");
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
