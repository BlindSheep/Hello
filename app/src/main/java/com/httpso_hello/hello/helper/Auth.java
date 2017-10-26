package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 24.07.2017.
 */
// Наши библиотеки

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.httpso_hello.hello.Structures.Contact;
import com.httpso_hello.hello.Structures.Registration;
import com.httpso_hello.hello.Structures.ReqUpdateAvatar;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.Structures.Resp;

import java.util.HashMap;
import java.util.Map;
//Сторонние библиотеки


public class Auth extends Help {

    private Settings stgs;
    private Api api;
    Context context;

    public Auth(Context context){

        stgs = new Settings(context);
        this.context = context;
        api = new Api(context);
    }

    //Функция проверки авторизованности приложения
    public boolean autoLogion(){
        // Если Есть ключ то приложение считаент что оно авторизовано
        if((stgs.getSettingStr("auth_token")!= null) && (stgs.getSettingInt("user_id") != 0)){
            return true;
        }
        return false;
    }

    //Функция проверки Первый ли вход в приложение
    public boolean firstLogin(){
        if (stgs.getSettingInt("firstLogin") != 0) return false;
        else return true;
    }

    public void registration(String email, String password, String nickname, final RegistrationFinishCallBack registrationFinishCallBack){
        api.registration(email, password, nickname,
                new Api.RegistrationCallback() {
                    @Override
                    public void onSuccess(Registration result){
                        if(result.error == null) {
                            if(result.user_id != 0)
                                registrationFinishCallBack.onSuccess(result.user_id);
                                return;
                        }
                        registrationFinishCallBack.onError(result.error.error_code, result.error.error_msg);
                        return;
                    }

                    @Override
                    public void onInternetError() {
                        registrationFinishCallBack.onError(0, "Что то пошло не так.");
                    }
                }
                );
        }


    public void authorize(String login, String password, final AuthFinishingCallback authFinishing){
        api.login(login, password,
                new Api.LoginCallback() {
                    @Override
                    public void onSuccess(Resp result){
                        if(result.error == null) {
                            // Если учетные данные подошли, проверяем не заблокирован ли аккаунт
                            if (result.user_info.is_locked) {
                                authFinishing.onLoccked();
                                return;
                            }
                            //Если аккаунт не заблокирован
                            stgs.setSetting("session_id", result.session_id);
                            stgs.setSetting("session_name", result.session_name);
                            stgs.setSettingInt("user_id", result.user_id);
                            stgs.setSetting("auth_token", result.auth_token);
                            stgs.setSetting("user_nickname", result.user_info.nickname);
                            if(result.user_info.birth_date != null){
                                stgs.setSetting("user_age", ConverterDate.convertDateToAge(result.user_info.birth_date));
                            }
                            if(result.user_info.avatar !=null) {
                                stgs.setSetting("user_avatar.micro", Constant.upload + result.user_info.avatar.micro);
                            }

                            String token = FirebaseInstanceId.getInstance().getToken();
                            api.setPushUpToken(token);

                            authFinishing.onSuccess(result.user_info);
                            return;
                        }
                        authFinishing.onError(result.error.error_code, result.error.error_msg);
                        return;
                    }

                    @Override
                    public void onInternetError() {
                        authFinishing.onError(0, "Что то пошло не так.");
                    }
                }
        );
    }

    public void logout(final LogoutFinishingCallback logoutFinishingCallback){
        api.logout(new Api.LogoutCallback() {
            @Override
            public void onSuccess() {
                stgs.setSetting("session_id", null);
                stgs.setSetting("session_name", null);
                stgs.setSetting("user_id", null);
                stgs.setSetting("auth_token", null);
                stgs.setSetting("user_nickname", null);
                stgs.setSetting("user_age", null);
                stgs.setSetting("user_avatar.micro", null);
                logoutFinishingCallback.onSuccess();
                return;
            }

            @Override
            public void onInternetError() {
                logoutFinishingCallback.onError();
                return;
            }
        });
    }

    //Загрузка аватарки
    public void authRestore(
            final String email,
            final Context context,
            final AuthRestoreCallBack authRestoreCallBack
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.auth_restore_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("update_avatar", response);
                            if(response!=null){
                                ReqUpdateAvatar reqUpdateAvatar = gson.fromJson(response, ReqUpdateAvatar.class);
                                if(reqUpdateAvatar.error==null) {
                                    authRestoreCallBack.onSuccess();
                                    return;
                                }
                                authRestoreCallBack.onError(reqUpdateAvatar.error.error_code, reqUpdateAvatar.error.error_msg);
                                return;
                            }
                            authRestoreCallBack.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            authRestoreCallBack.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("email", email);
                    return params;
                };
            };
            RequestQ.getInstance(context).addToRequestQueue(SReq, "auth_restore");
        }
    }

    // Интерфейс для кэллбэка завершения авторизации
    public interface AuthFinishingCallback{
        void onSuccess(User user);
        void onError(int error_code, String error_msg);
        void onLoccked();
    }

    public interface LogoutFinishingCallback{
        void onSuccess();
        void onError();
    }

    public interface RegistrationFinishCallBack{
        void onSuccess(int user_id);
        void onError(int error_code, String error_msg);
    }

    public interface AuthRestoreCallBack{
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
