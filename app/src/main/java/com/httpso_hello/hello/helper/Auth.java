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
        super(context);
        stgs = new Settings(context);
        this.context = context;
        api = new Api(context);
    }

    //Функция проверки авторизованности приложения
    public boolean autoLogion(){
        // Если Есть ключ то приложение считаент что оно авторизовано
        if((stgs.getSettingStr("token")!= null) && (stgs.getSettingInt("userId") != 0)){
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
                            registrationFinishCallBack.onSuccess(result);
                        } else {
                            registrationFinishCallBack.onError(result.error.code, result.error.message);
                        }
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
                            if (result.isLocked) {
                                authFinishing.onLoccked();
                                return;
                            }
                            //Если аккаунт не заблокирован
                            stgs.setSetting("token", result.token);
                            stgs.setSettingInt("userId", result.userId);
                            stgs.setSetting("nickname", result.nickname);
                            if(result.birthDate != null){
                                stgs.setSetting("birthDate", ConverterDate.convertDateToAge(result.birthDate));
                            }
                            if(result.avatar !=null) {
                                stgs.setSetting("avatar.micro", Constant.upload + result.avatar.micro);
                            }
                            //Установка токена
                            String token = FirebaseInstanceId.getInstance().getToken();
                            api.setPushUpToken(token);

                            authFinishing.onSuccess(result.user_info);
                            return;
                        }
                        authFinishing.onError(result.error.code, result.error.message);
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
                stgs.setSetting("token", null);
                stgs.setSetting("userId", null);
                stgs.setSetting("nickname", null);
                stgs.setSetting("birthDate", null);
                stgs.setSetting("avatar.micro", null);
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
                            authRestoreCallBack.onError(reqUpdateAvatar.error.code, reqUpdateAvatar.error.message);
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
                params.put("email", email);
                return params;
            };
        };
        RequestQ.getInstance(context).addToRequestQueue(SReq, "auth_restore");
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
        void onSuccess(Registration registration);
        void onError(int error_code, String error_msg);
    }

    public interface AuthRestoreCallBack{
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
