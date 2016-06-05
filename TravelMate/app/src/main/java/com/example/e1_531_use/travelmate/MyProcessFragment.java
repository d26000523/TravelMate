package com.example.e1_531_use.travelmate;



import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e1_531_use.travelmate.tab_package.SlidingTabLayout;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProcessFragment extends Fragment {
    private SlidingTabLayout tabs;
    private ViewPager pager;
    private TabFragmentPagerAdapter adapter;


    public MyProcessFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance(){
        MyProcessFragment f = new MyProcessFragment();
        return f;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager(),getActivity());
        //super.onViewCreated(view, savedInstanceState);

        pager = (ViewPager) view.findViewById(R.id.view_pager);
        pager.setAdapter(adapter);
        //tabs
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabView(R.layout.tab_view,R.id.tabTeks);
        tabs.setBackgroundResource(R.color.colorPrimary);
        tabs.setSelectedIndicatorColors(R.color.colorAccent);
        tabs.setViewPager(pager);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_process, container, false);

    }

    //sliding tab內容按下後的切換

}
