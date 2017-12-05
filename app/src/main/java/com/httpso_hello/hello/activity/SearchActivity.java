package com.httpso_hello.hello.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ListView;
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
    private ListView profilesList;
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
    private EditText inputID;
    private static int pageNumber;
    private boolean thatsAll = false;
    private boolean isLaunch = false;
    private View header;
    private View footerLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner cel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setHeader();
        setMenuItem("SearchActivity");

        context = this.getApplicationContext();
        this.profile = new Profile(this);
        header = getLayoutInflater().inflate(R.layout.footer6dp, null);
        footerLoading = getLayoutInflater().inflate(R.layout.footer_loading, null);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_blue_color_hello,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        ImageView search_profile_avatar = (ImageView) findViewById(R.id.search_profile_avatar);

        this.profilesList = (ListView) findViewById(R.id.listUsers);

        //Фильтр
        birth_date_from = (Spinner) findViewById(R.id.birth_date_from);
        birth_date_to = (Spinner) findViewById(R.id.birth_date_to);
        gender = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioButtonAll = (RadioButton) findViewById(R.id.radioButtonAll);
        RadioButton radioButtonMan = (RadioButton) findViewById(R.id.radioButtonMan);
        RadioButton radioButtonWoman = (RadioButton) findViewById(R.id.radioButtonWoman);
        cel = (Spinner) findViewById(R.id.cel);
        search = (Button) findViewById(R.id.search);
        inputID = (EditText) findViewById(R.id.enterID);
        pageNumber = 1;

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                thatsAll = false;
                pageNumber = 1;
                setProfiles();
            }
        });

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

        profilesList.addHeaderView(header);
        profilesList.addFooterView(footerLoading);

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

                        pageNumber = 1;
                        thatsAll = false;

                        setProfiles();
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
                    pageNumber = 1;
                    thatsAll = false;

                    setProfiles();
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
        if (profilesList.getFooterViewsCount() == 0) profilesList.addFooterView(footerLoading);
        thatsAll = false;
        isLaunch = true;
        pageNumber = 1;
        swipeRefreshLayout.setRefreshing(true);
        this.profile.searchProfiles(
                this.ageFrom,
                this.ageTo + 1,
                this.gender_str,
                this.pageNumber,
                cel.getSelectedItemPosition(),
                new Profile.SearchProfilesCallback() {
                    @Override
                    public void onSuccess(User[] users) {
                        pageNumber = pageNumber + 1;
                        if ((users.length == 0) || ((users.length == 1))) {
                            thatsAll = true;
                            profilesList.removeFooterView(footerLoading);
                        }
                        ArrayList<User> defolt = new ArrayList<>();
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].avatar != null) defolt.add(users[i]);
                        }
                        plAdapter = new ProfilesListAdapter(SearchActivity.this, defolt);
                        profilesList.setAdapter(plAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        isLaunch = false;
                    }

                    @Override
                    public void onError(int error_code, String error_msg) {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                setProfiles();
                            }
                        }, 5000);
                    }

                    @Override
                    public void onInternetError() {
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                setProfiles();
                            }
                        }, 5000);
                    }
                }
        );
    }

    //Подгружаем новых юзеров, метод вызывается из адаптера
    public void getNew(){
        if (!thatsAll && !isLaunch) {
            isLaunch = true;
            this.profile.searchProfiles(
                    this.ageFrom,
                    this.ageTo + 1,
                    this.gender_str,
                    this.pageNumber,
                    cel.getSelectedItemPosition(),
                    new Profile.SearchProfilesCallback() {
                        @Override
                        public void onSuccess(User[] users) {
                            if ((users.length == 0) || ((users.length == 1))) {
                                thatsAll = true;
                                profilesList.removeFooterView(footerLoading);
                            } else {
                                pageNumber = pageNumber + 1;
                                ArrayList<User> defolt = new ArrayList<>();
                                for (int i = 0; i < users.length; i++) {
                                    if (users[i].avatar != null) defolt.add(users[i]);
                                }
                                plAdapter.add(defolt);
                                plAdapter.notifyDataSetChanged();
                            }
                            isLaunch = false;
                        }

                        @Override
                        public void onError(int error_code, String error_msg) {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    getNew();
                                }
                            }, 5000);
                        }

                        @Override
                        public void onInternetError() {
                            isLaunch = false;
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    getNew();
                                }
                            }, 5000);
                        }
                    }
            );
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else super.onBackPressed();
    }
}