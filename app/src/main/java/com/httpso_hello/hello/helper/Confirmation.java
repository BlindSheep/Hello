package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by mixir on 03.11.2017.
 */

public class Confirmation extends Help {

    private Settings stgs;
    private Context _context;

    private static Confirmation instance;

    public Confirmation(Context context) {
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Confirmation getInstance(Context context) {
        if (instance == null) {
            instance = new Confirmation(context);
        }
        return instance;
    }

    public void startConfirm(
            final String reason,
            final Confirmation.StartConfirmCallback startConfirmCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.confirmation_start_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("startConfirm", response);
                        if (response != null) {
                            com.httpso_hello.hello.Structures.Gifts gifts = gson.fromJson(response, com.httpso_hello.hello.Structures.Gifts.class);
                            if (gifts.error == null) {
                                startConfirmCallback.onSuccess();
                                return;
                            }
                            startConfirmCallback.onError(gifts.error.code, gifts.error.message);
                            return;
                        }
                        startConfirmCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startConfirmCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap(_context);
                params.put("reason", reason);
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "startConfirm");
    }

    public void endConfirm(
            final String password,
            final String reason,
            final Confirmation.EndConfirmCallback endConfirmCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.confirmation_finish_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("endConfirm", response);
                        if (response != null) {
                            com.httpso_hello.hello.Structures.Gifts gifts = gson.fromJson(response, com.httpso_hello.hello.Structures.Gifts.class);
                            if (gifts.error == null) {
                                endConfirmCallback.onSuccess();
                                return;
                            }
                            endConfirmCallback.onError(gifts.error.code, gifts.error.message);
                            return;
                        }
                        endConfirmCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endConfirmCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap(_context);
                params.put("password", password);
                params.put("reason", reason);
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "endConfirm");
    }

    public interface StartConfirmCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface EndConfirmCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}