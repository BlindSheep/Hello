package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 25.08.2017.
 */

public class Photo extends Help {

    private Settings stgs;
    private Context _context;

    private static Photo instance;

    Photo(Context context){
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Photo getInstance(Context context){
        if(instance == null){
            instance = new Photo(context);
        }
        return instance;
    }

    // Загрузка фотографии
    //Загрузка аватарки
    public void addPhoto(
            final String file_base64,
            final String ext,
            final AddPhotoCallback addPhotoCallback,
            final ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.photos_add_photo,
                new Response.Listener<String>() {
                    public void onResponse(String response){

                        if(response!=null){
                            Log.d("add_photo", response);
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
                params.put("ext", ext);
                params.put("file_base64", file_base64);
                return params;
            };
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.update_avatar");
    }

    public void deletePhoto(
            final int photoId,
            final DeletePhotoCalback deletePhotoCalback,
            final Help.ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_remove_photo,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null){
                            Log.d("delete_photo", response);
                            deletePhotoCalback.onSuccess();
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
                params.put("photoId", Integer.toString(photoId));
                return params;
            };
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.update_avatar");
    }

    public interface AddPhotoCallback {
        void onSuccess();
    }
    public interface DeletePhotoCalback{
        void onSuccess();
    }
}
