package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Resp;

import java.util.Map;

/**
 * Created by Общий on 13.11.2017.
 */

public class Logs extends Help{

    private Context _context;

    public Logs(Context context){
        super(context);
        this._context = context;
    }
    private static Logs instance;

    public static synchronized Logs getInstance(Context context){
        if(instance==null){
            instance = new Logs(context);
        }
        return instance;
    }
    public void add(
            final String type,
            final String data,
            final String caption,
            final String exception
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.logs_add_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            /*if (response != null) {
                                try {
                                    if (resp.error == null) {
                                        getSmallUserInfoCallback.onSuccess(resp.user_info);
                                        return;
                                    }
                                    errorCallback.onError(resp.error.error_code, resp.error.error_msg);
                                    return;
                                } catch (Exception e) {

                                }
                            }
                            errorCallback.onInternetError();*/
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            errorCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    if(type!=null)
                        params.put("type", type);
                    if(data!=null)
                        params.put("data",data);
                    if(caption!=null)
                        params.put("caption", caption);
                    if(exception != null)
                        params.put("exception", exception);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "logs.add");
        }
    }
    public interface AddCallback{
        void onSuccess();
    }
}
