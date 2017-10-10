package com.httpso_hello.hello.fragments;

/**
 * Created by mixir on 16.08.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.activity.LikeActivity;
import com.httpso_hello.hello.helper.Constant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoFragment extends Fragment {

    public static final String ARG_PHOTO_ORIG = "ARG_PHOTO_ORIG";

    private String photoOrig;

    public static PhotoFragment newInstance(String image) {
        PhotoFragment pageFragment = new PhotoFragment();
        Bundle arguments = new Bundle();

        arguments.putString("ARG_PHOTO_ORIG", image);

        pageFragment.setArguments(arguments);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoOrig = getArguments().getString(ARG_PHOTO_ORIG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.photo_fragment_item, container, false);
        final PhotoView IV = ((PhotoView) rootView.findViewById(R.id.photo_item));
        final ProgressBar progressBarPhoto = (ProgressBar) rootView.findViewById(R.id.progressBarPhoto);

        //Загрузка фотки
        Picasso.with(getContext())
                .load(photoOrig)
                .error(R.mipmap.avatar)
                .into(IV, new Callback(){
                    @Override
                    public void onSuccess(){
                        progressBarPhoto.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(){
                        progressBarPhoto.setVisibility(View.GONE);
                    }
                });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

