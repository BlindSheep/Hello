package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.Structures.Votes;
import com.httpso_hello.hello.helper.push_services.TokenReq;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 03.09.2017.
 */

public class Billing extends Help {

    private Settings stgs;
    private Context _context;

    private static Billing instance;

    public Billing(Context context) {
        super(context);
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Billing getInstance(Context context) {
        if (instance == null) {
            instance = new Billing(context);
        }
        return instance;
    }

    public void addBalance(
            final int summa,
            final String token,
            final Billing.AddBalanceCallback addBalanceCallback,
            final ErrorCallback errorCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.paid_services_add_balance_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("add_balance", response);
                        if (response != null) {
                            TokenReq tokenReq = gson.fromJson(response, TokenReq.class);
                            if (tokenReq.error == null) {
                                addBalanceCallback.onSuccess();
                                return;
                            }
                            errorCallback.onError(tokenReq.error.code, tokenReq.error.message);
                            return;
                        }
                        errorCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("action", "add_balance");
                params.put("paid_token", token);
                params.put("balance", Integer.toString(summa));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.addBalance");
    }

    public void pay(
            final String reason,
            final PayCallback payCallback) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.billing_pay_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("paid_view_g", response);
                        if (response != null) {
                            UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                            if (universalResponse.error == null) {
                                setNewToken(universalResponse.token);
                                payCallback.onSuccess();
                                return;
                            }
                            payCallback.onError(universalResponse.error.code, universalResponse.error.message);
                            return;
                        }
                        payCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        payCallback.onInternetError();
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
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.paidViewGuests");
    }

    public interface AddBalanceCallback{
        void onSuccess();
    }
    public interface PayCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}