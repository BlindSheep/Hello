package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.AddFile;
//import com.httpso_hello.hello.Structures.Board;
//import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.Structures.Contacts;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Notices;
import com.httpso_hello.hello.Structures.RequestMessages;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.activity.MessagesActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 27.07.2017.
 */

public class Messages extends Help {

    private Settings stgs;
    private Context _context;
    private Activity activity;
    private Api api;
    private String device_id;
    private ArrayList<Integer> attachemts = new ArrayList<>();

    public Messages(Context context, Activity activity){
        super(context);
        this._context = context;
        stgs = new Settings(this._context);
        api = new Api(this._context);
        this.activity = activity;
        device_id = Integer.toString(stgs.getSettingInt("device_id"));
        if(device_id == "0"){
            device_id = null;
        }
    }

    public void deleteContacts(
            final int contactId,
            final Messages.DeleteContactsCallback deleteContactsCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.messages_delete_contact_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("deleteContacts", response);
                        if (response != null) {
                            Notices notises = gson.fromJson(response, Notices.class);
                            if(notises.error == null){
                                deleteContactsCallback.onSuccess();
                                return;
                            }
                            deleteContactsCallback.onError(notises.error.code, notises.error.message);
                            return;
                        }
                        deleteContactsCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deleteContactsCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contactId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.delete_contacts");
    }

    public void deleteMessage(
            final int message_id,
            final int from, // 1 - сообщение юзера, 0 - сообщение юзеру
            final Messages.DeleteMessageCallback deleteMessageCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.messages_delete_message_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("deleteMessage", response);
                        if (response != null) {
                            UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                            if(universalResponse.error == null){
                                deleteMessageCallback.onSuccess();
                                return;
                            }
                            deleteMessageCallback.onError(universalResponse.error.code, universalResponse.error.message);
                            return;
                        }
                        deleteMessageCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deleteMessageCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("message_id", Integer.toString(message_id));
                params.put("from", Integer.toString(from));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.delete_Message");
    }

    public void ignorContact(
            final int ignoredUserId,
            final Messages.IgnorContactCallback ignorContactCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.blacklist_add_ignore_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("ignorContact", response);
                        if (response != null) {
                            Notices notises = gson.fromJson(response, Notices.class);
                            if(notises.error == null){
                                setNewToken(notises.token);
                                ignorContactCallback.onSuccess();
                                return;
                            }
                            ignorContactCallback.onError(notises.error.code, notises.error.message);
                            return;
                        }
                        ignorContactCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ignorContactCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap(_context);
                params.put("ignoredUserId", Integer.toString(ignoredUserId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.ignor_contact");
    }

    public int getCountAttachments(){
        return attachemts.size();
    }

    public String addFileToMessage(
            final String type,
            final String ext,
            final String file_base64,
            final int position,
            final AddFileToMessageCallback addFileToMessageCallback,
            final Help.ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.messages_add_file_to_message_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("add_file_to_message", response);
                        if (response != null) {
                            AddFile addFile = gson.fromJson(response, AddFile.class);
                            if(addFile.error == null){
                                attachemts.add(addFile.id);
                                addFileToMessageCallback.onSuccess(addFile.response, addFile.id, position);
                                return;
                            }
                            errorCallback.onError(addFile.error.code, addFile.error.message);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("type", type);
                params.put("file_base64" , file_base64);
                params.put("ext" , ext);
                return params;
            };
        };
//            SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        String tag = "messages.addFileToMessage_" + Integer.toString(position);
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, tag);
        return tag;
    }

    public void deleteAttachment(final int nuber){
        try {
            RequestQ.getInstance(_context).cancelPendingRequests("messages.addFileToMessage_" + Integer.toString(nuber));
            this.attachemts.remove(nuber);

        } catch (Exception e){
        }
    }


    public interface AddFileToMessageCallback {
        void onSuccess(boolean response, final int id, final int position);
    }

    public interface DeleteContactsCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface DeleteMessageCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface IgnorContactCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}

