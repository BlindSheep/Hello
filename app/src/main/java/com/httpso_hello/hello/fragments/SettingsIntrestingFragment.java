package com.httpso_hello.hello.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.SettingsActivity;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 02.09.2017.
 */

public class SettingsIntrestingFragment extends Fragment {

    private User user;
    private EditText userEditFavoriteMusic;
    private EditText userEditFavoriteFilms;
    private EditText userEditFavoriteBooks;
    private EditText userEditFavoriteGames;
    private EditText userEditAnotherIntresting;
    private TextView saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        User user = (User) args.getParcelable("User");

        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_settings_intresting_fragment, container, false);
        userEditFavoriteMusic = (EditText) rootView.findViewById(R.id.userEditFavoriteMusic);
        userEditFavoriteFilms = (EditText) rootView.findViewById(R.id.userFavoriteFilms);
        userEditFavoriteBooks = (EditText) rootView.findViewById(R.id.userFavoriteBooks);
        userEditFavoriteGames = (EditText) rootView.findViewById(R.id.userFavoriteGames);
        userEditAnotherIntresting = (EditText) rootView.findViewById(R.id.userAnotherIntresting);
        saveButton = (TextView) rootView.findViewById(R.id.saveButton);

        if(user.music != null) userEditFavoriteMusic.setText(user.music);
        if(user.movies != null) userEditFavoriteFilms.setText(user.movies);
        if(user.books != null) userEditFavoriteBooks.setText(user.books);
        if(user.games != null) userEditFavoriteGames.setText(user.games);
        if(user.interes != null) userEditAnotherIntresting.setText(user.interes);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setText("Сохраняем...");
                String userEditFavoriteMusicString = userEditFavoriteMusic.getText().toString();
                String userFavoriteFilmsString = userEditFavoriteFilms.getText().toString();
                String userFavoriteBooksString = userEditFavoriteBooks.getText().toString();
                String userFavoriteGamesString = userEditFavoriteGames.getText().toString();
                String userAnotherIntrestingString = userEditAnotherIntresting.getText().toString();

                Map<String, String> params = new HashMap<String, String>();
                params.put("movies", userFavoriteFilmsString);
                params.put("books", userFavoriteBooksString);
                params.put("music", userEditFavoriteMusicString);
                params.put("games", userFavoriteGamesString);
                params.put("interes", userAnotherIntrestingString);

                GsonBuilder GB = new GsonBuilder();
                Gson gson = GB.create();

                String gsonString = gson.toJson(params);

                System.out.println(gsonString);

                Profile profile = new Profile(getContext());
                profile.editProfile(gsonString, new Profile.EditProfileCallback() {
                    @Override
                    public void onSuccess() {
                        saveButton.setText("Сохранить");
                        Toast.makeText(getContext().getApplicationContext(), "Все данные успешно сохранены", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        saveButton.setText("Сохранить");
                        Toast.makeText(getContext().getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onInternetError() {
                        saveButton.setText("Сохранить");
                        Toast.makeText(getContext().getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        return rootView;
    }

}