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
    private TextView balance;
    private TextView prise;
    private TextView error;
    private boolean firstView = true;

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
    }

    private void getToken() {
        paidGuests.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        // Запрос токена на просмотр гостей
        Billing.getInstance(getApplicationContext()).getRaisingToken(
                "paid_view_guests",
                new Billing.GetRaisingTokenCallback() {
                    @Override
                    public void onSuccess(final TokenReq token) {
                        balance.setText("У вас на счету " + token.balance + " баллов");
                        if (token.action_price == 1) prise.setText("Стоимость просмотра " + Integer.toString(token.action_price) + " балл");
                        else prise.setText("Стоимость просмотра " + Integer.toString(token.action_price) + " балла");
                        paidGuests.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setVisibility(View.GONE);

                        if (Integer.parseInt(token.balance) < token.action_price) {
                            error.setVisibility(View.VISIBLE);
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
                            go.setText("Продолжить");
                            go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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

                                    //Списываем деньги
                                    paidViewGuests(token.token);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getToken();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                getToken();
                            }
                        }, 5000);
                    }
                }
        );
    }

    private void getGuests() {
        firstView = false;
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

    @Override
    public void onResume() {
        if (firstView) {
            getToken();
        }
        super.onResume();
    }
}
