package com.example.wanandroid.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    //static final int PAGER_NUM = 4;
    List<Fragment> items ;
    List<String> title ;

    public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> items, List<String> title) {
        super(fm);
        this.items = items;
        this.title = title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return items.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return title.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
