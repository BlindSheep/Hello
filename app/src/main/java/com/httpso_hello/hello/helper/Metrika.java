package com.httpso_hello.hello.helper;

import android.app.Application;
import com.yandex.metrica.YandexMetrica;

public class Metrika extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        YandexMetrica.activate(getApplicationContext(), "71dba453-2cd4-4256-b676-77d249383d44");
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
