package com.httpso_hello.hello.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.FlirtikItem;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.Region;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.activity.SettingsActivity;
import com.httpso_hello.hello.adapters.FlirtikiFragmentAdapter;
import com.httpso_hello.hello.helper.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.R.style.Animation_Dialog;
import static android.view.ViewGroup.*;

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
    private Settings stgs;
    private PopupWindow popUpForUpdateAvatar;
    private View popupViewForUpdateAvatar;
    private Bitmap selectedImage;
    private Uri imageUri;
    private File photoFile;
    private Activity activity;
    private PopupWindow popupPreviewAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        User user = (User) args.getSerializable("User");

        this.user = user;

        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.content_settings_profile_fragment, container, false);
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
        ((ImageView) rootView.findViewById(R.id.newAvatar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 popupViewForUpdateAvatar = getLayoutInflater(new Bundle()).inflate(R.layout.popup_for_new_photo, null);
                popUpForUpdateAvatar = new PopupWindow(popupViewForUpdateAvatar, ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
                popUpForUpdateAvatar.setWidth(displaymetrics.widthPixels);
                popUpForUpdateAvatar.setHeight(displaymetrics.heightPixels);
                popUpForUpdateAvatar.setAnimationStyle(Animation_Dialog);
                popUpForUpdateAvatar.showAtLocation(rootView.findViewById(R.id.settings_new_avatar_container), Gravity.CENTER, 0, 0);
                popupViewForUpdateAvatar.findViewById(R.id.popup_for_new_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpForUpdateAvatar.dismiss();
                    }
                });

// Загрузка новых фоток из галереи
                popupViewForUpdateAvatar.findViewById(R.id.newPhotoFromGalery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpForUpdateAvatar.dismiss();
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, 3);
                    }
                });

// Загрузка новых фоток с камеры
                popupViewForUpdateAvatar.findViewById(R.id.newPhotoFromCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Help.runTaskAfterPermission(
                                getActivity(),
                                new String[] {
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA
                                },
                                Help.REQUEST_ADD_PHOTO_CAMERA

                        )) {
                            openCameraWindow(4);
                        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onResult", "res");
    }

    public void openAvatarUpdateWindow(
            View.OnClickListener saveButtonClick
    ){
        try{
            Activity activity = getActivity();

            final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);

            DisplayMetrics displaymetrics = activity.getApplicationContext().getResources().getDisplayMetrics();

            View popupViewPreviewAvatar = activity.getLayoutInflater().inflate(R.layout.popup_accept_new_photo, null);
            popupPreviewAvatar = new PopupWindow(popupViewPreviewAvatar, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            popupPreviewAvatar.setWidth(displaymetrics.widthPixels);
            popupPreviewAvatar.setHeight(displaymetrics.heightPixels);
            popupPreviewAvatar.setAnimationStyle(Animation_Dialog);
            popupPreviewAvatar.showAtLocation(activity.findViewById(R.id.newAvatar), Gravity.CENTER, 0, 0);

            Picasso.with(activity.getApplicationContext())
                    .load(imageUri)
                    .resize(displaymetrics.widthPixels, displaymetrics.widthPixels)
                    .centerCrop()
                    .into(((ImageView) popupViewPreviewAvatar.findViewById(R.id.newPhoto)));
            ((TextView) popupViewPreviewAvatar.findViewById(R.id.savePhoto)).setText("Сохранить");
            ((LinearLayout) popupViewPreviewAvatar.findViewById(R.id.popup_accept_new_photo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupPreviewAvatar.dismiss();
                }
            });

            ((TextView) popupPreviewAvatar.getContentView().findViewById(R.id.savePhoto)).setOnClickListener(saveButtonClick);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener updateAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupPreviewAvatar.dismiss();
            popupPreviewAvatar.showAtLocation(getActivity().findViewById(R.id.newAvatar), Gravity.CENTER, 0, 0);


            final String base64_code_ava = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(imageUri, getActivity().getApplicationContext()),
                    0
            );
            Profile.getInstance(getActivity().getApplicationContext()).updateAvatar(
                    base64_code_ava,
                    "jpg",
                    new Profile.UpdateAvatarCallback() {
                        @Override
                        public void onSuccess(Image avatar) {
                            popupPreviewAvatar.dismiss();
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            popupPreviewAvatar.dismiss();
                        }

                        @Override
                        public void onInternetError() {
                            popupPreviewAvatar.dismiss();
                        }
                    }
            );
        }
    };
    public void openCameraWindow(int request_code){
        Intent photoCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            photoFile = null;
            try{
                photoFile = Help.createImageFile(getActivity());
            } catch (IOException ex){
                ex.printStackTrace();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.httpso_hello.hello.fileprovider",
                        photoFile
                );
                photoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(photoCameraIntent, 100);


            }


        }
    }
}