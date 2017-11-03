package com.httpso_hello.hello.activity;

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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(getApplicationContext())
                .load(Constant.upload + photo)
                .into(photoImage);

        priceGift.setText("Цена: " + Integer.toString(price) + " баллов");

        Billing.getInstance(getApplicationContext())
                .getRaisingToken("paid_gift", new Billing.GetRaisingTokenCallback() {
                    @Override
                    public void onSuccess(final TokenReq token) {
                        balanceUser.setText("На счету " + token.balance + " баллов");
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Gifts.getInstance(getApplicationContext())
                                        .sendGifts(id, userId, comment.getText().toString(), isAnon.isChecked(), token.token, new Gifts.SendGiftsCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(getApplicationContext(), "Подарок успешно отправлен", Toast.LENGTH_LONG).show();
                                                finish();
                                            }

                                            @Override
                                            public void onError(int error_code, String error_msg) {
                                                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                                finish();
                                            }

                                            @Override
                                            public void onInternetError() {
                                                Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        });
                            }
                        });
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
