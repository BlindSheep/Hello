package com.httpso_hello.hello.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.helper.Auth;
import com.httpso_hello.hello.helper.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mixir on 02.09.2017.
 */

public class SettingsLookingForFragment extends Fragment {

    private User user;
    private RadioButton lookingForAll;
    private RadioButton lookingForMan;
    private RadioButton lookingForWoman;
    private Spinner birthDateFrom;
    private Spinner birthDateTo;
    private CheckBox checkBoxLookinFor0;
    private CheckBox checkBoxLookinFor1;
    private CheckBox checkBoxLookinFor2;
    private CheckBox checkBoxLookinFor3;
    private CheckBox checkBoxLookinFor4;
    private CheckBox checkBoxLookinFor5;
    private CheckBox checkBoxLookinFor6;
    private CheckBox checkBoxLookinFor7;
    private CheckBox checkBoxLookinFor8;
    private CheckBox checkBoxLookinFor9;
    private CheckBox checkBoxLookinFor10;
    private CheckBox checkBoxLookinFor11;
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

        View rootView = inflater.inflate(R.layout.content_settings_lookingfor_fragment, container, false);
        lookingForAll = (RadioButton) rootView.findViewById(R.id.lookingForAll);
        lookingForMan = (RadioButton) rootView.findViewById(R.id.lookingForMan);
        lookingForWoman = (RadioButton) rootView.findViewById(R.id.lookingForWoman);
        birthDateFrom = (Spinner) rootView.findViewById(R.id.birthDateFrom);
        birthDateTo = (Spinner) rootView.findViewById(R.id.birthDateTo);
        checkBoxLookinFor0 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor0);
        checkBoxLookinFor1 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor1);
        checkBoxLookinFor2 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor2);
        checkBoxLookinFor3 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor3);
        checkBoxLookinFor4 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor4);
        checkBoxLookinFor5 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor5);
        checkBoxLookinFor6 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor6);
        checkBoxLookinFor7 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor7);
        checkBoxLookinFor8 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor8);
        checkBoxLookinFor9 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor9);
        checkBoxLookinFor10 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor10);
        checkBoxLookinFor11 = (CheckBox) rootView.findViewById(R.id.checkBoxLookinFor11);
        saveButton = (TextView) rootView.findViewById(R.id.saveButton);

        //Ищу
        if (user.looking_for == 3) {
            lookingForAll.setChecked(true);
            lookingForMan.setChecked(false);
            lookingForWoman.setChecked(false);
        } else  if (user.looking_for == 2) {
            lookingForAll.setChecked(false);
            lookingForMan.setChecked(false);
            lookingForWoman.setChecked(true);
        } else  if (user.looking_for == 1) {
            lookingForAll.setChecked(false);
            lookingForMan.setChecked(true);
            lookingForWoman.setChecked(false);
        } else  if (user.looking_for == 0) {
            lookingForAll.setChecked(false);
            lookingForMan.setChecked(false);
            lookingForWoman.setChecked(false);
        }
        birthDateFrom.setSelection(user.looking_for_age);
        birthDateTo.setSelection(user.age_do);
        if (user.reg_cel != null) {
            char[] cel = user.reg_cel.toCharArray();
            if (cel.length >= 1) if (cel[0] == '1') checkBoxLookinFor0.setChecked(true);
            if (cel.length >= 2) if (cel[1] == '1') checkBoxLookinFor1.setChecked(true);
            if (cel.length >= 3) if (cel[2] == '1') checkBoxLookinFor2.setChecked(true);
            if (cel.length >= 4) if (cel[3] == '1') checkBoxLookinFor3.setChecked(true);
            if (cel.length >= 5) if (cel[4] == '1') checkBoxLookinFor4.setChecked(true);
            if (cel.length >= 6) if (cel[5] == '1') checkBoxLookinFor5.setChecked(true);
            if (cel.length >= 7) if (cel[6] == '1') checkBoxLookinFor6.setChecked(true);
            if (cel.length >= 8) if (cel[7] == '1') checkBoxLookinFor7.setChecked(true);
            if (cel.length >= 9) if (cel[8] == '1') checkBoxLookinFor8.setChecked(true);
            if (cel.length >= 10) if (cel[9] == '1') checkBoxLookinFor9.setChecked(true);
            if (cel.length >= 11) if (cel[10] == '1') checkBoxLookinFor10.setChecked(true);
            if (cel.length >= 12) if (cel[11] == '1') checkBoxLookinFor11.setChecked(true);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setText("Сохраняем...");
                String lookingForString = "";
                String birthDateFromString = "";
                String birthDateToString = "";
                String regCelString = "";

                if (lookingForMan.isChecked()) lookingForString = "1";
                else if (lookingForWoman.isChecked()) lookingForString = "2";
                else if (lookingForAll.isChecked()) lookingForString = "3";
                else lookingForString = "0";

                birthDateFromString = Integer.toString(birthDateFrom.getSelectedItemPosition());

                birthDateToString = Integer.toString(birthDateTo.getSelectedItemPosition());

                char[] celZn = new char[12];
                if (checkBoxLookinFor0.isChecked()) celZn[0] = '1'; else celZn[0] = '0';
                if (checkBoxLookinFor1.isChecked()) celZn[1] = '1'; else celZn[1] = '0';
                if (checkBoxLookinFor2.isChecked()) celZn[2] = '1'; else celZn[2] = '0';
                if (checkBoxLookinFor3.isChecked()) celZn[3] = '1'; else celZn[3] = '0';
                if (checkBoxLookinFor4.isChecked()) celZn[4] = '1'; else celZn[4] = '0';
                if (checkBoxLookinFor5.isChecked()) celZn[5] = '1'; else celZn[5] = '0';
                if (checkBoxLookinFor6.isChecked()) celZn[6] = '1'; else celZn[6] = '0';
                if (checkBoxLookinFor7.isChecked()) celZn[7] = '1'; else celZn[7] = '0';
                if (checkBoxLookinFor8.isChecked()) celZn[8] = '1'; else celZn[8] = '0';
                if (checkBoxLookinFor9.isChecked()) celZn[9] = '1'; else celZn[9] = '0';
                if (checkBoxLookinFor10.isChecked()) celZn[10] = '1'; else celZn[10] = '0';
                if (checkBoxLookinFor11.isChecked()) celZn[11] = '1'; else celZn[11] = '0';
                regCelString = new String(celZn);

                Map<String, String> params = new HashMap<String, String>();
                params.put("looking_for", lookingForString);
                params.put("looking_for_age", birthDateFromString);
                params.put("age_do", birthDateToString);
                params.put("reg_cel", regCelString);

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