package com.httpso_hello.hello.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.Structures.Region;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.activity.SettingsActivity;
import com.httpso_hello.hello.adapters.FlirtikiFragmentAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.CountryRegionCity;
import com.httpso_hello.hello.helper.Profile;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    private EditText userRostAuto;
    private EditText userVesAuto;
    private Calendar dateAndTime;
    public static int year;
    public static int month;
    public static int day;
    private Spinner matPol;
    private Spinner region;
    private Spinner sity;
    private ImageView newAvatar;
    private Settings stgs;

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
        userRostAuto = (EditText) rootView.findViewById(R.id.userRostAuto);
        userVesAuto = (EditText) rootView.findViewById(R.id.userVesAuto);
        dateAndTime = Calendar.getInstance();
        currentDateTime = (TextView) rootView.findViewById(R.id.currentDateTime);
        matPol = (Spinner) rootView.findViewById(R.id.mat_pol);
        region = (Spinner) rootView.findViewById(R.id.region);
        sity = (Spinner) rootView.findViewById(R.id.sity);
        newAvatar = (ImageView) rootView.findViewById(R.id.newAvatar);
        stgs = new Settings(getContext());
        setInitialDateTime();


//Заполнение спинеров Регионов и Городов
        String[] regionsArray = new String[CountryRegionCity.getRegionsForSpinner().length];
        for(int i = 0; i < CountryRegionCity.getRegionsForSpinner().length; i++){
            regionsArray[i] = CountryRegionCity.getRegionsForSpinner()[i].getName();
        }

        String[] citysArray = new String[CountryRegionCity.getCitysForSpinner().length];
        for(int i = 0; i < CountryRegionCity.getCitysForSpinner().length; i++){
            citysArray[i] =
                    CountryRegionCity.getCitysForSpinner()[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regionsArray);
        region.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, citysArray);
        sity.setAdapter(adapter2);
//Конец заполнения спинеров Регионов и Городов


//Профиль
        userEditName.setText(user.nickname);

        if(stgs.getSettingStr("user_avatar.micro") != null) {
            Picasso
                    .with(getContext())
                    .load(stgs.getSettingStr("user_avatar.micro"))
                    .transform(new CircularTransformation(0))
                    .into(newAvatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso
                                    .with(getContext())
                                    .load(Uri.parse(Constant.default_avatar))
                                    .transform(new CircularTransformation(0))
                                    .into(newAvatar);
                        }
                    });
        } else {
            Picasso
                    .with(getContext())
                    .load(Uri.parse(Constant.default_avatar))
                    .transform(new CircularTransformation(0))
                    .into(newAvatar);
        }

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

        if (user.rost != 0) userRostAuto.setText(Integer.toString(user.rost));
        if (user.ves != 0) userVesAuto.setText(Integer.toString(user.ves));

        if (user.mat_poloz != 0) {
            if (user.mat_poloz == 1) matPol.setSelection(0);
            else if (user.mat_poloz == 2) matPol.setSelection(1);
            else if (user.mat_poloz == 3) matPol.setSelection(2);
            else if (user.mat_poloz == 4) matPol.setSelection(3);
            else if (user.mat_poloz == 5) matPol.setSelection(4);
        }

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
                String userEditVesString = "";
                String userEditRostString = "";
                String userEditMatString = "";

                userEditNameString = userEditName.getText().toString();

                if (userEditGenderMan.isChecked()) genderString = "1";
                else if (userEditGenderWoman.isChecked()) genderString = "2";
                else genderString = "0";

                userEditRostString = userRostAuto.getText().toString();

                userEditVesString = userVesAuto.getText().toString();

                userEditPhoneString = userEditPhone.getText().toString();

                userEditSkypeString = userEditSkype.getText().toString();

                userEditAutoString = userEditAuto.getText().toString();

                userEditMatString = Integer.toString(matPol.getSelectedItemPosition() + 1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", userEditNameString);
                params.put("gender", genderString);
                params.put("phone", userEditPhoneString);
                if (day != 0) params.put("birth_date", ConverterDate.sendBirthdate(year, month, day));
                params.put("skype", userEditSkypeString);
                params.put("avto", userEditAutoString);
                params.put("ves", userEditVesString);
                params.put("rost", userEditRostString);
                params.put("mat_poloz", userEditMatString);

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
                        Toast.makeText(getContext().getApplicationContext(),
                                "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
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