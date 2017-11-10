package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.*;

import java.util.HashMap;
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
            final Activity activity,
            final Simpation.GetSimpationCallback getSimpationCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.flirtiki_get_info_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("flirtiki", response);
                            if (response != null) {
                                Flirtiki flirtiki = gson.fromJson(response, Flirtiki.class);
                                if(flirtiki.error==null){
                                    getSimpationCallback.onSuccess(flirtiki.flirtiki, flirtiki.youLike, flirtiki.whoYouLike, activity);
                                    return;
                                }
                                getSimpationCallback.onError(flirtiki.error.error_code, flirtiki.error.error_msg);
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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "flirtiki.getInfo");
        }
    }

    public void sendFlirtik(
            final int profile_id,
            final Simpation.SendSimpationCallback sendSimpationCallback,
            final Help.ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.flirtiki_send_flirtik_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("send_flirt", response);
                            if (response != null) {
                                FlirtikSend flirtikSend = gson.fromJson(response, FlirtikSend.class);
                                if(flirtikSend.error==null){
                                    sendSimpationCallback.onSuccess(flirtikSend.response);
                                    return;
                                }
                                errorCallback.onError(flirtikSend.error.error_code, flirtikSend.error.error_msg);
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
                    params.put("profile_id", Integer.toString(profile_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "flirtiki.sendFlirt");
        }
    }

    public void getFlirtList(
            final int city_id,
            final int from, // Возраст от
            final int to, // Возраст до
            final Simpation.GetFlirtListCallback getFlirtListCallback,
            final Help.ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.flirtiki_send_flirtik_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("flirt_list", response);
                            if (response != null) {
                                GetFlirtListResponse getFlirtListResponse = gson.fromJson(response, GetFlirtListResponse.class);
                                if(getFlirtListResponse.error==null){
                                    getFlirtListCallback.onSuccess(getFlirtListResponse.users);
                                    return;
                                }
                                errorCallback.onError(getFlirtListResponse.error.error_code, getFlirtListResponse.error.error_msg);
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
                    if(city_id != 0)
                        params.put("city_id", Integer.toString(city_id));
                    if(from != 0)
                        params.put("from", Integer.toString(from));
                    if(to != 0)
                        params.put("to", Integer.toString(to));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "flirtiki.getFlirtList");
        }
    }

    public interface GetSimpationCallback {
        public void onSuccess(FlirtikItem[] vz, FlirtikItem[] vam, FlirtikItem[] vi, Activity activity);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface SendSimpationCallback {
        public void onSuccess(String response);
    }
    public interface GetFlirtListCallback{
        void onSuccess(User[] users);
    }

}
