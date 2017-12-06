package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.Structures.Vote;
import com.httpso_hello.hello.Structures.Votes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 06.12.2017.
 */

public class Complaint extends Help {

    private Settings stgs;
    private Context _context;

    private static Complaint instance;

    public Complaint(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized  Complaint getInstance(Context context){
        if(instance == null){
            instance = new Complaint(context);
        }
        return instance;
    }

    public void addComplaint(
            final String content,
            final String target_controller,
            final String target_subject,
            final int target_id,
            final Complaint.SendComplaintCallback sendComplaintCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.complaint_add_complaint_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("complaint", response);
                            if (response != null) {
                                UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                                if(universalResponse.error==null){
                                    sendComplaintCallback.onSuccess();
                                    return;
                                }
                                sendComplaintCallback.onError(universalResponse.error.error_code, universalResponse.error.error_msg);
                                return;
                            }
                            sendComplaintCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendComplaintCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("content", content);
                    params.put("target_controller", target_controller);
                    params.put("target_subject", target_subject);
                    params.put("target_id", Integer.toString(target_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "complaint.add_complaint");
        }
    }

    public interface SendComplaintCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
