package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Board;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Resp;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 19.08.2017.
 */

public class HBoard extends Help{

    private Settings stgs;
    private Context _context;

    public HBoard(Context context){
        super();
        this._context = context;
        stgs = new Settings(_context);
    }

    public void getBoard(final Activity activity, final int page, final GetBoardCallback getBoardCallback){
        Log.d("board", "Enter");
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.board_get_board_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("board", response);
                            if (response != null) {
                                Board board = gson.fromJson(response, Board.class);
                                if (board.error==null){
//                                    Log.d("board_error", board.content_error);
//                                    Log.d("board", Integer.toString(board.items[0].id));
                                    getBoardCallback.onSuccess(board.items, activity);
                                    return;
                                }
                                getBoardCallback.onError(board.error.error_code, board.error.error_msg);
                                return;
                            }
                            getBoardCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getBoardCallback.onInternetError();
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", Constant.api_key);
                    params.put("auth_token", stgs.getSettingStr("auth_token"));
                    params.put("page", Integer.toString(page));
                    return params;
                };
            };
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getProfile");
        }
    }

    public void addBoard(
            final String content,
            final boolean is_anonim,
            final ArrayList<Integer> uploadedFiles,
            final AddBoardCallback addBoardCallback,
            final Help.ErrorCallback errorCallback
    ){
        if (Constant.api_key !="") {
            StringRequest SReq = new StringRequest(
                    Request.Method.POST,
                    Constant.board_add_item_uri,
                    new Response.Listener<String>() {
                        public void onResponse(String response){
                            Log.d("add_board", response);
                            if (response != null) {
                                YandexMetrica.getReporter(_context, Constant.metrika_api_key).reportEvent("add_board");
                                addBoardCallback.onSuccess();
//                                Board board = gson.fromJson(response, Board.class);
//                                Log.d("add_item", board.content_error);
//                                if (board.error==null){
//                                    Log.d("board", Integer.toString(board.items[0].id));
//                                    addBoardCallback.onSuccess();
//                                    return;
//                                }
//                                addBoardCallback.onError(board.error.error_code, board.error.error_msg);
//                                return;
                            }
//                            addBoardCallback.onInternetError();
//                            return;
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
                    params.put("content", content);
                    params.put("ctype_name", "board");
                    if (is_anonim) params.put("is_anonim", "1");
                    else params.put("is_anonim", "0");
                    if(uploadedFiles.size()!=0){
                        params.put("uploaded_files", gson.toJson(uploadedFiles.toArray()));
                    }
                    return params;
                };
            };
            SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQ.getInstance(this._context).addToRequestQueue(SReq, "board.addBoard");
        }
    }

    public interface GetBoardCallback{
        void onSuccess(BoardItem[] boardItems, Activity activity);
        void onError(int error_code, String error_message);
        void onInternetError();
    }

    public interface AddBoardCallback{
        void onSuccess();
    }

}
