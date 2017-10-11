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
import com.httpso_hello.hello.Structures.AddFileToMessage;
//import com.httpso_hello.hello.Structures.Board;
//import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.Structures.Contacts;
import com.httpso_hello.hello.Structures.Message;
import com.httpso_hello.hello.Structures.Notices;
import com.httpso_hello.hello.Structures.RequestMessages;
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
        this._context = context;
        stgs = new Settings(this._context);
        api = new Api(this._context);
        this.activity = activity;
        device_id = Integer.toString(stgs.getSettingInt("device_id"));
        if(device_id == "0"){
            device_id = null;
        }
    }

    public void getContacts(final Messages.GetContactsCallback getContactsCallback){
        api.getContacts(this.activity, new Api.GetContactsCallback() {
            @Override
            public void onSuccess(Contacts contacts, Activity activity) {
                if(contacts.error==null) {
                    getContactsCallback.onSuccess(
                            contacts.contact,
                            (MessagesActivity)activity,
                            contacts.dateLastUpdate);
                    return;
                }
                getContactsCallback.onError(contacts.error.error_code, contacts.error.error_msg);
            }
            @Override
            public void onInternetError(){
                // TODO: 28.07.2017 Сделать локализацию
                getContactsCallback.onInternetError("Ошибка интернет соединения");
            }
        });
    }

    public void refreshContacts(final String dateLastUpdate, final RefreshContactsCallback refreshContactsCallback, final ErrorCallback errorCallback){
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_refresh_contacts_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("refresh_contacts", response);
                            if (response != null) {
                                Contacts contacts = gson.fromJson(response, Contacts.class);
                                if(contacts.error == null){
                                    refreshContactsCallback.onSuccess(contacts.contact, contacts.dateLastUpdate);
                                    return;
                                }
                                errorCallback.onError(contacts.error.error_code, contacts.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("dateLastUpdate", dateLastUpdate);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.refresh_contacts");
        }
    }
    public void refreshMessages(
            final String dateLastUpdate,
            final int contact_id,
            final RefreshMessagesCallback refreshMessagesCallback,
            final ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_refresh_messages_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("refresh_messages", response);
                            if (response != null) {
                                RequestMessages requestMessages = gson.fromJson(response, RequestMessages.class);
                                if(requestMessages.error == null){
                                    refreshMessagesCallback.onSuccess(requestMessages.messages, requestMessages.dateLastUpdate, requestMessages.contact_is_online);
                                    return;
                                }
                                errorCallback.onError(requestMessages.error.error_code, requestMessages.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("dateLastUpdate", dateLastUpdate);
                    params.put("contact_id", Integer.toString(contact_id));
                    // Если есть device_id отправляем его для устранения дубляжа сообщений
                    if(device_id != null){
                        params.put("device_id", device_id);
                    }
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.refresh_contacts");
        }
    }
    public void deleteContacts(
            final int contactId,
            final Messages.DeleteContactsCallback deleteContactsCallback
    ){
        if (Constant.api_key !="") {
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
                                deleteContactsCallback.onError(notises.error.error_code, notises.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("contact_id", Integer.toString(contactId));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.delete_contacts");
        }
    }

    public void getMessages(
            final int contact_id,
            final Messages.GetMessagesCallback getMessagesCallback,
            final Help.ErrorCallback errorCallback
    ){
        /*api.getMessages(contact_id, activity, new Api.GetMessagesCallback() {
            @Override
            public void onSuccess(RequestMessages messages, Activity activity) {
                if(messages.error==null){
                    getMessagesCallback.onSuccess(messages.messages, activity,
                            stgs.getSettingInt("user_id"),
                            Constant.host + messages.userAvatar.micro,
                            messages.sendedUnreadedMessagesIDs,
                            messages.dateLastUpdate,
                            messages.contact_is_online
                    );
                    return;
                }
            }

            @Override
            public void onInternetError() {

            }
        });*/
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_get_messages_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("messages", response);
                            if(response!=null){
                                RequestMessages requestMessages = gson.fromJson(response, RequestMessages.class);
                                if(requestMessages.error == null){
                                    getMessagesCallback.onSuccess(
                                            requestMessages.messages,
                                            activity,
                                            stgs.getSettingInt("user_id"),
                                            Constant.host + requestMessages.userAvatar.micro,
                                            requestMessages.sendedUnreadedMessagesIDs,
                                            requestMessages.dateLastUpdate,
                                            requestMessages.contact_is_online
                                    );
                                    return;
                                }
                                errorCallback.onError(requestMessages.error.error_code, requestMessages.error.error_msg);
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
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("contact_id", Integer.toString(contact_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.getMessages_"+Integer.toString(contact_id));
        }
    }

    public void sendMessage(
            final int contact_id,
            final String messageContent,
            final int message_number,
            final Messages.MessagesSendMessage messagesSendMessage,
            final ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_send_message_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("send_message", response);
                            if(response!=null){
                                attachemts.clear();
                                RequestMessages requestMessages = gson.fromJson(response, RequestMessages.class);
                                if(requestMessages.error==null){
                                    messagesSendMessage.onSuccess(
                                            requestMessages.messages[0],
                                            requestMessages.dateLastUpdate,
                                            message_number);
                                    return;
                                }
                                errorCallback.onError(requestMessages.error.error_code, requestMessages.error.error_msg);
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
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("contact_id", Integer.toString(contact_id));
                    if(messageContent!=null)
                        params.put("messageContent", messageContent);
                    if(attachemts.size()!=0)
                        params.put("attachments", gson.toJson(attachemts.toArray()));
                    if(device_id != null){
                        params.put("device_id", device_id);
                    }
                    return params;
                };
            };
            SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.sendMessage_"+Integer.toString(contact_id));
        }
    }

    public int getCountAttachments(){
        return attachemts.size();
    }

    public void getStateMessages(
            final ArrayList<Message> messagesIDs,
            final GetStateMessagesCallback getStateMessagesCallback,
            final ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_get_state_messages_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("getStateMessages", response);
                            if (response != null) {
                                RequestMessages requestMessages = gson.fromJson(response, RequestMessages.class);
                                if(requestMessages.error == null){
                                    getStateMessagesCallback.onSuccess(requestMessages.messages);
                                    return;
                                }
                                errorCallback.onError(requestMessages.error.error_code, requestMessages.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("messagesIDs", gson.toJson(messagesIDs.toArray()));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.delete_contacts");
        }
    }


    public void addFileToMessage(
            final String type,
            final String ext,
            final String file_base64,
            final int position,
            final AddFileToMessageCallback addFileToMessageCallback,
            final Help.ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_add_file_to_message_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("add_file_to_message", response);
                            if (response != null) {
                                AddFileToMessage addFileToMessage = gson.fromJson(response, AddFileToMessage.class);
                                if(addFileToMessage.error == null){
                                    attachemts.add(addFileToMessage.id);
                                    addFileToMessageCallback.onSuccess(addFileToMessage.response, addFileToMessage.id, position);
                                    return;
                                }
                                errorCallback.onError(addFileToMessage.error.error_code, addFileToMessage.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("type", type);
                    params.put("file_base64" , file_base64);
                    params.put("ext" , ext);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.delete_contacts");
        }
    }

    public void deleteAttachment(int nuber, int id){
        if(this.attachemts.get(nuber) == id){
            this.attachemts.remove(nuber);
        }

    }

    public interface AddFileToMessageCallback {
        void onSuccess(boolean response, final int id, final int position);
    }

    public interface GetContactsCallback {
        void onSuccess(Contact contact[], MessagesActivity activity, String dateLastUpdate);
        void onError(int error_code, String error_msg);
        void onInternetError(String error_msg);
    }

    public interface GetStateMessagesCallback{
        void onSuccess(Message[] messagesIDs);
    }

    public interface RefreshContactsCallback {
        void onSuccess(Contact contact[], String dateLastUpdate);
    }

    public interface DeleteContactsCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface  GetMessagesCallback{
        void onSuccess(Message messages[],
                       Activity activity,
                       int user_id,
                       String user_avatar_micro,
                       Message[] sendedUnreadedMessagesIDs,
                       String dateLastUpdate,
                       boolean contactIsOnline
        );
//        void onError(int error_code, String error_msg);
//        void onInternetError();
    }

    public interface MessagesSendMessage{
        void onSuccess(Message message, String dateLastUpdate, int message_number);
    }
    public interface RefreshMessagesCallback{
        void onSuccess(Message[] messages, String dateLastUpdate, boolean contactIsOnline);
    }
}

