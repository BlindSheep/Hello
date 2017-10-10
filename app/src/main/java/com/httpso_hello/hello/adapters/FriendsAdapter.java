package com.httpso_hello.hello.adapters;

/**
 * Created by mixir on 23.08.2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.Structures.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private ArrayList<FriendItem> friends = new ArrayList<FriendItem>();

    public FriendsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        this.friends = friends;
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

}