package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.*;

import java.util.Map;

/**
 * Created by mixir on 24.08.2017.
 */

public class Simpation extends Help {

    private Settings stgs;
    private Context _context;

    private static Simpation instance;

    public Simpation(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized  Simpation getInstance(Context context){
        if(instance == null){
            instance = new Simpation(context);
        }
        return instance;
    }

    public void getInfo(
            final String type,
            final Activity activity,
            final Simpation.GetSimpationCallback getSimpationCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.flirtiki_get_flirtik_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("flirtiki", response);
                        if (response != null) {
                            Flirtiki flirtiki = gson.fromJson(response, Flirtiki.class);
                            if(flirtiki.error==null){
                                getSimpationCallback.onSuccess(flirtiki.simpations, activity);
                                setNewToken(flirtiki.token);
                                return;
                            }
                            getSimpationCallback.onError(flirtiki.error.code, flirtiki.error.message);
                            return;
                        }
                        getSimpationCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getSimpationCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap(_context);
                params.put("type", type);
                params.put("page", Integer.toString(0));
                params.put("perPage", Integer.toString(30));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "flirtiki.getInfo");
    }

    public void sendFlirtik(
            final int toUserId,
            final Simpation.SendSimpationCallback sendSimpationCallback,
            final Help.ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.flirtiki_add_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("send_flirt", response);
                        if (response != null) {
                            FlirtikSend flirtikSend = gson.fromJson(response, FlirtikSend.class);
                            if(flirtikSend.error==null){
                                setNewToken(flirtikSend.token);
                                sendSimpationCallback.onSuccess(flirtikSend.response);
                                return;
                            }
                            errorCallback.onError(flirtikSend.error.code, flirtikSend.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("toUserId", Integer.toString(toUserId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "flirtiki.sendFlirt");
    }

    public interface GetSimpationCallback {
        public void onSuccess(ForUserOnly[] vz, Activity activity);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface SendSimpationCallback {
        public void onSuccess(String response);
    }
}
