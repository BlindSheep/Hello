package com.httpso_hello.hello.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.Structures.NoticeItem;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.adapters.FlirtikiFragmentAdapter;
import com.httpso_hello.hello.adapters.FriendsFragmentAdapter;
import com.httpso_hello.hello.helper.Constant;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mixir on 24.08.2017.
 */


public class SimpationFragment extends Fragment {

    private FlirtikItem[] flirtikItemFragment;
    private View header;
    private FlirtikiFragmentAdapter flirtikiFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        FlirtikItem[] flirtikItemFragment = (FlirtikItem[]) args.getSerializable("flirtikArray");

        this.flirtikItemFragment = flirtikItemFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_simpation, container, false);
        final ListView LV = (ListView) rootView.findViewById(R.id.listSimpatii);
        header = getActivity().getLayoutInflater().inflate(R.layout.footer6dp, null);

        ArrayList<FlirtikItem> flirtiki = new ArrayList<FlirtikItem>();
        Collections.addAll(flirtiki, this.flirtikItemFragment);

        flirtikiFragmentAdapter = new FlirtikiFragmentAdapter(getActivity(), flirtiki);

        LV.addHeaderView(header);
        LV.setAdapter(flirtikiFragmentAdapter);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("profile_id", flirtikItemFragment[position-1].id);
                intent.putExtra("profile_nickname", flirtikItemFragment[position-1].nickname);
                intent.putExtra("avatar", Constant.default_avatar);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
