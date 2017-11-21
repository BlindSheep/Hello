package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;

import com.httpso_hello.hello.Structures.FriendItem;
import com.httpso_hello.hello.adapters.FriendsAdapter;
import com.httpso_hello.hello.fragments.FriendsFragment;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Friend;
import com.squareup.picasso.Picasso;

import static android.R.style.Animation_Dialog;

public class FriendsActivity extends SuperMainActivity{

    private ProgressBar progressBarFriends;
    private Bundle extras;
    private PopupWindow popUpWindow;
    private View popupView;
    private ViewPager listFriends;
    private ProgressBar launch;
    private TextView zagolovok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_friends);
        setHeader();

        if (extras.getInt("profile_id") == 0) setMenuItem("FriendsActivity");
        progressBarFriends = (ProgressBar) findViewById(R.id.progressBarFriends);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_friends, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listFriends = (ViewPager) findViewById(R.id.viewpagerFriends);
        launch = (ProgressBar) popupView.findViewById(R.id.launch);
        zagolovok = (TextView) popupView.findViewById(R.id.zagolovok);
        getFriends();
    }

    public void getFriends() {
        Friend.getInstance(getApplicationContext()).getFriends(extras.getInt("profile_id"), this,
                new Friend.GetFriendsCallback() {
                    @Override
                    public void onSuccess(FriendItem[] friendsAll, FriendItem[] friendsOnline, FriendItem[] request_in_friends, Activity activity, boolean isUserFriends) {

                        //содаем фрагменты и аргументы
                        Bundle friendsAllFragmentArg = new Bundle();
                        Bundle friendsOnlineFragmentArg = new Bundle();
                        Bundle friendsRequestInFriendsFragmentArg = new Bundle();
                        FriendsFragment friendsAllFragment = new FriendsFragment();
                        FriendsFragment friendsOnlineFragment = new FriendsFragment();
                        FriendsFragment friendsRequestInFragment= new FriendsFragment();

                        //формируем аргументы для фрагмента со всеми друзьями
                        friendsAllFragmentArg.putSerializable("friendsArray", friendsAll);
                        friendsAllFragmentArg.putBoolean("isRequests", false);
                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                        friendsAllFragment.setArguments(friendsAllFragmentArg);

                        //формируем аргументы для фрагмента с онлайн друзьями
                        friendsOnlineFragmentArg.putSerializable("friendsArray", friendsOnline);
                        friendsOnlineFragmentArg.putBoolean("isRequests", false);
                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                        friendsOnlineFragment.setArguments(friendsOnlineFragmentArg);

                        //формируем аргументы для фрагмента с заявками в друзья
                        friendsRequestInFriendsFragmentArg.putSerializable("friendsArray", request_in_friends);
                        friendsRequestInFriendsFragmentArg.putBoolean("isRequests", true);
                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                        friendsRequestInFragment.setArguments(friendsRequestInFriendsFragmentArg);

                        int friendsAllCount = friendsAll.length;
                        int friendsOnlineCount = friendsOnline.length;
                        int friendsRequestInFriendsCount = request_in_friends.length;
                        String friends = "";
                        if(friendsAllCount != 0) {
                            if(friendsAllCount == 1) friends = Integer.toString(friendsAllCount) + " друг";
                            else if((friendsAllCount >= 2) && (friendsAllCount < 5)) friends = Integer.toString(friendsAllCount) + " друга";
                            else if((friendsAllCount >= 5) && (friendsAllCount < 21)) friends = Integer.toString(friendsAllCount) + " друзей";
                            else if((friendsAllCount >= 21) && (friendsAllCount % 10 == 1)) friends = Integer.toString(friendsAllCount) + " друг";
                            else if((friendsAllCount >= 21) && (friendsAllCount % 10 == 2)) friends = Integer.toString(friendsAllCount) + " друга";
                            else if((friendsAllCount >= 21) && (friendsAllCount % 10 == 3)) friends = Integer.toString(friendsAllCount) + " друга";
                            else if((friendsAllCount >= 21) && (friendsAllCount % 10 == 4)) friends = Integer.toString(friendsAllCount) + " друга";
                            else friends = Integer.toString(friendsAllCount) + " друзей";
                        } else {
                            friends = "Нет друзей";
                        }

                        // Добавляем фраменты на страницу
                        FriendsAdapter adapter = new FriendsAdapter(getSupportFragmentManager());
                        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerFriends);

                        adapter.addFragment(friendsAllFragment, friends);
                        adapter.addFragment(friendsOnlineFragment, Integer.toString(friendsOnlineCount) + " онлайн");
                        if(request_in_friends.length!=0) adapter.addFragment(friendsRequestInFragment, "Заявки " + Integer.toString(friendsRequestInFriendsCount));

                        viewPager.setAdapter(adapter);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayoutFriends);
                        tabLayout.setupWithViewPager(viewPager);

                        progressBarFriends.setVisibility(View.GONE);

                        toolbar.setTitle("Все друзья");
                        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(final int position) {
                                //Заполняем тулбар при смене вкладыки
                                if (position == 0)toolbar.setTitle("Все друзья");
                                if (position == 1)toolbar.setTitle("Друзья онлайн");
                                if (position == 2)toolbar.setTitle("Заявки в друзья");
                            }

                            @Override
                            public void onPageScrolled(int position, float positionOffset,
                                                       int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {

                    }

                    @Override
                    public void onInternetError() {

                    }
                }
        );
    }

    public void getPopupForAcceptOrDelete(final int id) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindow.setWidth(displaymetrics.widthPixels);
        popUpWindow.setHeight(displaymetrics.heightPixels);
        popUpWindow.setAnimationStyle(Animation_Dialog);
        zagolovok.setText("Заявка в друзья");
        popUpWindow.showAtLocation(listFriends, Gravity.CENTER, 0, 0);
        ((TextView) popupView.findViewById(R.id.acceptFriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zagolovok.setText("Ожидайте...");
                launch.setVisibility(View.VISIBLE);
                Friend.getInstance(getApplicationContext()).acceptFriend(id, new Friend.AcceptFriendCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                        intent.putExtra("profile_id", 0);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Заявка принята", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        zagolovok.setText("Заявка в друзья");
                        launch.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                        zagolovok.setText("Заявка в друзья");
                        launch.setVisibility(View.GONE);
                    }
                });
            }
        });
        ((TextView) popupView.findViewById(R.id.deleteFriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zagolovok.setText("Ожидайте...");
                launch.setVisibility(View.VISIBLE);
                Friend.getInstance(getApplicationContext()).deleteFriend(
                        1,
                        id,
                        new Friend.DeleteFriendsCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                        intent.putExtra("profile_id", 0);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Заявка удалена", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        zagolovok.setText("Заявка в друзья");
                        launch.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                        zagolovok.setText("Заявка в друзья");
                        launch.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (popUpWindow != null) popUpWindow.dismiss();
        super.onDestroy();
    }
}
