package com.httpso_hello.hello.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.httpso_hello.hello.Structures.Board;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Resp;

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
                                    Log.d("board", Integer.toString(board.items[0].id));
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
            final AddBoardCallback addBoardCallback){
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
                                    Log.d("board", Integer.toString(board.items[0].id));
                                    addBoardCallback.onSuccess();
                                    return;
                                }
                                addBoardCallback.onError(board.error.error_code, board.error.error_msg);
                                return;
                            }
                            addBoardCallback.onInternetError();
                            return;
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            addBoardCallback.onInternetError();
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
                    return params;
                };
            };
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
        void onError(int error_code, String error_message);
        void onInternetError();
    }

}
