package com.httpso_hello.hello.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.HBoard;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddBoardActivity extends SuperMainActivity {

    private TextView boardCancel;
    private TextView boardSave;
    private EditText boardText;
    private TextView boardPhotos;
    private LinearLayout ll;
    private float density;
    private String photos = "";
    private String boardTextString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_board);

        boardCancel = (TextView) findViewById(R.id.boardCancel);
        boardSave = (TextView) findViewById(R.id.boardSave);
        boardText = (EditText) findViewById(R.id.boardText);
        boardPhotos = (TextView) findViewById(R.id.boardPhotos);
        ll = (LinearLayout) findViewById(R.id.boadrForImage);
        density = getApplicationContext().getResources().getDisplayMetrics().density;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                Intent intent = new Intent(AddBoardActivity.this, ProfileActivity.class);
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

        //Кнопка "Добавить фото"
        boardPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        //Кнопка "Отмена"
        boardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Кнопка "Сохранить"
        boardSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boardTextString = boardText.getText().toString();
                    if ((!boardTextString.isEmpty()) && (!photos.isEmpty())) {
                        HBoard hBoard = new HBoard(getApplicationContext());
                        hBoard.addBoard(boardTextString, new HBoard.AddBoardCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onError(int error_code, String error_message) {
                                Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onInternetError() {
                                Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    } else if (boardTextString.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Напишите текст объявления", Toast.LENGTH_LONG).show();
                    } else if (photos.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Добавьте хотябы одну фотографию", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }


    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 0, baos);
                        byte[] bytes = baos.toByteArray();
                        final String base64_code_ava;
                        base64_code_ava = Base64.encodeToString(bytes, 0);

                        //Показ миниатюры для отправки
                        ImageView iv = new ImageView(getApplicationContext());
                        iv.setPadding((int) (density * 5), (int) (density * 5), 0, (int) (density * 5));
                        Picasso.with(getApplicationContext())
                                .load(imageUri)
                                .resize(0, (int) (density * 100))
                                .into(iv);
                        ll.addView(iv);

                        this.photos = base64_code_ava;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                System.out.println("Какая-то ошибка");
                break;
        }
    }
}
