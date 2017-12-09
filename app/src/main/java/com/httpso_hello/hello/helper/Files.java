package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.AddFile;
import com.httpso_hello.hello.Structures.UniversalResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Files extends Help {

    private Settings stgs;
    private Context _context;
    private Activity activity;
    private ArrayList<Integer> uploadedFiles = new ArrayList<>();

    public static Files instance;

    public static synchronized Files getInstance(Context context, Activity activity){
        if(instance == null){
            instance = new Files(context, activity);
        }
        return instance;
    }

    public Files(Context context, Activity activity){
        super(context);
        this._context = context;
        stgs = new Settings(this._context);
        this.activity = activity;
    }

    public String uploadFile(
            final String type,
            final String ext,
            final String file_base64,
            final String controller,
            final String content_type,
            final int position,
            final UploadFileCallback uploadFileCallback,
            final Help.ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            uploadedFiles.add(0);
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.temp_files_save_file,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if (response != null) {
                                Log.d("temp_file", response);
                                AddFile addFile = gson.fromJson(response, AddFile.class);
                                if(addFile.error == null){
                                    setUploadedFiles(addFile.id, position);
                                    uploadFileCallback.onSuccess(addFile.id, position);
                                    return;
                                }
                                errorCallback.onError(addFile.error.error_code, addFile.error.error_msg);
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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("type", type);
                    params.put("file_base64" , file_base64);
                    params.put("ext" , ext);
                    params.put("controller" , controller);
                    params.put("content_type" , content_type);
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "Files.uploadFile" + Integer.toString(position));
            return "Files.uploadFile" + Integer.toString(position);
        }
        return null;
    }
    public void deleteFile(
            final int position
    ){
        this.deleteFileFromUploadedFiles(0, position);
    }
    public void deleteFile(
            final int id,
            final int position,
            final DeleteFileCallback deleteFileCallback,
            final ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            // Удаляем из массива
            this.deleteFileFromUploadedFiles(id, position);
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.temp_files_delete_file,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            if (response != null) {
                                Log.d("temp_file.delete", response);
                                UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                                if (universalResponse.error == null) {
                                    deleteFileCallback.onSuccess();
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
                    params.put("id", Integer.toString(id));
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "Files.deleteFile" + Integer.toString(id));
        }
    }
    public void deleteFile(
            final int id,
            final int position
    ){
        if(Constant.api_key!=""){
            // Удаляем из массива
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.temp_files_delete_file,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = getParamsMap();
                    params.put("id", Integer.toString(id));
                    return params;
                }

                ;
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "Files.deleteFile" + Integer.toString(id));
        }
    }

    public void breakUploadRequest(String tag){
        RequestQ.getInstance(_context).cancelPendingRequests(tag);
    }

    public interface DeleteFileCallback{
        void onSuccess();
    }

    public interface UploadFileCallback{
        void onSuccess(int id, int position);
    }

    private void setUploadedFiles(final int id, final int position){
        if(uploadedFiles.get(position)==-1){
            // Удалить файл
            uploadedFiles.remove(position);
            deleteFile(id, position);
        } else {
            uploadedFiles.set(position, id);
        }
    }

    private void deleteFileFromUploadedFiles(final int id, final int position){
        if(position>=uploadedFiles.size())
            return;
        if(uploadedFiles.get(position) != id)
            return;
        if(id==0){
            uploadedFiles.set(position, -1);
        } else {
            uploadedFiles.remove(position);
        }
    }

    public ArrayList<Integer> getUploadedFiles(){
        return uploadedFiles;
    }

}
