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


public class Api {

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
    public Resp login(final String login, final String password, final LoginCallback callback){
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.auth_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null) {
                            callback.onSuccess(gson.fromJson(response, Resp.class));
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

    public void setPushUpToken(final String token){
        Log.d("PUSHUP", "!!!");
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.set_token_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null) {
                            Log.d("PUSHUP", response);
                            ReqSetToken reqSetToken = gson.fromJson(response, ReqSetToken.class);
                            if(reqSetToken.error == null){
                                stgs.setSettingInt("device_id", reqSetToken.device_id);
                                Log.d("PUSHUP", Integer.toString(stgs.getSettingInt("device_id")));
                                return;
                            }

                            // Кэллбэк для обработки полученного результата
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("push_up_token", token);
                return params;
            };


        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "auth.set_token");
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

    public void getContacts(final Activity activity, final GetContactsCallback getContactsCallback){
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.messages_get_contacts_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("contacts", response);
                        if(response!=null){
                            Contacts contacts;
                            try{
                                contacts = gson.fromJson(response, Contacts.class);
                            } catch (Exception e){
                                Logs.getInstance(_context).add(
                                        "error",
                                        response,
                                        "api_messages_get_contacts",
                                        e.toString()
                                );
                                contacts = new Contacts();
                                contacts.error = new Error();
                                contacts.error.message = "Произошла непредвиденая ошибка";
                                contacts.error.code = 998;
                            }

                            getContactsCallback.onSuccess(contacts, activity);
                            return;
                        }
                        getContactsCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getContactsCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.getContacts");
    }

    public void sendMessage(final String messageContent, final int contact_id, final ApiSendMessage apiSendMessage){
        StringRequest SReq = new StringRequest(
                Method.POST,
                Constant.messages_send_message_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null){
                            Debug.systemLog(response);
                            apiSendMessage.onSuccess(gson.fromJson(response, RequestMessages.class));
                            return;
                        }
                        apiSendMessage.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiSendMessage.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contact_id));
                params.put("messageContent", messageContent);
                return params;
            };
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.sendMessage_"+Integer.toString(contact_id));
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

    public interface ApiSendMessage{
        void onSuccess(RequestMessages requestMessages);
        void onInternetError();
    }

}

