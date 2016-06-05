package com.example.e1_531_use.travelmate;

import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by E1-531-USE on 2016/4/29.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mcontext;
    private String[] titles = {"正在進行的行程","規劃中的行程","已完成的行程"};
    private int heighticon;
    public TabFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mcontext = context;

   }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new PlanningProcessFragment();
                break;
            case 1:
                fragment = new RunningProcessFragment();
                break;
            case 2:
                fragment = new EndingProcessFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }
}
