package com.example.wanandroid.fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wanandroid.R;

import com.ms.banner.Banner;
import com.ms.banner.holder.BannerViewHolder;

import java.util.ArrayList;


public class HomeFragment extends BaseFragment {
    View mView;
    Banner banner;
    ArrayList<Integer> picList = new ArrayList<>();

    static boolean loadComplete = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.frag_home, viewGroup);
        mView = view;
        initBanner();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (banner != null && !banner.isStart()){
            banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (banner != null && banner.isStart()){
            banner.stopAutoPlay();
        }
    }

    private void initBanner() {
        initPicture();

        banner = mView.findViewById(R.id.banner);
        banner.setAutoPlay(true)
            .setPages(picList, new CustomViewHolder())
            .start();
    }

    private void initPicture() {
        picList.add(R.mipmap.test_picture1);
        picList.add(R.mipmap.test_picture2);
        picList.add(R.mipmap.test_picture3);
    }


    class CustomViewHolder implements BannerViewHolder<Integer>{


        @Override
        public View createView(Context context, int position, Integer data) {
            View view = LayoutInflater.from(context).inflate(R.layout.screen_slide_pager, null);
            ImageView imageView = view.findViewById(R.id.pager_image);

            RequestOptions options = new RequestOptions().override(40, 30).centerCrop().skipMemoryCache(true);
            Glide.with(context).load(data).apply(options).into(imageView);
            return view;
        }

    }

}
