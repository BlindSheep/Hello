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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BalanceReq;
import com.httpso_hello.hello.helper.*;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.style.Animation_Dialog;

public class BillingActivity extends SuperMainActivity {

    private PopupWindow popUpWindow;
    private View popupView;
    private IInAppBillingService mService;
    static private String product = "tobuy10points";
    private ProgressBar progressBarBilling;
    private TextView buyBotton;
    private Spinner productSpinner;
    private TextView priceOfPoints;
    private TextView priceOfOnePoints;
    private TextView skidka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setHeader();
        setMenuItem("ServisesActivity");

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        popupView = getLayoutInflater().inflate(R.layout.popup_for_wait, null);
        popUpWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displaymetrics = getApplicationContext().getResources().getDisplayMetrics();
        popUpWindow.setWidth(displaymetrics.widthPixels);
        popUpWindow.setHeight(displaymetrics.heightPixels);
        popUpWindow.setAnimationStyle(Animation_Dialog);
        ((TextView) popupView.findViewById(R.id.textForWaiting)).setText("Идет оплата...");
        progressBarBilling = (ProgressBar) findViewById(R.id.progressBarBilling);
        buyBotton = (TextView) findViewById(R.id.buyBotton);
        productSpinner = (Spinner) findViewById(R.id.productSpinner);
        priceOfPoints = (TextView) findViewById(R.id.priceOfPoints);
        priceOfOnePoints = (TextView) findViewById(R.id.priceOfOnePoints);
        skidka = (TextView) findViewById(R.id.skidka);

//Запросить на сервере кол-во баллов
        Profile.getInstance(getApplicationContext())
                .getBalance(new Profile.GetBalanceCallback() {
                    @Override
                    public void onSuccess(BalanceReq balanceReq) {
                        getSupportActionBar().setSubtitle("На счету "+Integer.toString(balanceReq.balance)+" баллов");
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

//Сбрасываем все покупки, потом доработать сделать проверку, передалась ли вся инфа о покупке на сервер
        try {
            RefreshBuing();
        } catch (Exception e) {
            e.printStackTrace();
        }

//Прослушивание Спиннера
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    skidka.setVisibility(View.GONE);
                    priceOfPoints.setVisibility(View.GONE);
                    priceOfOnePoints.setVisibility(View.GONE);
                    product = "...";
                } else if (position == 1) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 0;
                    Double i = 20.0;
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/20));
                    product = "buy20points";
                } else if (position == 2) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 0;
                    Double i = 30.0;
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/30));
                    product = "buy30points";
                } else if (position == 3) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 0;
                    Double i = 40.0;
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/40));
                    product = "buy40points";
                } else if (position == 4) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 5;
                    Double i = 50.0 - (50.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/50));
                    product = "buy50points";
                } else if (position == 5) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 5;
                    Double i = 60.0 - (60.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/60));
                    product = "buy60points";
                } else if (position == 6) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 5;
                    Double i = 70.0 - (70.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/70));
                    product = "buy70points";
                } else if (position == 7) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 5;
                    Double i = 80.0 - (80.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/80));
                    product = "buy80points";
                } else if (position == 8) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 5;
                    Double i = 90.0 - (90.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/90));
                    product = "buy90points";
                } else if (position == 9) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 10;
                    Double i = 100.0 - (100.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/100));
                    product = "buy100points";
                } else if (position == 10) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 10;
                    Double i = 200.0 - (200.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/200));
                    product = "buy200points";
                } else if (position == 11) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 10;
                    Double i = 300.0 - (300.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/300));
                    product = "buy300points";
                } else if (position == 12) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 10;
                    Double i = 400.0 - (400.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/400));
                    product = "buy400points";
                } else if (position == 13) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 15;
                    Double i = 500.0 - (500.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/500));
                    product = "buy500points";
                } else if (position == 14) {
                    skidka.setVisibility(View.VISIBLE);
                    priceOfPoints.setVisibility(View.VISIBLE);
                    priceOfOnePoints.setVisibility(View.VISIBLE);
                    int skidkaInt = 15;
                    Double i = 1000.0 - (1000.0/100*skidkaInt);
                    skidka.setText("- " + Integer.toString(skidkaInt) + "%");
                    priceOfPoints.setText(Double.toString(i));
                    priceOfOnePoints.setText(Double.toString(i/1000));
                    product = "buy1000points";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//Кнопка покупки
        buyBotton.setOnClickListener(new
                                             View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (!product.equals("...")) {
//Запросить на сервере токен
                                                         progressBarBilling.setVisibility(View.VISIBLE);
                                                         Billing.getInstance(getApplicationContext())
                                                                 .getRaisingToken(
                                                                         "add_balance",
                                                                         new Billing.GetRaisingTokenCallback() {
                                                                             @Override
                                                                             public void onSuccess(String token) {
                                                                                 progressBarBilling.setVisibility(View.GONE);
                                                                                 Bundle buyIntentBundle = null;
                                                                                 try {
                                                                                     buyIntentBundle = mService.getBuyIntent(3, getPackageName(), product, "inapp", token);
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

                                                                             @Override
                                                                             public void onError(int error_code, String error_msg) {
                                                                                 Toast.makeText(getApplicationContext(), "Ошибка соединения с сервером" , Toast.LENGTH_LONG).show();
                                                                                 progressBarBilling.setVisibility(View.GONE);
                                                                             }

                                                                             @Override
                                                                             public void onInternetError() {
                                                                                 Toast.makeText(getApplicationContext(), "Ошибка интернет соединения" , Toast.LENGTH_LONG).show();
                                                                                 progressBarBilling.setVisibility(View.GONE);
                                                                             }
                                                                         });
                                                     } else Toast.makeText(getApplicationContext(), "Не выбрано сколько баллов Вы желаете купить!" , Toast.LENGTH_LONG).show();
                                                 }
                                             });
    }

    //Ответ Гугла о покупке
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    popUpWindow.showAtLocation((ScrollView) findViewById(R.id.SRV), Gravity.CENTER, 0, 0);
                    JSONObject jo = new JSONObject(purchaseData);
                    String productId = jo.getString("productId");
                    String developerPayload = jo.getString("developerPayload");
                    String purchaseToken = jo.getString("purchaseToken");
                    SendAddBalance(productId, developerPayload, purchaseToken);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Сообщаем Гуглу что мы доставили все продукты покупателю, чтоб покупатель заного смог их купить
    private void RefreshBuing() throws Exception {
        Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);

        if (ownedItems.getInt("RESPONSE_CODE") != 0) {
            throw new Exception("Error");
        }

        ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
        for (String purchaseData : purchaseDataList) {
            JSONObject o = new JSONObject(purchaseData);
            String purchaseToken = o.optString("token", o.optString("purchaseToken"));
            mService.consumePurchase(3, getPackageName(), purchaseToken);
        }
    }

    //Передаем сведенья о покупке на сервер
    private void SendAddBalance(final String productId, final String developerPayload, final String purchaseToken){
        Billing.getInstance(getApplicationContext())
                .addBalance(
                        ConverterDate.getSummaById(productId),
                        developerPayload,
                        new Billing.AddBalanceCallback() {
                            @Override
                            public void onSuccess() {
                                try {
                                    mService.consumePurchase(3, getPackageName(), purchaseToken);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), "Вы успешно приобрели " + Integer.toString(ConverterDate.getSummaById(productId)) + " баллов, спасибо за покупку!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }, new Help.ErrorCallback() {
                            @Override
                            public void onError(int error_code, String error_msg) {
//Сделана задержка 5 секунды, чтобы не наебнуть нахуй чёнибудь
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SendAddBalance(productId, developerPayload,
                                        purchaseToken);
                            }

                            @Override
                            public void onInternetError() {
//Сделана задержка 5 секунды, чтобы не наебнуть нахуй чёнибудь
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SendAddBalance(productId, developerPayload, purchaseToken);
                            }
                        });
    }

    //Соединение с билингом
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
        popUpWindow.dismiss();
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}