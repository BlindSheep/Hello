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
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.temp_files_save_file,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if (response != null) {
                                Log.d("temp_file", response);
                                AddFile addFile = gson.fromJson(response, AddFile.class);
                                if(addFile.error == null){
                                    uploadedFiles.add(addFile.id);
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
            final int id,
            final DeleteFileCallback deleteFileCallback,
            final ErrorCallback errorCallback
    ){
        if(Constant.api_key!=""){
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.temp_files_delete_file,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            if (response != null) {
                                Log.d("temp_file.delete", response);
                                UniversalResponse universalResponse = gson.fromJson(response, UniversalResponse.class);
                                if(universalResponse.error == null){
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
                    Map<String, String> params = getParamsMap();
                    params.put("id", Integer.toString(id));
                    return params;
                };
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

    public ArrayList<Integer> getUploadedFiles(){
        return uploadedFiles;
    }

}
