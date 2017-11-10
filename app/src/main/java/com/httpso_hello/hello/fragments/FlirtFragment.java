package com.httpso_hello.hello.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.httpso_hello.hello.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by mixir on 10.11.2017.
 */

public class FlirtFragment extends Fragment {

    public static final String ARG_PHOTO_ORIG = "ARG_PHOTO_ORIG";

    private String photoOrig;

    public static FlirtFragment newInstance(String image) {
        FlirtFragment pageFragment = new FlirtFragment();
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

        View rootView = inflater.inflate(R.layout.content_flirtik_item, container, false);
        final PhotoView IV = ((PhotoView) rootView.findViewById(R.id.photo_item));
        final ProgressBar progressBarPhoto = (ProgressBar) rootView.findViewById(R.id.progressBarPhoto);

        //Загрузка фотки
        DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
        int width = displaymetrics.widthPixels / 2 - 50;
        IV.setMinimumHeight(width);
        IV.setMinimumWidth(width);
        Picasso.with(getContext())
                .load(photoOrig)
                .resize(width, width)
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

