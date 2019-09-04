package com.firmanjabar.submission4.feature.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.firmanjabar.submission4.feature.favorite.FavoriteFragment;
import com.firmanjabar.submission4.feature.movie.MovieFragment;
import com.firmanjabar.submission4.feature.tv.TvFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments = {new MovieFragment(), new TvFragment(), new FavoriteFragment()};

    public MainPagerAdapter(FragmentManager fm, AHBottomNavigation nav) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return fragments[0];
            case 1:
                return fragments[1];
            case 2:
                return fragments[2];
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
