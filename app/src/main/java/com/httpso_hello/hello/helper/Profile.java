package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.AllCounts;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.Structures.Guests;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.ReqUpdateAvatar;
import com.httpso_hello.hello.Structures.Resp;
import com.httpso_hello.hello.Structures.SearchProfiles;
import com.httpso_hello.hello.Structures.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 13.08.2017.
 */

public class Profile extends Help{

    private Settings stgs;
    private Context _context;

    private static Profile instance;

    public static synchronized Profile getInstance(Context context){
        if(instance==null){
            instance = new Profile(context);
        }
        return instance;
    }

    public Profile(Context context) {
        super();
        this._context = context;
        this.stgs = new Settings(context);
    }
    public void getProfile(final int profile_id, final Activity activity, final GetProfileCallback getProfileCallback){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_get_profile_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if (response != null) {
                                try {
                                    Log.d("User", response);
                                    Resp resp = gson.fromJson(response, Resp.class);
                                    if (resp.error == null) {
                                        getProfileCallback.onSuccess(resp.user_info, activity);
                                        return;
                                    }
                                    getProfileCallback.onError(resp.error.error_code, resp.error.error_msg);
                                    return;
                                } catch (Exception e) {

                                }
                            }
                            getProfileCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getProfileCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("profile_id", Integer.toString(profile_id));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getProfile"+Integer.toString(profile_id));
        }
    }

    public void editProfile(final String new_user_info, final EditProfileCallback editProfileCallback){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_edit_profile_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if (response != null) {
                                Log.d("User", response);
                                Resp resp = gson.fromJson(response, Resp.class);
                                if (resp.error == null) {
                                    editProfileCallback.onSuccess();
                                    return;
                                }
                                editProfileCallback.onError(resp.error.error_code, resp.error.error_msg);
                                return;
                            }
                            editProfileCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            editProfileCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("new_user_info", new_user_info);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.edutProfile");
        }
    }

    public void searchProfiles(final int ageFrom, final int ageTo, final int gender, final int page, final SearchProfilesCallback searchProfilesCallback){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_search_profiles_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if(response!=null){
                                Log.d("sP", response);
                                SearchProfiles searchProfiles = gson.fromJson(response, SearchProfiles.class);
                                if(searchProfiles.error==null) {
                                    searchProfilesCallback.onSuccess(searchProfiles.profiles_list);
                                    return;
                                }
                                searchProfilesCallback.onError(searchProfiles.error.error_code, searchProfiles.error.error_msg);
                                return;
                            }
                            searchProfilesCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            searchProfilesCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("birth_date[from]", Integer.toString(ageFrom));
                    params.put("birth_date[to]", Integer.toString(ageTo));
                    //Если выбраны и парни и девушки
                    if (gender != 0) params.put("gender", Integer.toString(gender));
                    //
                    params.put("page", Integer.toString(page));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.searchProfiles");
        }
    }

    public void getGuests(final int page, final Activity activity, final GetGuestsCallback getGuestsCallback){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.guests_get_guests_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if(response!=null){
                                Log.d("guests", response);
                                Guests guests = gson.fromJson(response, Guests.class);
                                if(guests.error==null) {
                                    getGuestsCallback.onSuccess(guests.guests, activity);
                                    return;
                                }
                                getGuestsCallback.onError(guests.error.error_code, guests.error.error_msg);
                                return;
                            }
                            getGuestsCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getGuestsCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("page", Integer.toString(page));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getGuests");
        }
    }

    public void getBalance(
            final GetBalanceCallback getBalanceCallback
    ) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_get_balance_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("balance", response);
                            BalanceReq balanceReq = gson.fromJson(response, BalanceReq.class);
                            if (response != null) {

                                if (balanceReq.error == null) {
                                    getBalanceCallback.onSuccess(balanceReq);
                                    return;
                                }
                                getBalanceCallback.onError(balanceReq.error.error_code, balanceReq.error.error_msg);
                                return;
                            }
                            getBalanceCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getBalanceCallback.onInternetError();
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
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.getPoints");
        }
    }

    public void getCount(
            final GetCountCallback getCountCallback
    ) {
        if (Constant.api_key != "") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_get_counts_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.d("users_get_counts", response);
                            AllCounts allCounts = gson.fromJson(response, AllCounts.class);
                            if (response != null) {
                                if (allCounts.error == null) {
                                    getCountCallback.onSuccess(allCounts);
                                    return;
                                }
                                getCountCallback.onError(allCounts.error.error_code, allCounts.error.error_msg);
                                return;
                            }
                            getCountCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getCountCallback.onInternetError();
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
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users_get_counts");
        }
    }

    //Загрузка аватарки
    public void updateAvatar(
            final String base64_code_ava,
            final String ext,
            final UpdateAvatarCallback updateAvatarCallback,
            final ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.users_update_avatar,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("update_avatar", response);
                            if(response!=null){
                                ReqUpdateAvatar reqUpdateAvatar = gson.fromJson(response, ReqUpdateAvatar.class);
                                if(reqUpdateAvatar.error==null) {
                                    stgs.setSetting("user_avatar.micro", Constant.upload + reqUpdateAvatar.avatar.micro);
                                    //Picasso
                                    //        .with(_context)
                                    //        .load(Constant.upload + reqUpdateAvatar.avatar.micro)
                                    //        .resize(100, 100)
                                    //        .transform(new CircularTransformation(0))
                                    //        .into((ImageView) ((Activity) _context).findViewById(R.id.user_avatar_header));
                                    updateAvatarCallback.onSuccess(reqUpdateAvatar.avatar);
                                    return;
                                }
                                errorCallback.onError(reqUpdateAvatar.error.error_code, reqUpdateAvatar.error.error_msg);
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
                    params.put("ext", ext);
                    params.put("base64_code_ava", base64_code_ava);
                    return params;
                };
            };
            SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.update_avatar");
        }
    }

    public interface UpdateAvatarCallback{
        void onSuccess(Image avatar);
    }

    public interface GetProfileCallback {
        void onSuccess(User user, Activity activity);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface EditProfileCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface SearchProfilesCallback{
        void onSuccess(User[] users);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetGuestsCallback{
        void onSuccess(Guest[] profiles, Activity activity);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetBalanceCallback {
        public void onSuccess(BalanceReq balanceReq);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetCountCallback {
        public void onSuccess(AllCounts allCounts);
        public void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
