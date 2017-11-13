package com.httpso_hello.hello.fragments;

/**
 * Created by mixir on 23.08.2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.activity.FriendsActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.adapters.FriendsFragmentAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Friend;

import java.util.ArrayList;
import java.util.Collections;

public class FriendsFragment extends Fragment {

    private FriendItem[] friendItemFragment;
    private boolean isRequests;
    private boolean isUserFriends;
    private View header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        FriendItem[] friendItem = (FriendItem[]) args.getSerializable("friendsArray");
        boolean isRequests = args.getBoolean("isRequests");
        boolean isUserFriends = args.getBoolean("isUserFriends");


        this.friendItemFragment = friendItem;
        this.isRequests = isRequests;
        this.isUserFriends = isUserFriends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_friends, container, false);
        final ListView LV = (ListView) rootView.findViewById(R.id.listFriends);
        header = getActivity().getLayoutInflater().inflate(R.layout.footer6dp, null);

        ArrayList<FriendItem> friendsAllArray = new ArrayList<FriendItem>();
        Collections.addAll(friendsAllArray, this.friendItemFragment);

        FriendsFragmentAdapter friendsAdapter = new FriendsFragmentAdapter(getActivity(), friendsAllArray, isRequests, isUserFriends);

        LV.addHeaderView(header);
        LV.setAdapter(friendsAdapter);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", friendItemFragment[position - 1].id);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

}
