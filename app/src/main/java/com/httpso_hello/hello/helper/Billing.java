package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.UniversalResponse;
import com.httpso_hello.hello.Structures.Vote;
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


    /*
    public void getToken(
            final String action,
            final Billing.GetTokenCallback getTokenCallback
    ) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.rating_send_like_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("like", response);
                            if (response != null) {
                                Votes votes = gson.fromJson(response, Votes.class);
                                if (votes.error == null) {
                                    getTokenCallback.onSuccess();
                                    return;
                                }
                                getTokenCallback.onError(votes.error.error_code, votes.error.error_msg);
                                return;
                            }
                            getTokenCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getTokenCallback.onInternetError();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    if(action!=null){
                        params.put("action", action);
                    }
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.send_like");
        }
    }
*/
    public void sendToken(
            final Billing.SendTokenCallback sendTokenCallback
    ) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.rating_send_like_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("like", response);
                            if (response != null) {
                                Votes votes = gson.fromJson(response, Votes.class);
                                if (votes.error == null) {
                                    sendTokenCallback.onSuccess();
                                    return;
                                }
                                sendTokenCallback.onError(votes.error.error_code, votes.error.error_msg);
                                return;
                            }
                            sendTokenCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendTokenCallback.onInternetError();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "rating.send_like");
        }
    }

    public void getRaisingToken(
            final String action,
            final Billing.GetRaisingTokenCallback getRaisingTokenCallback
    ) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.paid_services_raising_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("raising", response);
                            if (response != null) {
                                TokenReq tokenReq = gson.fromJson(response, TokenReq.class);
                                if (tokenReq.error == null) {
                                    getRaisingTokenCallback.onSuccess(tokenReq);
                                    return;
                                }
                                getRaisingTokenCallback.onError(tokenReq.error.error_code, tokenReq.error.error_msg);
                                return;
                            }
                            getRaisingTokenCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getRaisingTokenCallback.onInternetError();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    if(action!=null){
                        params.put("action", action);
                    }
                    return params;
                }

                ;
            };
            SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.getRaisingToken");
        }
    }

    public void paidRaising(final String paid_token,
            final Billing.PaidRaisingCallback paidRaisingCallback
    ) {
        Log.d("paid_raising", paid_token);
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.paid_services_paid_raising_uri ,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("paid_raising", response);
                            if (response != null) {
                                UniversalResponse resp = gson.fromJson(response, UniversalResponse.class);
                                if (resp.error == null) {
                                    paidRaisingCallback.onSuccess(resp.response);
                                    return;
                                }
                                paidRaisingCallback.onError(resp.error.error_code, resp.error.error_msg);
                                return;
                            }
                            paidRaisingCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            paidRaisingCallback.onInternetError();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("paid_token", paid_token);
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.getRaisingToken");
        }
    }

    public void addBalance(
            final int summa,
            final String token,
            final Billing.AddBalanceCallback addBalanceCallback,
            final ErrorCallback errorCallback
    ) {
        if (Constant.api_key != "") {
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
                                errorCallback.onError(tokenReq.error.error_code, tokenReq.error.error_msg);
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
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("action", "add_balance");
                    params.put("paid_token", token);
                    params.put("balance", Integer.toString(summa));
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.addBalance");
        }
    }

    public void paidViewGuests(
            final String paid_token,
            final RemovePointsCallback removePointsCallback,
            final ErrorCallback errorCallback) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.paid_services_remove_points_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("paid_view_g", response);
                            if (response != null) {
                                UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                                if (universalResponse.error == null) {
                                    removePointsCallback.onSuccess();
                                    return;
                                }
                                errorCallback.onError(universalResponse.error.error_code, universalResponse.error.error_msg);
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
                    Map<String, String> params = getParamsMap();
                    params.put("paid_token", paid_token);
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.paidViewGuests");
        }
    }

    public interface RemovePointsCallback {
        public void onSuccess();
    }

    public interface GetTokenCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface SendTokenCallback {
        public void onSuccess();
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetRaisingTokenCallback{
        void onSuccess(TokenReq token);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface PaidRaisingCallback {

        void onSuccess(Boolean response);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface AddBalanceCallback{
        void onSuccess();
    }
}