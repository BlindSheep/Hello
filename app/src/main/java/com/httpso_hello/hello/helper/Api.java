package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 24.07.2017.
 */

//Сторонние библиотеки

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.Structures.Contacts;
import com.httpso_hello.hello.Structures.Error;
import com.httpso_hello.hello.Structures.Registration;
import com.httpso_hello.hello.Structures.RequestMessages;
import com.httpso_hello.hello.Structures.ReqSetToken;
import com.httpso_hello.hello.Structures.Resp;
import com.httpso_hello.hello.Structures.UniversalResponse;


public class Api extends Help {

    private Context _context;
    private Settings stgs;

    private Gson gson;

    private Resp result;

    Api(Context context){
        this._context = context;
        GsonBuilder GB = new GsonBuilder();
        this.gson = GB.create();
        stgs = new Settings(context);
    }

    /**
     * Авторизация
     * NewAPI
     * @param login
     * @param password
     * @param callback
     * @return
     */
    public Resp login(final String login, final String password, final String token, final LoginCallback callback){
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.auth_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null) {
                            UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                            callback.onSuccess(universalResponse.data);
                            return;
                        }
                        callback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);
                params.put("pushUpToken", token);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "auth.login");
        return this.result;
    }


    //Регистрация юзера
    public void registration(final String telefone, final String password, final String nickname, final RegistrationCallback callback) {
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.registr_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null) {
                            Debug.systemLog(response);
                            callback.onSuccess(gson.fromJson(response, Registration.class));
                            return;
                        }
                        callback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error");
                        callback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", nickname);
                params.put("phone", telefone);
                params.put("password", password);
                params.put("secondPassword", password);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "auth.signup");
    }

    public void logout(final LogoutCallback logoutCallback){
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.logout_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null) {
                            logoutCallback.onSuccess();
                            return;
                        }
                        logoutCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error");
                        logoutCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "auth.logout");
    }


    public interface LoginCallback {
        void onSuccess(Resp result);
        void onInternetError();
    }

    public interface RegistrationCallback {
        void onSuccess(Registration registration);
        void onInternetError();
    }

    public interface LogoutCallback{
        void onSuccess();
        void onInternetError();
    }

    public interface GetContactsCallback {
        void onSuccess(Contacts contacts, Activity activity);
        void onInternetError();
    }
}

