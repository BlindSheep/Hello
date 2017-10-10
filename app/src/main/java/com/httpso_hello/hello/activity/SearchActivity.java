package com.httpso_hello.hello.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.User;
import com.httpso_hello.hello.adapters.ProfilesListAdapter;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends SuperMainActivity{

    private ProfilesListAdapter plAdapter;
    private GridView profilesList;
    private Spinner birth_date_from;
    private Spinner birth_date_to;
    private RadioGroup gender;
    private Button search;
    private Context context;
    private ImageView search_profile_avatar;
    private boolean filterIsChanged = false;
    private Integer ageFrom;
    private Integer ageTo;
    private Integer gender_str;
    private Profile profile;
    private LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private Integer filterState;
    private EditText inputID;
    private ProgressBar progressBarSearch;
    private ProgressBar progressBarSearchRefresh;
    private static int pageNumber;
    private boolean launching = false;
    private boolean thatsAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this.getApplicationContext();
        this.profile = new Profile(this);
        progressBarSearch = (ProgressBar) findViewById(R.id.progressBarSearch);
        progressBarSearchRefresh = (ProgressBar) findViewById(R.id.progressBarSearchRefresh);

        ImageView search_profile_avatar = (ImageView) findViewById(R.id.search_profile_avatar);

        this.profilesList = (GridView) findViewById(R.id.listUsers);

        //Фильтр
        birth_date_from = (Spinner) findViewById(R.id.birth_date_from);
        birth_date_to = (Spinner) findViewById(R.id.birth_date_to);
        gender = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioButtonAll = (RadioButton) findViewById(R.id.radioButtonAll);
        RadioButton radioButtonMan = (RadioButton) findViewById(R.id.radioButtonMan);
        RadioButton radioButtonWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
        search = (Button) findViewById(R.id.search);
        inputID = (EditText) findViewById(R.id.enterID);
        pageNumber = 1;

        progressBarSearch.setVisibility(View.VISIBLE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Во все активности перенести, заполнение шапки в меню
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
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

        //Если юзер не разу не фильтровал, произвести валидацию
        if (stgs.getSettingInt("ageFrom") == 0) {
            stgs.setSettingInt("ageFrom", 18);
        }
        if (stgs.getSettingInt("ageTo") == 0) {
            stgs.setSettingInt("ageTo", 35);
        }
        if (stgs.getSettingInt("gender") == 0) {
            stgs.setSettingInt("gender", 0);
        }


        //Подстановка значения в РадиоБаттон пола
        if (stgs.getSettingInt("gender") == 0) {
            radioButtonAll.setChecked(true);
            radioButtonMan.setChecked(false);
            radioButtonWoman.setChecked(false);
            filterIsChanged = false;
        } else  if (stgs.getSettingInt("gender") == 1) {
            radioButtonAll.setChecked(false);
            radioButtonMan.setChecked(true);
            radioButtonWoman.setChecked(false);
            filterIsChanged = false;
        } else  if (stgs.getSettingInt("gender") == 2) {
            radioButtonAll.setChecked(false);
            radioButtonMan.setChecked(false);
            radioButtonWoman.setChecked(true);
            filterIsChanged = false;
        }
        //Прослушивание значения в РадиоБутон пола
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SearchActivity.this.filterIsChanged = true;
                switch (checkedId) {
                    case R.id.radioButtonAll:
                        stgs.setSettingInt("gender", 0);
                        SearchActivity.this.gender_str = 0;
                        break;
                    case R.id.radioButtonMan:
                        stgs.setSettingInt("gender", 1);
                        SearchActivity.this.gender_str = 1;
                        break;
                    case R.id.radioButtonWoman:
                        stgs.setSettingInt("gender", 2);
                        SearchActivity.this.gender_str = 2;
                        break;
                }
            }
        });

        //Подстановка значение в спинер возраст от
        birth_date_from.setSelection(stgs.getSettingInt("ageFrom"));
        //Прослушивание спинера возраст от
        //Подстановка значение в спинер возраст до
        birth_date_to.setSelection(stgs.getSettingInt("ageTo"));
        //Прослушивание спинера возраст до
        birth_date_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                stgs.setSettingInt("ageFrom", selectedItemPosition);
                SearchActivity.this.ageFrom = selectedItemPosition;
                SearchActivity.this.filterIsChanged = true;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        birth_date_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                stgs.setSettingInt("ageTo", selectedItemPosition);
                SearchActivity.this.ageTo = selectedItemPosition;
                SearchActivity.this.filterIsChanged = true;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Прослушивание скрытия фильтра(обновление активности)
        this.llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet_search);
        this.bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // Настройка колбэков при изменениях
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // обновление Активности после скрытия фильтра
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    if (SearchActivity.this.filterIsChanged) {

                        progressBarSearch.setVisibility(View.VISIBLE);
                        pageNumber = 1;
                        thatsAll = false;

                        SearchActivity.this.refreshProfiles();
                        filterIsChanged = false;
                    }
                }
                inputID.setText("");
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        this.llBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SearchActivity.this.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    SearchActivity.this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return;
                }
                SearchActivity.this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                inputID.setText("");
                return;
            }
        });

        //Прослушивание кнопки поиска (перезапускаем активность с новыми значениями фильтра)
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String searchIDSrt = "";
                searchIDSrt = inputID.getText().toString().trim();
                int searchID = 0;
                if (inputID.getText().toString().equals("")) searchID = 0;
                else searchID = Integer.parseInt(searchIDSrt);
                if (searchID == 0){

                    progressBarSearch.setVisibility(View.VISIBLE);
                    pageNumber = 1;
                    thatsAll = false;

                    ((SearchActivity)view.getContext()).refreshProfiles();
                    SearchActivity.this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                    intent.putExtra("profile_id", searchID);
                    startActivity(intent);
                    SearchActivity.this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    inputID.setText("");
                }
            }
        });

            this.gender_str = stgs.getSettingInt("gender");
            this.ageFrom = stgs.getSettingInt("ageFrom");
            this.ageTo = stgs.getSettingInt("ageTo");

            this.setProfiles();
            filterIsChanged = false;

    }

    private void setProfiles() {
        this.profile.searchProfiles(this.ageFrom, this.ageTo + 1, this.gender_str, this.pageNumber,
                new Profile.SearchProfilesCallback() {
                    @Override
                    public void onSuccess(User[] users) {

                        ArrayList<User> defolt = new ArrayList<>();
                        Collections.addAll(defolt, users);
                        plAdapter = new ProfilesListAdapter(SearchActivity.this, defolt);

                        profilesList.setAdapter(plAdapter);

                        profilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Обработчик клика по юзеру
                                User user = ((ProfilesListAdapter) parent.getAdapter()).getItem(position);
                                // Открытие профиля
                                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                                intent.putExtra("profile_id", user.id);
                                intent.putExtra("profile_nickname", " " + user.nickname);
                                // TODO: 01.08.2017 Добавить проверку размеров
                                if (user.avatar == null) {
                                    intent.putExtra("avatar", Constant.default_avatar);
                                } else {
                                    intent.putExtra("avatar", user.avatar.micro);
                                }
                                startActivity(intent);
                            }
                        });
                        pageNumber = pageNumber + 1;
                        progressBarSearch.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void refreshProfiles(){
        this.profile.searchProfiles(this.ageFrom, this.ageTo + 1, this.gender_str, this.pageNumber,
                new Profile.SearchProfilesCallback() {
                    @Override
                    public void onSuccess(User[] users) {
                        ArrayList<User> defolt  = new ArrayList<>();
                        Collections.addAll(defolt, users);
                        plAdapter = new ProfilesListAdapter(SearchActivity.this, defolt);
                        profilesList.setAdapter(plAdapter);

                        pageNumber = pageNumber + 1;
                        progressBarSearch.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onInternetError() {
                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    //Подгружаем новых юзеров, метод вызывается из адаптера
    public void getNew(){
        if (!thatsAll) {
            progressBarSearchRefresh.setVisibility(View.VISIBLE);
            this.profile.searchProfiles(this.ageFrom, this.ageTo + 1, this.gender_str, this.pageNumber,
                    new Profile.SearchProfilesCallback() {
                        @Override
                        public void onSuccess(User[] users) {
                            if (users.length == 0) thatsAll = true;
                            ArrayList<User> defolt = new ArrayList<>();
                            Collections.addAll(defolt, users);
                            plAdapter.add(defolt);
                            plAdapter.notifyDataSetChanged();

                            pageNumber = pageNumber + 1;
                            progressBarSearchRefresh.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                            progressBarSearchRefresh.setVisibility(View.GONE);
                        }

                        @Override
                        public void onInternetError() {
                            Toast.makeText(getApplicationContext(), "Ошибка интернет соединения", Toast.LENGTH_LONG).show();
                            progressBarSearchRefresh.setVisibility(View.GONE);
                        }
                    }
            );
        }
    }
}