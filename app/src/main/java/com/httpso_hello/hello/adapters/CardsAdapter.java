package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.fragments.PhotoFragment;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mixir on 10.11.2017.
 */

public class CardsAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> photoOrig;

    //Принять нужные значения из активности и впихнуть в переменные
    public CardsAdapter(FragmentManager fm, ArrayList<String> photoOrig) {
        super(fm);
        this.photoOrig = photoOrig;
    }

    //Переслать значения во фрагмент
    @Override
    public Fragment getItem(int i) {
        return PhotoFragment.newInstance(photoOrig.get(i));
    }

    //Кол-во всех элементов
    @Override
    public int getCount() {
        return photoOrig.size();
    }

    public void addAll (ArrayList<String> photoOrig) {
        this.photoOrig.addAll(photoOrig);
    }
}