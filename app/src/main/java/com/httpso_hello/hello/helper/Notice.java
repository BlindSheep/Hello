package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.Structures.Notices;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 24.08.2017.
 */

public class Notice extends Help {

    private Settings stgs;
    private Context _context;

    private static Notice instance;

    public Notice(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Notice getInstance(Context context){
        if(instance == null){
            instance = new Notice(context);
        }
        return instance;
    }

    public void getNotice(
            final Activity activity,
            final Notice.GetNoticeCallback getNoticeCallback,
            final Help.ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.messages_get_notices_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("notices", response);
                            if (response != null) {
                                Notices notises = gson.fromJson(response, Notices.class);
                                if(notises.error == null){
                                    getNoticeCallback.onSuccess(notises.notices, activity);
                                    return;
                                }
                                errorCallback.onError(notises.error.error_code, notises.error.error_msg);
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
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "messages.getNotices");
        }
    }

public interface GetNoticeCallback {
    void onSuccess(NoticeItem[] noticeItem, Activity activity);
}

}
