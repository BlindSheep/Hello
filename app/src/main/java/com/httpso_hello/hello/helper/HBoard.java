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
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 19.08.2017.
 */

public class HBoard extends Help {

    private Settings stgs;
    private Context _context;

    public HBoard(Context context) {
        super(context);
        this._context = context;
        stgs = new Settings(_context);
    }


    public void getBoard(
            final Activity activity,
            final int page,
            final GetBoardCallback getBoardCallback) {
        Log.d("board", "Enter");
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.board_get_board_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("board", response);
                        if (response != null) {
                            Board board = gson.fromJson(response, Board.class);
                            if (board.error == null) {
                                getBoardCallback.onSuccess(board.items, activity);
                                setNewToken(board.token);
                                return;
                            }
                            getBoardCallback.onError(board.error.code, board.error.message);
                            return;
                        }
                        getBoardCallback.onInternetError();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getBoardCallback.onInternetError();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = getParamsMap(_context);
                params.put("page", Integer.toString(page));
                return params;
            }

            ;
        };
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "users.getProfile");
    }

    public void addBoard(
            final String text,
            final int groupId,
            final boolean isAnonim,
            final ArrayList<Integer> uploadedFiles,
            final AddBoardCallback addBoardCallback,
            final Help.ErrorCallback errorCallback
    ) {
        StringRequest SReq = new StringRequest(
                Request.Method.POST,
                Constant.group_add_post_uri,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.d("add_board", response);
                        if (response != null) {
                            addBoardCallback.onSuccess();
//                                Board board = gson.fromJson(response, Board.class);
//                                Log.d("add_item", board.content_error);
//                                if (board.error==null){
//                                    Log.d("board", Integer.toString(board.items[0].id));
//                                    addBoardCallback.onSuccess();
//                                    return;
//                                }
//                                addBoardCallback.onError(board.error.code, board.error.message);
//                                return;
                        }
//                            addBoardCallback.onInternetError();
//                            return;
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
                Map<String, String> params = getParamsMap(_context);
                params.put("groupId", Integer.toString(groupId));
                params.put("isAnonim", Boolean.toString(isAnonim));
                params.put("text", text);
                if (uploadedFiles.size() != 0) {
                    params.put("uploaded_files", gson.toJson(uploadedFiles.toArray()));
                }
                return params;
            }

            ;
        };
        SReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQ.getInstance(this._context).addToRequestQueue(SReq, "board.addBoard");
    }

    public interface GetBoardCallback {
        void onSuccess(BoardItem[] boardItems, Activity activity);

        void onError(int error_code, String error_message);

        void onInternetError();
    }

    public interface AddBoardCallback {
        void onSuccess();
    }

}
