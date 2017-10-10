package com.httpso_hello.hello.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.adapters.FlirtikiFragmentAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 02.09.2017.
 */

public class SettingsProfileFragment extends Fragment {

    private User user;
    private TextView saveButton;
    private EditText userEditName;
    private RadioButton userEditGenderMan;
    private RadioButton userEditGenderWoman;
    private EditText userEditPhone;
    private EditText userEditSkype;
    private EditText userEditAuto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        User user = (User) args.getSerializable("User");

        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_settings_profile_fragment, container, false);
        userEditAuto = (EditText) rootView.findViewById(R.id.userEditAuto);
        saveButton = (TextView) rootView.findViewById(R.id.saveButton);
        userEditName = (EditText) rootView.findViewById(R.id.userEditName);
        userEditGenderMan = (RadioButton) rootView.findViewById(R.id.userEditGenderMan);
        userEditGenderWoman = (RadioButton) rootView.findViewById(R.id.userEditGenderWoman);
        userEditPhone = (EditText) rootView.findViewById(R.id.userEditPhone);
        userEditSkype = (EditText) rootView.findViewById(R.id.userEditSkype);


        //Профиль
        userEditName.setText(user.nickname);

        if (user.gender == 0) {
            userEditGenderMan.setChecked(false);
            userEditGenderWoman.setChecked(false);
        } else if (user.gender == 1){
            userEditGenderMan.setChecked(true);
            userEditGenderWoman.setChecked(false);
        } else if (user.gender == 2){
            userEditGenderMan.setChecked(false);
            userEditGenderWoman.setChecked(true);
        }

        userEditAuto.setText(user.avto);

        //Контакты
        userEditPhone.setText(user.phone);
        userEditSkype.setText(user.skype);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setText("Сохраняем...");
                String userEditNameString = "";
                String genderString = "";
                String userEditPhoneString = "";
                String userEditSkypeString = "";
                String userEditAutoString = "";

                userEditNameString = userEditName.getText().toString();

                if (userEditGenderMan.isChecked()) genderString = "1";
                else if (userEditGenderWoman.isChecked()) genderString = "2";
                else genderString = "0";

                userEditPhoneString = userEditPhone.getText().toString();

                userEditSkypeString = userEditSkype.getText().toString();

                userEditAutoString = userEditAuto.getText().toString();

                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", userEditNameString);
                params.put("gender", genderString);
                params.put("phone", userEditPhoneString);
                params.put("skype", userEditSkypeString);
                params.put("avto", userEditAutoString);

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