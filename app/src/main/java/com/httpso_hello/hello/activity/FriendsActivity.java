package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;

import com.httpso_hello.hello.Structures.ForUserOnly;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.FriendsAdapter;
import com.httpso_hello.hello.fragments.FriendsFragment;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Friend;

import static android.R.style.Animation_Dialog;

public class FriendsActivity extends SocketActivity {

    private ProgressBar progressBarFriends;
    private Bundle extras;
    private PopupWindow popUpWindow;
    private View popupView;
    private PopupWindow popUpWindow2;
    private View popupView2;
    private ViewPager listFriends;
    private ProgressBar launch;
    private TextView zagolovok;
    private ProgressBar launch2;
    private TextView zagolovok2;
    private Bundle friendsAllFragmentArg;
    private Bundle friendsOnlineFragmentArg;
    private Bundle friendsRequestInFriendsFragmentArg;
    private FriendsFragment friendsAllFragment;
    private FriendsFragment friendsOnlineFragment;
    private FriendsFragment friendsRequestInFragment;
    private FriendsAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_friends);
        setHeader();

        if (extras.getInt("profile_id") == stgs.getSettingInt("userId")) setMenuItem("FriendsActivity");
        progressBarFriends = (ProgressBar) findViewById(R.id.progressBarFriends);
        popupView = getLayoutInflater().inflate(R.layout.popup_for_friends, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView2 = getLayoutInflater().inflate(R.layout.popup_for_friends_second, null);
        popUpWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listFriends = (ViewPager) findViewById(R.id.viewpagerFriends);
        launch = (ProgressBar) popupView.findViewById(R.id.launch);
        zagolovok = (TextView) popupView.findViewById(R.id.zagolovok);
        launch2 = (ProgressBar) popupView2.findViewById(R.id.launch);
        zagolovok2 = (TextView) popupView2.findViewById(R.id.zagolovok);
        friendsAllFragmentArg = new Bundle();
        friendsOnlineFragmentArg = new Bundle();
        friendsRequestInFriendsFragmentArg = new Bundle();
        friendsAllFragment = new FriendsFragment();
        friendsOnlineFragment = new FriendsFragment();
        friendsRequestInFragment= new FriendsFragment();
        adapter = new FriendsAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpagerFriends);

        getFriends();
    }

    public void getFriends() {
        Friend.getInstance(getApplicationContext()).getFriends(extras.getInt("profile_id"), "", FriendsActivity.this,
                new Friend.GetFriendsCallback() {
                    @Override
                    public void onSuccess(ForUserOnly[] friendsAll, Activity activity, boolean isUserFriends) {
                        //формируем аргументы для фрагмента со всеми друзьями
                        friendsAllFragmentArg.putSerializable("friendsArray", friendsAll);
                        friendsAllFragmentArg.putBoolean("isRequests", false);
                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                        friendsAllFragment.setArguments(friendsAllFragmentArg);
                        int friendsAllCount = friendsAll.length;
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
                        adapter.addFragment(friendsAllFragment, friends);

                        Friend.getInstance(getApplicationContext()).getFriends(extras.getInt("profile_id"), "online", FriendsActivity.this,
                                new Friend.GetFriendsCallback() {
                                    @Override
                                    public void onSuccess(ForUserOnly[] friendsAll, Activity activity, boolean isUserFriends) {
                                        //формируем аргументы для фрагмента с онлайн друзьями
                                        friendsOnlineFragmentArg.putSerializable("friendsArray", friendsAll);
                                        friendsOnlineFragmentArg.putBoolean("isRequests", false);
                                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                                        friendsOnlineFragment.setArguments(friendsOnlineFragmentArg);
                                        int friendsOnlineCount = friendsAll.length;
                                        adapter.addFragment(friendsOnlineFragment, Integer.toString(friendsOnlineCount) + " онлайн");

                                        Friend.getInstance(getApplicationContext()).getFriends(extras.getInt("profile_id"), "incoming", FriendsActivity.this,
                                                new Friend.GetFriendsCallback() {
                                                    @Override
                                                    public void onSuccess(ForUserOnly[] friendsAll, Activity activity, boolean isUserFriends) {
                                                        //формируем аргументы для фрагмента с заявками в друзья
                                                        friendsRequestInFriendsFragmentArg.putSerializable("friendsArray", friendsAll);
                                                        friendsRequestInFriendsFragmentArg.putBoolean("isRequests", true);
                                                        friendsAllFragmentArg.putBoolean("isUserFriends", isUserFriends);
                                                        friendsRequestInFragment.setArguments(friendsRequestInFriendsFragmentArg);
                                                        int friendsRequestInFriendsCount = friendsAll.length;
                                                        if(friendsAll.length!=0) adapter.addFragment(friendsRequestInFragment, "Заявки " + Integer.toString(friendsRequestInFriendsCount));

                                                        setPage();
                                                    }

                                                    @Override
                                                    public void onError(int error_code, String error_msg) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override public void run() {
                                                                getFriends();
                                                            }
                                                        }, 5000);
                                                    }

                                                    @Override
                                                    public void onInternetError() {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override public void run() {
                                                                getFriends();
                                                            }
                                                        }, 5000);
                                                    }
                                                }
                                        );
                                    }

                                    @Override
                                    public void onError(int error_code, String error_msg) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override public void run() {
                                                getFriends();
                                            }
                                        }, 5000);
                                    }

                                    @Override
                                    public void onInternetError() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override public void run() {
                                                getFriends();
                                            }
                                        }, 5000);
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFriends();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getFriends();
                            }
                        }, 5000);
                    }
                }
        );
    }

    private void setPage() {
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
                Friend.getInstance(getApplicationContext()).addFriend(id, new Friend.AddFriendsCallback() {
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
                Friend.getInstance(getApplicationContext()).deleteFriend(id, new Friend.DeleteFriendsCallback() {
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

    public void getPopupForDeleteFriend(final int id, final String name, final Image avatar) {
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindow2.setWidth(displaymetrics.widthPixels);
        popUpWindow2.setHeight(displaymetrics.heightPixels);
        popUpWindow2.setAnimationStyle(Animation_Dialog);
        zagolovok2.setText(name);
        popUpWindow2.showAtLocation(listFriends, Gravity.CENTER, 0, 0);
        ((TextView) popupView2.findViewById(R.id.writeToFriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                intent.putExtra("contact_id", id);
                intent.putExtra("nickname", name);
                if (avatar == null) {
                    intent.putExtra("avatar", Constant.default_avatar);
                } else {
                    intent.putExtra("avatar", avatar.micro);
                }
                startActivity(intent);
            }
        });
        ((TextView) popupView2.findViewById(R.id.deleteFriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zagolovok2.setText("Ожидайте...");
                launch2.setVisibility(View.VISIBLE);
                Friend.getInstance(getApplicationContext()).deleteFriend(id, new Friend.DeleteFriendsCallback() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                                intent.putExtra("profile_id", 0);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), name + " удален(а) из друзей", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                zagolovok2.setText(name);
                                launch2.setVisibility(View.GONE);
                            }

                            @Override
                            public void onInternetError() {
                                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                zagolovok2.setText(name);
                                launch2.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (popUpWindow.isShowing()) popUpWindow.dismiss();
        else if (popUpWindow2.isShowing()) popUpWindow2.dismiss();
        else super.onBackPressed();
    }

    @Override
    public void onPause() {
        if (popUpWindow != null) popUpWindow.dismiss();
        if (popUpWindow2 != null) popUpWindow2.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (popUpWindow != null) popUpWindow.dismiss();
        if (popUpWindow2 != null) popUpWindow2.dismiss();
        super.onDestroy();
    }
}
