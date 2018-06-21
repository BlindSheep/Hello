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

    public Messages(Context context){
        super(context);
        this._context = context;
        stgs = new Settings(this._context);
        api = new Api(this._context);
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
                getContactsCallback.onError(contacts.error.code, contacts.error.message);
            }
            @Override
            public void onInternetError(){
                // TODO: 28.07.2017 Сделать локализацию
                getContactsCallback.onInternetError("Ошибка интернет соединения");
            }
        });
    }

    public void refreshContacts(final String dateLastUpdate, final RefreshContactsCallback refreshContactsCallback, final ErrorCallback errorCallback){
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
                            errorCallback.onError(contacts.error.code, contacts.error.message);
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
                params.put("dateLastUpdate", dateLastUpdate);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.refresh_contacts");
    }

    public void refreshMessages(
            final String dateLastUpdate,
            final int contact_id,
            final boolean writing,
            final RefreshMessagesCallback refreshMessagesCallback,
            final ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.messages_refresh_messages_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("refresh_messages", response);
                        if (response != null) {
                            RequestMessages requestMessages = gson.fromJson(response, RequestMessages.class);
                            if(requestMessages.error == null){
                                refreshMessagesCallback.onSuccess(requestMessages.messages, requestMessages.dateLastUpdate, requestMessages.contact_is_online, requestMessages.is_writing);
                                return;
                            }
                            errorCallback.onError(requestMessages.error.code, requestMessages.error.message);
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
                params.put("dateLastUpdate", dateLastUpdate);
                params.put("contact_id", Integer.toString(contact_id));
                if (writing) params.put("is_writing", Boolean.toString(writing));
                // Если есть device_id отправляем его для устранения дубляжа сообщений
                if(device_id != null){
                    params.put("device_id", device_id);
                }
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.refresh_contacts");
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
            final int contactId,
            final Messages.IgnorContactCallback ignorContactCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_ignor_contact_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("ignorContact", response);
                        if (response != null) {
                            Notices notises = gson.fromJson(response, Notices.class);
                            if(notises.error == null){
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contactId));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.ignor_contact");
    }

    public void getMessages(
            final int contact_id,
            final int page,
            final Messages.GetMessagesCallback getMessagesCallback,
            final Help.ErrorCallback errorCallback
    ){
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
                            errorCallback.onError(requestMessages.error.code, requestMessages.error.message);
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
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contact_id));
                params.put("last_viewed_id", Integer.toString(page));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.getMessages_"+Integer.toString(contact_id));
    }

    public void sendMessage(
            final int contact_id,
            final String messageContent,
            final int message_number,
            final long deviceMessageId,
            final Messages.MessagesSendMessage messagesSendMessage,
            final ErrorCallback errorCallback
    ){
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
                            errorCallback.onError(requestMessages.error.code, requestMessages.error.message);
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
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contact_id));
                if(messageContent!=null)
                    params.put("messageContent", messageContent);
                if(attachemts.size()!=0)
                    params.put("attachments", gson.toJson(attachemts.toArray()));
                if(device_id != null){
                    params.put("device_id", device_id);
                }
                params.put("device_message_id", Long.toString(deviceMessageId));
                return params;
            };
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.sendMessage_"+Integer.toString(contact_id));
    }

    public int getCountAttachments(){
        return attachemts.size();
    }

    /**
     * Запрос проверки прочитанности отправленных сообщений. Работает индивидуально по сообщениям
     * @param messagesIDs
     * @param getStateMessagesCallback
     * @param errorCallback
     */

    public void getStateMessages(
            final ArrayList<Message> messagesIDs,
            final GetStateMessagesCallback getStateMessagesCallback,
            final ErrorCallback errorCallback
    ){
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
                            errorCallback.onError(requestMessages.error.code, requestMessages.error.message);
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
                params.put("messagesIDs", gson.toJson(messagesIDs.toArray()));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.get_state_messages");
    }


    /**
     * Получение статуса прочитанности последних оптравленных сообщений. Работает по последнему
     * отправленному сообщению
     * @param contact_id
     * @param getReadStateMessages
     * @param errorCallback
     */
    public void getReadStateMessages(
            final int contact_id,
            final GetReadStateMessages getReadStateMessages,
            final ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.messages_get_read_state_messages_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("getStateMessages", response);
                        if (response != null) {
                            UniversalResponse state = gson.fromJson(response, UniversalResponse.class);
                            if(state.error == null){
                                getReadStateMessages.onSuccess(state.response);
                                return;
                            }
                            errorCallback.onError(state.error.code, state.error.message);
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
                Map<String, String> params = getParamsMap();
                params.put("contact_id", Integer.toString(contact_id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.get_read_state_messages");
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

    public interface  GetMessagesCallback{
        void onSuccess(Message messages[],
                       Activity activity,
                       int user_id,
                       String user_avatar_micro,
                       Message[] sendedUnreadedMessagesIDs,
                       String dateLastUpdate,
                       boolean contactIsOnline
        );
    }

    public interface MessagesSendMessage{
        void onSuccess(Message message, String dateLastUpdate, int message_number);
    }
    public interface RefreshMessagesCallback{
        void onSuccess(Message[] messages, String dateLastUpdate, boolean contactIsOnline, boolean contact_is_writing);
    }
    public interface GetReadStateMessages{
        void onSuccess(boolean state);
    }
}

