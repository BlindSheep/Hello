package com.httpso_hello.hello.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class BillingActivity extends SuperMainActivity {

    private IInAppBillingService mService;
    private LinearLayout toBuy50Points;
    private LinearLayout toBuy100Points;
    private LinearLayout toBuy500Points;
    static final String buyOne = "tobuy50points";
    static final String buyTwo = "tobuy100points";
    static final String buyThree = "tobuy500points";
    private String verification;
    private ProgressBar progressBarBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        toBuy50Points = (LinearLayout) findViewById(R.id.toBuy50Points);
        toBuy100Points = (LinearLayout) findViewById(R.id.toBuy100Points);
        toBuy500Points = (LinearLayout) findViewById(R.id.toBuy500Points);
        progressBarBilling = (ProgressBar) findViewById(R.id.progressBarBilling);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillingActivity.this, ProfileActivity.class);
                intent.putExtra("profile_id", stgs.getSettingInt("user_id"));
                startActivity(intent);
                finish();
            }
        });
        ImageView headerImageView = (ImageView) headerLayout.findViewById(R.id.user_avatar_header);
        TextView user_name_and_age_header = (TextView) headerLayout.findViewById(R.id.user_name_and_age_header);
        TextView user_id_header = (TextView) headerLayout.findViewById(R.id.user_id_header);
        Picasso
                .with(getApplicationContext())
                .load(stgs.getSettingStr("user_avatar.micro"))
                .resize(300, 300)
                .centerCrop()
                .transform(new CircularTransformation(0))
                .into(headerImageView);
        if(stgs.getSettingStr("user_age") != null) {
            user_name_and_age_header.setText(stgs.getSettingStr("user_nickname") + ", " + stgs.getSettingStr("user_age"));
        } else user_name_and_age_header.setText(stgs.getSettingStr("user_nickname"));
        user_id_header.setText("Ваш ID " + Integer.toString(stgs.getSettingInt("user_id")));

        //Запросить на сервере кол-во баллов
        Profile.getInstance(getApplicationContext())
                .getBalance(new Profile.GetBalanceCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        toolbar.setSubtitle("На счету "+Integer.toString(balanceReq.balance)+" баллов");
                        progressBarBilling.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        progressBarBilling.setVisibility(View.GONE);
                    }

                    @Override
                    public void onInternetError() {
                        progressBarBilling.setVisibility(View.GONE);
                    }
                });

        toBuy50Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Billing.getInstance(getApplicationContext())
                        .getToken(new Billing.GetTokenCallback() {
                            @Override
                            public void onSuccess() {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onInternetError() {
                                verification = "test/test/test";
                            }
                        });

                Bundle buyIntentBundle = null;
                try {
                    buyIntentBundle = mService.getBuyIntent(3, getPackageName(), buyOne, "inapp", verification);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                PendingIntent pendingIntent = null;
                if (buyIntentBundle != null) {
                    pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                }

                try {
                    if (pendingIntent != null) {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
                    }
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        toBuy100Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Billing.getInstance(getApplicationContext())
                        .getToken(new Billing.GetTokenCallback() {
                            @Override
                            public void onSuccess() {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onInternetError() {
                                verification = "test/test/test";
                            }
                        });

                Bundle buyIntentBundle = null;
                try {
                    buyIntentBundle = mService.getBuyIntent(3, getPackageName(), buyTwo, "inapp", verification);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                PendingIntent pendingIntent = null;
                if (buyIntentBundle != null) {
                    pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                }

                try {
                    if (pendingIntent != null) {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
                    }
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        toBuy500Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Billing.getInstance(getApplicationContext())
                        .getToken(new Billing.GetTokenCallback() {
                            @Override
                            public void onSuccess() {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                verification = "test/test/test";
                            }

                            @Override
                            public void onInternetError() {
                                verification = "test/test/test";
                            }
                        });

                Bundle buyIntentBundle = null;
                try {
                    buyIntentBundle = mService.getBuyIntent(3, getPackageName(), buyThree, "inapp", verification);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                PendingIntent pendingIntent = null;
                if (buyIntentBundle != null) {
                    pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                }

                try {
                    if (pendingIntent != null) {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
                    }
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String productId = jo.getString("productId");
                    String purchaseTime = jo.getString("purchaseTime");
                    String developerPayload = jo.getString("developerPayload");

                    Toast.makeText(getApplicationContext(), "Вы успешно приобрели " + productId + " Время - " + purchaseTime + " Токен - " + developerPayload, Toast.LENGTH_LONG).show();
                    Billing.getInstance(getApplicationContext())
                            .sendToken(new Billing.SendTokenCallback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError(int error_code, String error_msg) {
                                }
                                @Override
                                public void onInternetError() {
                                }
                            });
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        }
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}
