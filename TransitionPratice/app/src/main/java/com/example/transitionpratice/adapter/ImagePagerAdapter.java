package com.example.transitionpratice.adapter;

import android.media.Image;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.transitionpratice.fragment.ImageFragment;
import com.example.transitionpratice.utils.Constants;
import com.example.transitionpratice.utils.ImageData;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {


    public ImagePagerAdapter(@NonNull Fragment fragment) {
        //为什么要使用子 View 的 FragmentManager 初始化？
        super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(ImageData.IMAGE_DRAWABLES[position]);
    }

    @Override
    public int getCount() {
        return ImageData.IMAGE_DRAWABLES.length;
    }
}
