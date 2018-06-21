package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.Structures.AllCounts;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.Structures.Guests;
import com.httpso_hello.hello.Structures.IgnoreList;
import com.httpso_hello.hello.Structures.IgnoreUser;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.NewProfile;
import com.httpso_hello.hello.Structures.ReqUpdateAvatar;
import com.httpso_hello.hello.Structures.Resp;
import com.httpso_hello.hello.Structures.SearchProfiles;
import com.httpso_hello.hello.Structures.User;

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

    /**
     * Получение стартовой информации о пользователе
     * newAPI
     * @param version
     * @param pushUpToken
     * @param getSmallUserInfoCallback
     * @param errorCallback
     */
    public void getSmallUserInfo(
            final String version,
            final String pushUpToken,
            final GetSmallUserInfoCallback getSmallUserInfoCallback,
            final Help.ErrorCallback errorCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_get_small_user_info_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if (response != null) {
                            Log.d("small_user_info", response);
                            Resp resp = gson.fromJson(response, Resp.class);
                            if (resp.error == null) {
                                getSmallUserInfoCallback.onSuccess(resp);
                                setNewToken(resp.token);
                                return;
                            } else errorCallback.onError(resp.error.code, resp.error.message);
                        } else {
                            errorCallback.onInternetError();
                            return;
                        }
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
                params.put("pushUpToken", pushUpToken);
                params.put("version", version);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getSmallUserInfo");
    }

    public void getProfile(
            final int profile_id,
            final Activity activity,
            final GetProfileCallback getProfileCallback){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_get_profile_uri + Integer.toString(profile_id),
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if (response != null) {
                            try {
                                Log.d("User", response);
                                RespForNewProfile resp = gson.fromJson(response, RespForNewProfile.class);
                                if (resp.error == null) {
                                    getProfileCallback.onSuccess(resp.profile, activity);
                                    setNewToken(resp.token);
                                    return;
                                }
                                getProfileCallback.onError(resp.error.code, resp.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("profileId", Integer.toString(profile_id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getProfile"+Integer.toString(profile_id));
    }

    public void editProfile(
            final String new_user_info,
            final EditProfileCallback editProfileCallback){
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
                            editProfileCallback.onError(resp.error.code, resp.error.message);
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
                params.put("token", stgs.getSettingStr("token"));
                params.put("changedInfo", new_user_info);
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.edutProfile");
    }

    public void getIgnoreList (
            final GetIgnoreListCallback getIgnoreListCallback){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_get_ignore_list_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if (response != null) {
                            Log.d("User", response);
                            IgnoreList ignoreList = gson.fromJson(response, IgnoreList.class);
                            if (ignoreList.error == null) {
                                getIgnoreListCallback.onSuccess(ignoreList.ignoreUsers);
                                return;
                            }
                            getIgnoreListCallback.onError(ignoreList.error.code, ignoreList.error.message);
                            return;
                        }
                        getIgnoreListCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getIgnoreListCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getIgnorList");
    }

    public void deleteUserFromIgnore (
            final int contact_id,
            final DeleteUserIgnoreCallback deleteUserIgnoreCallback){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_delete_user_ignore_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if (response != null) {
                            Log.d("User", response);
                            IgnoreList ignoreList = gson.fromJson(response, IgnoreList.class);
                            if (ignoreList.error == null) {
                                deleteUserIgnoreCallback.onSuccess();
                                return;
                            }
                            deleteUserIgnoreCallback.onError(ignoreList.error.code, ignoreList.error.message);
                            return;
                        }
                        deleteUserIgnoreCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deleteUserIgnoreCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("contact_id", Integer.toString(contact_id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.deleteUserFromIgnore");
    }

    public void searchProfiles(
            final int ageFrom,
            final int ageTo,
            final int gender,
            final int page,
            final int reg_cel,
            final SearchProfilesCallback searchProfilesCallback){

        Map<String, String> filtersMap = new HashMap<String, String>();
        filtersMap.put("startAge", Integer.toString(ageFrom));
        filtersMap.put("finishAge", Integer.toString(ageTo));
//        if (reg_cel != 0) filtersMap.put("reg_cel[]", Integer.toString(reg_cel));
        filtersMap.put("gender", Integer.toString(gender));
        GsonBuilder GBmap = new GsonBuilder();
        Gson gsonMap = GBmap.create();
        final String filters = gsonMap.toJson(filtersMap);

        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.users_search_profiles_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        if(response!=null){
                            Log.d("sP", response);
                            SearchProfiles searchProfiles = gson.fromJson(response, SearchProfiles.class);
                            if(searchProfiles.error==null) {
                                searchProfilesCallback.onSuccess(searchProfiles.users);
                                setNewToken(searchProfiles.token);
                                // Сохранение инфы о фильтре с сервера
                                if (searchProfiles.filters != null) {
                                    stgs.setSettingInt("gender", searchProfiles.filters.gender);
                                    stgs.setSettingInt("ageFrom", searchProfiles.filters.startAge);
                                    stgs.setSettingInt("ageTo", searchProfiles.filters.finishAge);
                                }
                                // Конец
                                return;
                            }
                            searchProfilesCallback.onError(searchProfiles.error.code, searchProfiles.error.message);
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
                Map<String, String> params = getParamsMap(_context);
                params.put("filters", filters.toString());
                params.put("page", Integer.toString(page));
                params.put("perPage", Integer.toString(30));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.searchProfiles");
    }

    public void getGuests(final int page, final Activity activity, final GetGuestsCallback getGuestsCallback){
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
                            getGuestsCallback.onError(guests.error.code, guests.error.message);
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
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("page", Integer.toString(page));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getGuests");
    }

    public void getBalance(
            final GetBalanceCallback getBalanceCallback
    ) {
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
                            getBalanceCallback.onError(balanceReq.error.code, balanceReq.error.message);
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
                params.put("token", stgs.getSettingStr("auth_token"));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "billing.getPoints");
    }

    public void getCount(
            final GetCountCallback getCountCallback
    ) {
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
                            getCountCallback.onError(allCounts.error.code, allCounts.error.message);
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
                params.put("token", stgs.getSettingStr("auth_token"));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users_get_counts");
    }

    //Загрузка аватарки
    public void updateAvatar(
            final String base64_code_ava,
            final String ext,
            final UpdateAvatarCallback updateAvatarCallback,
            final ErrorCallback errorCallback
    ){
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
                                updateAvatarCallback.onSuccess(reqUpdateAvatar.avatar);
                                return;
                            }
                            errorCallback.onError(reqUpdateAvatar.error.code, reqUpdateAvatar.error.message);
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
                params.put("ext", ext);
                params.put("base64_code_ava", base64_code_ava);
                return params;
            };
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.update_avatar");
    }

    public interface UpdateAvatarCallback{
        void onSuccess(Image avatar);
    }

    public interface GetProfileCallback {
        void onSuccess(NewProfile profile, Activity activity);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetSmallUserInfoCallback {
        void onSuccess(Resp resp);
    }
    public interface EditProfileCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface GetIgnoreListCallback {
        void onSuccess(IgnoreUser[] iu);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    public interface DeleteUserIgnoreCallback {
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
