package com.httpso_hello.hello.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private SharedPreferences SP ;

    public Settings(Context context){
        SP = context.getSharedPreferences("Settings", context.MODE_PRIVATE);

    }
    /**
     * Установка настройки
     * @param name
     * @param val
     */
    public <T> void setSetting(String name, String val){
        SharedPreferences.Editor edSP = SP.edit();
        edSP.putString(name, val);
        edSP.commit();
    }

    public void setSettingInt(String name, int val){
        SharedPreferences.Editor edSP = SP.edit();
        edSP.putInt(name, val);
        edSP.commit();
    }
    /**
     * Получение настройки
     * @param name
     * @return
     */
    public <T> String getSettingStr(String name){
        return SP.getString(name, null);
    }
    public int getSettingInt(String name){
        return SP.getInt(name, 0);
    }

    public <T> boolean getSettingBool(String name){
        return SP.getBoolean(name, false);
    }
}
