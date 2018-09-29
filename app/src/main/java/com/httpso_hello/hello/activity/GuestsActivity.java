package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.activity.Super.SocketActivity;
import com.httpso_hello.hello.adapters.GuestsListAdapter;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.push_services.TokenReq;

import java.util.ArrayList;
import java.util.Collections;


public class GuestsActivity extends SocketActivity {

    private ListView listGuestsNew;
    private Profile profile;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GuestsListAdapter glAdapterNew;
    public int pageNumber = 0;
    private View footerLoading;
    private boolean thatsAll = false;
    private boolean isLaunch = false;
    private LinearLayout paidGuests;
    private TextView cancel;
    private TextView go;
    private String paidToken=null;
    private TextView balance;
    private TextView prise;
    private TextView error;
    private boolean firstView = true;
    private CheckBox checkBox;
    private TextView newGuests;
    private boolean notPayd = false;
    private LinearLayout empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);
        setHeader();
        setMenuItem("GuestsActivity");

        listGuestsNew = ((ListView) findViewById(R.id.listGuestsNew));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        footerLoading = getLayoutInflater().inflate(R.layout.footer_loading, null);
        paidGuests = (LinearLayout) findViewById(R.id.paidGuests);
        go = (TextView) findViewById(R.id.go);
        balance = (TextView) findViewById(R.id.balance);
        prise = (TextView) findViewById(R.id.prise);
        error = (TextView) findViewById(R.id.error);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        newGuests = (TextView) findViewById(R.id.newGuests);
        empty = (LinearLayout) findViewById(R.id.empty);
    }

    private void getToken() {
        if (stgs.getSettingInt("avtoGetGuests") == 0) {
            paidGuests.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            if (stgs.getSettingInt("guests") == 0) newGuests.setText("У вас нет новых гостей");
            else newGuests.setText("У вас " + Integer.toString(stgs.getSettingInt("guests")) + " новых гостей");
            balance.setText("У вас на счету - " + Integer.toString(stgs.getSettingInt("balance")) + " баллов");
            prise.setText("Стоимость просмотра - 1 балл");
            paidGuests.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.GONE);

            if (stgs.getSettingInt("balance") < 1) {
                error.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);
                go.setText("Пополнить");
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GuestsActivity.this, BillingActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                error.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText("Не показывать предупреждение.\nПри каждом заходе в данный раздел, автоматически будет списываться 1 балл");
                go.setText("Продолжить");
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //После согласия на просмотр гостей
                        //Скрываем блок с предупреждением и открываем блок с контентом
                        paidGuests.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);

                        //Автопросмотр
                        if (checkBox.isChecked()) stgs.setSettingInt("avtoGetGuests", 1);
                        else stgs.setSettingInt("avtoGetGuests", 0);

                        //Получаем контент
                        getGuests();

                        // Свайп для обновления
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getGuests();
                            }
                        });
                    }
                });
            }
        } else {
            paidGuests.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            if (stgs.getSettingInt("balance") < 1) {
                if (stgs.getSettingInt("guests") == 0) newGuests.setText("У вас нет новых гостей");
                else newGuests.setText("У вас " + Integer.toString(stgs.getSettingInt("guests")) + " новых гостей");
                balance.setText("У вас на счету - " + stgs.getSettingInt("balance") + " баллов");
                prise.setText("Стоимость просмотра - 1 балл");
                paidGuests.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                stgs.setSettingInt("avtoGetGuests", 0);
                checkBox.setVisibility(View.GONE);
                go.setText("Пополнить");
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GuestsActivity.this, BillingActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                //После согласия на просмотр гостей
                //Скрываем блок с предупреждением и открываем блок с контентом
                paidGuests.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                //Получаем контент
                getGuests();

                // Свайп для обновления
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getGuests();
                    }
                });
            }
        }
    }

    private void getGuests() {
        firstView = false;
//        if (listGuestsNew.getFooterViewsCount() == 0) listGuestsNew.addFooterView(footerLoading);
        thatsAll = false;
        isLaunch = true;
        pageNumber = 0;
        listGuestsNew.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        profile = new Profile(getApplicationContext());
        profile.getGuests(pageNumber, this, new Profile.GetGuestsCallback() {
            @Override
            public void onSuccess(final Guest[] guests, Activity activity) {
                if (!notPayd) {
                    paidViewGuests();
                    notPayd = true;
                }
                pageNumber += 1;
                if (guests.length == 0) {
                    thatsAll = true;
                    listGuestsNew.removeFooterView(footerLoading);
                    listGuestsNew.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
                final ArrayList<Guest> newItem = new ArrayList<Guest>();
                Collections.addAll(newItem, guests);
                glAdapterNew = new GuestsListAdapter(activity, newItem);
                listGuestsNew.setAdapter(glAdapterNew);
                listGuestsNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != newItem.size()) {
                            Intent intent = new Intent(GuestsActivity.this, ProfileActivity.class);
                            intent.putExtra("profile_id", newItem.get(position).guest_id);
                            startActivity(intent);
                        }
                    }
                });
                isLaunch = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int error_code, String error_msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGuests();
                    }
                }, 5000);
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        getGuests();
                    }
                }, 5000);
            }
        });
    }

//    public void getNewGuests() {
//        if (!thatsAll && !isLaunch) {
//            isLaunch = true;
//            profile = new Profile(getApplicationContext());
//            profile.getGuests(pageNumber, this, new Profile.GetGuestsCallback() {
//                        @Override
//                        public void onSuccess(final Guest[] guests, Activity activity) {
//                            if ((guests.length == 0) || ((guests.length == 1))) {
//                                thatsAll = true;
//                                listGuestsNew.removeFooterView(footerLoading);
//                            } else {
//                                pageNumber = pageNumber + 1;
//                                ArrayList<Guest> defolt = new ArrayList<>();
//                                Collections.addAll(defolt, guests);
//                                glAdapterNew.addAll(defolt);
//                                glAdapterNew.notifyDataSetChanged();
//                            }
//                            isLaunch = false;
//                        }
//
//                        @Override
//                        public void onError(int error_code, String error_msg) {
//                            isLaunch = false;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override public void run() {
//                                    getNewGuests();
//                                }
//                            }, 5000);
//                        }
//
//                        @Override
//                        public void onInternetError() {
//                            isLaunch = false;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override public void run() {
//                                    getNewGuests();
//                                }
//                            }, 5000);
//                        }
//                    }
//            );
//        } else listGuestsNew.removeFooterView(footerLoading);
//    }

    private void paidViewGuests() {
        Billing.getInstance(getApplicationContext()).pay("LOOK_GUESTS", new Billing.PayCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int error_code, String error_msg) {

            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paidViewGuests();
                    }
                }, 5000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (stgs.getSettingInt("avtoGetGuests") == 1) {
            getMenuInflater().inflate(R.menu.guests, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_show:
                stgs.setSettingInt("avtoGetGuests", 0);
                Toast.makeText(getApplicationContext().getApplicationContext(), "Готово", Toast.LENGTH_LONG).show();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        if (firstView) {
            getToken();
        }
        super.onResume();
    }
}
