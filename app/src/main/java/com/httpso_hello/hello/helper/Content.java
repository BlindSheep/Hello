package com.httpso_hello.hello.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Board;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Friends;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 01.11.2017.
 */

public class Content extends Help {

    private Settings stgs;
    private Context _context;

    private static Content instance;

    public Content(Context context){
        super(context);
        this._context = context;
        stgs = new Settings(context);
    }

    public static synchronized Content getInstance(Context context){
        if(instance == null){
            instance = new Content(context);
        }
        return instance;
    }

    //Удаление контента
    public void deleteContent(
            final int id,
            final String content_type,
            final int group_id,
            final Content.DeleteContentCallback deleteContentCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.content_delete_item_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("content", response);
                        if (response != null) {
                            Friends friends = gson.fromJson(response, Friends.class);
                            if(friends.error == null){
                                deleteContentCallback.onSuccess();
                                return;
                            }
                            deleteContentCallback.onError(friends.error.code, friends.error.message);
                            return;
                        }
                        deleteContentCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deleteContentCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", stgs.getSettingStr("auth_token"));
                params.put("id", Integer.toString(id));
                params.put("content_type", content_type);
                if(group_id!=0) params.put("group_id", Integer.toString(group_id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "content.delete_item");
    }

    //Получение контента
    public void getContentItem(
            final int id,
            final Content.GetContentItemCallback getContentItemCallback
    ){
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.content_get_item_board_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response){
                        Log.d("content", response);
                        if (response != null) {
                            Board board = gson.fromJson(response, Board.class);
                            if(board.error == null){
                                getContentItemCallback.onSuccess(board.items[0]);
                                return;
                            }
                            getContentItemCallback.onError(board.error.code, board.error.message);
                            return;
                        }
                        getContentItemCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getContentItemCallback.onInternetError();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap();
                params.put("item_id", Integer.toString(id));
                return params;
            };
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "content.get_item");
    }

    public interface DeleteContentCallback {
        void onSuccess();
        void onError(int error_code, String error_msg);
        void onInternetError();
    }

    public interface GetContentItemCallback {
        void onSuccess(BoardItem item);
        void onError(int error_code, String error_msg);
        void onInternetError();
    }
}
