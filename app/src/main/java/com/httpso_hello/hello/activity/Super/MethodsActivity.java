package com.httpso_hello.hello.activity.Super;

import android.widget.Toast;

/**
 * Created by mixir on 26.07.2018.
 */

public class MethodsActivity extends ComponentsActivity {
    public void showMessage(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}
