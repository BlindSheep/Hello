package com.httpso_hello.hello.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Guest;
import com.httpso_hello.hello.adapters.GuestsListAdapter;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.Help;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.push_services.TokenReq;

import java.util.ArrayList;
import java.util.Collections;


public class GuestsActivity extends SuperMainActivity{

    private ListView listGuestsNew;
    private Profile profile;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GuestsListAdapter glAdapterNew;
    public int pageNumber = 1;
    private View footerLoading;
    private boolean thatsAll = false;
    private boolean isLaunch = false;
    private LinearLayout paidGuests;
    private TextView cancel;
    private TextView go;
    private String paidToken=null;

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
        cancel = (TextView) findViewById(R.id.cancel);
        go = (TextView) findViewById(R.id.go);

        paidGuests.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Запрос токена на просмотр гостей
        Billing.getInstance(getApplicationContext()).getRaisingToken(
                "paid_view_guests",
                new Billing.GetRaisingTokenCallback() {
                    @Override
                    public void onSuccess(TokenReq token) {
                        // Оплачиваем повторно
                        GuestsActivity.this.paidToken  = token.token;
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
//                                    showMessage(error_msg);
                    }

                    @Override
                    public void onInternetError() {
//                                    showMessage("Ошибка интернет соединения");
                    }
                }
        );
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paidToken!=null) {
                    //После согласия на просмотр гостей
                    //Скрываем блок с предупреждением и открываем блок с контентом
                    paidGuests.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    //Получаем контент
                    listGuestsNew.addFooterView(footerLoading);
                    getGuests();

                    // Свайп для обновления
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getGuests();
                        }
                    });

                    //Списываем деньги
                    paidViewGuests(paidToken);
                } else {
                    showMessage("Дождитесь получения информации");
                }
            }
        });
    }

    private void getGuests() {
        if (listGuestsNew.getFooterViewsCount() == 0) listGuestsNew.addFooterView(footerLoading);
        thatsAll = false;
        isLaunch = true;
        pageNumber = 1;
        swipeRefreshLayout.setRefreshing(true);
        profile = new Profile(getApplicationContext());
        profile.getGuests(pageNumber, this, new Profile.GetGuestsCallback() {
            @Override
            public void onSuccess(final Guest[] guests, Activity activity) {
                pageNumber += 1;
                if ((guests.length == 0) || ((guests.length == 1))) {
                    thatsAll = true;
                    listGuestsNew.removeFooterView(footerLoading);
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

    public void getNewGuests() {
        if (!thatsAll && !isLaunch) {
            isLaunch = true;
            profile = new Profile(getApplicationContext());
            profile.getGuests(pageNumber, this, new Profile.GetGuestsCallback() {
                        @Override
                        public void onSuccess(final Guest[] guests, Activity activity) {
                            if ((guests.length == 0) || ((guests.length == 1))) {
                                thatsAll = true;
                                listGuestsNew.removeFooterView(footerLoading);
                            } else {
                                pageNumber = pageNumber + 1;
                                ArrayList<Guest> defolt = new ArrayList<>();
                                Collections.addAll(defolt, guests);
                                glAdapterNew.addAll(defolt);
                                glAdapterNew.notifyDataSetChanged();
                            }
                            isLaunch = false;
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    getNewGuests();
                                }
                            }, 5000);
                        }

                        @Override
                        public void onInternetError() {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    getNewGuests();
                                }
                            }, 5000);
                        }
                    }
            );
        } else listGuestsNew.removeFooterView(footerLoading);
    }

    private void paidViewGuests(final String paidToken) {

        Billing.getInstance(getApplicationContext()).paidViewGuests(paidToken, new Billing.RemovePointsCallback() {
            @Override
            public void onSuccess() {
                //Оплата прошла успешно
            }
        }, new Help.ErrorCallback() {
            @Override
            public void onError(int error_code, String error_msg) {
                // С момента запроса токена прошло слишком много времени
                if(error_code == 401){
                    // Повторно получаем токен
                    Billing.getInstance(getApplicationContext()).getRaisingToken(
                            "paid_view_guests",
                            new Billing.GetRaisingTokenCallback() {
                                @Override
                                public void onSuccess(TokenReq token) {
                                    // Оплачиваем повторно
                                    GuestsActivity.this.paidToken  = token.token;
                                    paidViewGuests(token.token);
                                }

                                @Override
                                public void onError(int error_code, String error_msg) {
//                                    showMessage(error_msg);
                                }

                                @Override
                                public void onInternetError() {
//                                    showMessage("Ошибка интернет соединения");
                                }
                            }
                    );
                }
            }

            @Override
            public void onInternetError() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        paidViewGuests(paidToken);
                    }
                }, 5000);
            }
        });
    }
}
