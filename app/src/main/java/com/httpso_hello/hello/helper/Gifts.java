package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.GiftItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 03.11.2017.
 */

public class Gifts extends Help {

    private Settings stgs;
    private Context _context;

    private static Gifts instance;

    public Gifts(Context context) {
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Gifts getInstance(Context context) {
        if (instance == null) {
            instance = new Gifts(context);
        }
        return instance;
    }

    //Получения списка подарков
    public void getGifts(
            final int userId,
            final Gifts.GetGiftsCallback getGiftsCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.gifts_get_list_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("gifts", response);
                        if (response != null) {
                            com.httpso_hello.hello.Structures.Gifts gifts = gson.fromJson(response, com.httpso_hello.hello.Structures.Gifts.class);
                            if (gifts.error == null) {
                                setNewToken(gifts.token);
                                getGiftsCallback.onSuccess(gifts.gifts);
                                return;
                            }
                            getGiftsCallback.onError(gifts.error.code, gifts.error.message);
                            return;
                        }
                        getGiftsCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getGiftsCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =getParamsMap(_context);
                if (userId != 0) params.put("userId", Integer.toString(userId));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "gifts.get_gifts");
    }

    public void sendGifts(
            final int gift_id,
            final int to_id,
            final String text,
            final int isPrivate,
            final String paid_token,
            final int price,
            final Gifts.SendGiftsCallback sendGiftsCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.paid_services_paid_gift_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("gifts", response);
                        if (response != null) {
                            com.httpso_hello.hello.Structures.Gifts gifts = gson.fromJson(response, com.httpso_hello.hello.Structures.Gifts.class);
                            if (gifts.error == null) {
                                sendGiftsCallback.onSuccess();
                                return;
                            }
                            sendGiftsCallback.onError(gifts.error.code, gifts.error.message);
                            return;
                        }
                        sendGiftsCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendGiftsCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("gift_id", Integer.toString(gift_id));
                params.put("to_id", Integer.toString(to_id));
                params.put("text", text);
                params.put("private", Integer.toString(isPrivate));
                params.put("paid_token", paid_token);
                params.put("price", Integer.toString(price));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "gifts.send_gifts");
    }

    public interface GetGiftsCallback {
        void onSuccess(com.httpso_hello.hello.Structures.GiftItem[] gi);

        void onError(int error_code, String error_msg);

        void onInternetError();
    }

    public interface SendGiftsCallback {
        void onSuccess();

        void onError(int error_code, String error_msg);

        void onInternetError();
    }
}