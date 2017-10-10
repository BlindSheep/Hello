package com.httpso_hello.hello.adapters;

/**
 * Created by mixir on 16.08.2017.
 */

import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.TextView;

import com.httpso_hello.hello.activity.FullscreenPhotoActivity;
import com.httpso_hello.hello.fragments.PhotoFragment;
import com.httpso_hello.hello.helper.Constant;

import java.util.ArrayList;
import java.util.List;

public class FragmentPhotoAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> photoOrig;

    //Принять нужные значения из активности и впихнуть в переменные
    public FragmentPhotoAdapter(FragmentManager fm, ArrayList<String> photoOrig) {
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
}