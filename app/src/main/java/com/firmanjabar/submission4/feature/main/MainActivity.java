package com.firmanjabar.submission4.feature.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firmanjabar.submission4.R;
import com.firmanjabar.submission4.feature.main.adapter.MainPagerAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener{
    @BindView(R.id.container_layout) AHBottomNavigationViewPager container_layout;
    @BindView(R.id.bot_nav) AHBottomNavigation bot_nav;
    @BindView(R.id.toolbar) Toolbar toolbar;

    public static Boolean isFavViewLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        ArrayList<AHBottomNavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new AHBottomNavigationItem(getString(R.string.nav_movie), R.drawable.ic_movie));
        navigationItems.add(new AHBottomNavigationItem(getString(R.string.nav_tv_show), R.drawable.ic_tv));
        navigationItems.add(new AHBottomNavigationItem(getString(R.string.nav_favorite), R.drawable.ic_favorite));

        bot_nav.addItems(navigationItems);
        bot_nav.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bot_nav.setUseElevation(true);
        bot_nav.setAccentColor(Color.parseColor("#37B7B6"));
        bot_nav.setInactiveColor(Color.parseColor("#7537B7B6"));
        bot_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bot_nav.setTitleTextSizeInSp(14f,12f);
        bot_nav.setNotificationBackgroundColor(Color.parseColor("#d0021b"));
        bot_nav.setBehaviorTranslationEnabled(false);
        bot_nav.setOnTabSelectedListener(this);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), bot_nav);
        container_layout.setAdapter(mainPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reminder_setting:
                return true;
            case R.id.language_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        container_layout.setCurrentItem(position);
        if (position == 2){ isFavViewLoaded = true; }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
