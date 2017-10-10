package com.httpso_hello.hello.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.activity.ChatActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mixir on 13.08.2017.
 */

public class Help {

    public Gson gson;

    public static final int REQUEST_ADD_PHOTO_MESSAGE = 1;
    public static final int REQUEST_UPDATE_AVATAR_GALLERY = 2;
    public static final int REQUEST_UPDATE_AVATAR_CAMERA = 3;
    public static final int REQUEST_ADD_PHOTO_GALLERY = 4;
    public static final int REQUEST_ADD_PHOTO_CAMERA = 5;

    public Help(){
        GsonBuilder GB = new GsonBuilder();
        this.gson = GB.create();
    }

    public Object fromJSON(String jsonElement, Class casckade){
        return this.gson.fromJson(jsonElement, casckade);
    }

    public interface ErrorCallback{
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
    // Запрос прав доступа
    public static void requestMultiplePermissions(Activity activity, String[] permissions, int request_code) {
        ActivityCompat.requestPermissions(activity, permissions, request_code);
    }

    /**
     *
     * @param activity астивность которая запрашивает права на доступ
     * @param permissions запрашиваемые права на доступ
     * @param request_code уникальный возвращаемый код
     * @return возвращает true если права получены, false в случае если выполняется запрос на получение прав доступа.
     * В случае если выполняется получение прав на доступ, дальнейшая обработка должна производиться
     * в переопределенном методе (в activity) onRequestPermissionsResult(...)
     */
    public static boolean runTaskAfterPermission(final Activity activity, final String[] permissions, final int request_code){
        boolean needRequestPermission = false;
        for (String permission :
                permissions) {
            if(ContextCompat.checkSelfPermission(
                    activity.getApplicationContext(),
                    permission
            ) == PackageManager.PERMISSION_DENIED) {
                needRequestPermission = true;
            }
        }
        if(needRequestPermission) {
            // Нужно получение прав на доступ
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                final String message = "Приложению нужны права доступа к фотографиям";
                Snackbar.make(
                        activity.getCurrentFocus(),
                        message,
                        Snackbar.LENGTH_INDEFINITE
                ).setAction(
                        "Предоставить",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestMultiplePermissions(
                                        activity,
                                        permissions,
                                        request_code
                                );
                            }
                        })
                        .show();
            } else {
                requestMultiplePermissions(activity, permissions, request_code);
            }
        } else {
            // Получены права на доступ
            return true;
        }
        return false;
    }

    // Поулчение изображения в формате base64
    public static String getBase64FromImage(Bitmap image, Bitmap.CompressFormat compressFormat){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(compressFormat, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

}
