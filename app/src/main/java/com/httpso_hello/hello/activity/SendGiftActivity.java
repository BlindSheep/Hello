package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.Billing;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Gifts;
import com.httpso_hello.hello.helper.push_services.TokenReq;
import com.squareup.picasso.Picasso;

public class SendGiftActivity extends AppCompatActivity {

    private int id;
    private int price;
    private String photo;
    private int userId;
    private ImageView photoImage;
    private TextView priceGift;
    private TextView balanceUser;
    private TextView send;
    private CheckBox isAnon;
    private EditText comment;
    private TextView dontHavePoints;
    private ImageView back;
    private ImageView accept;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_send_gift);

        id = extras.getInt("id");
        price = extras.getInt("price");
        photo = extras.getString("photo");
        userId = extras.getInt("user_id");
        photoImage = (ImageView) findViewById(R.id.photo);
        priceGift = (TextView) findViewById(R.id.price);
        balanceUser = (TextView) findViewById(R.id.balance);
        send = (TextView) findViewById(R.id.send);
        isAnon = (CheckBox) findViewById(R.id.isAnon);
        comment = (EditText) findViewById(R.id.comment);
        dontHavePoints = (TextView) findViewById(R.id.dontHavePoints);
        back = (ImageView) findViewById(R.id.back);
        accept = (ImageView) findViewById(R.id.accept);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Picasso.with(getApplicationContext())
                .load(Constant.upload + photo)
                .into(photoImage);

        priceGift.setText("Цена: " + Integer.toString(price) + " баллов");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAll();
            }
        });
    }

    private void setAll() {
        swipeRefreshLayout.setRefreshing(true);
        Billing.getInstance(getApplicationContext())
                .getRaisingToken("paid_gift", new Billing.GetRaisingTokenCallback() {
                    @Override
                    public void onSuccess(final TokenReq token) {
                        balanceUser.setText("На счету: " + token.balance + " баллов");
                        swipeRefreshLayout.setRefreshing(false);
                        //Если хватает денег
                        if (Integer.parseInt(token.balance) >= price) {
                            send.setVisibility(View.GONE);
                            dontHavePoints.setVisibility(View.GONE);
                            accept.setVisibility(View.VISIBLE);
                            accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    swipeRefreshLayout.setRefreshing(true);
                                    int anon = 0;
                                    if(isAnon.isChecked()){
                                        anon = 1;
                                    }
                                    Gifts.getInstance(getApplicationContext())
                                            .sendGifts(id, userId, comment.getText().toString(), anon, token.token, price, new Gifts.SendGiftsCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(getApplicationContext(), "Подарок успешно отправлен", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }

                                                @Override
                                                public void onError(int error_code, String error_msg) {
                                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }

                                                @Override
                                                public void onInternetError() {
                                                    Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }
                                            });
                                }
                            });
                        } else {
                            //Если не хватает денег
                            accept.setVisibility(View.GONE);
                            send.setVisibility(View.VISIBLE);
                            dontHavePoints.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SendGiftActivity.this, BillingActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(int error_code, String error_msg) {

                    }

                    @Override
                    public void onInternetError() {

                    }
                });
    }

    @Override
    public void onResume () {
        setAll();
        super.onResume();
    }
}
