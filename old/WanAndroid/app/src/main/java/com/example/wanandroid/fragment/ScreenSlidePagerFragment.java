package com.example.wanandroid.fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wanandroid.R;

import androidx.fragment.app.Fragment;

public class ScreenSlidePagerFragment extends Fragment {
    int imageId ;
    public ScreenSlidePagerFragment(int resId){
        this.imageId = resId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ImageView view = (ImageView)inflater.inflate(R.layout.screen_slide_pager, container, false);
        view.setImageResource(imageId);
        return view;
    }


}
