package com.httpso_hello.hello.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import com.httpso_hello.hello.activity.SettingsActivity;
import com.httpso_hello.hello.adapters.FlirtikiFragmentAdapter;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Profile;

import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView currentDateTime;
    private Calendar dateAndTime;
    public static int year;
    public static int month;
    public static int day;

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
        dateAndTime = Calendar.getInstance();
        currentDateTime = (TextView) rootView.findViewById(R.id.currentDateTime);
        setInitialDateTime();


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

        if (user.birth_date != null) currentDateTime.setText(ConverterDate.getBirthdate(user.birth_date));
        else currentDateTime.setText("01 января 1990 г.");

        currentDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

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
                params.put("birth_date", ConverterDate.sendBirthdate(year, month, day));
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

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SettingsProfileFragment.year = year;
            SettingsProfileFragment.month = monthOfYear;
            SettingsProfileFragment.day = dayOfMonth;
            currentDateTime.setText(Integer.toString(dayOfMonth) + ConverterDate.getMonthName(monthOfYear + 1) + Integer.toString(year) + " г.");
        }
    };

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        if (user.birth_date != null) {
            dateAndTime.set(Calendar.YEAR, ConverterDate.getYear(user.birth_date));
            dateAndTime.set(Calendar.MONTH, ConverterDate.getMonth(user.birth_date));
            dateAndTime.set(Calendar.DAY_OF_MONTH, ConverterDate.getDay(user.birth_date));
        } else {
            dateAndTime.set(Calendar.YEAR, 1900);
            dateAndTime.set(Calendar.MONTH, 0);
            dateAndTime.set(Calendar.DAY_OF_MONTH, 1);
        }
    }

}