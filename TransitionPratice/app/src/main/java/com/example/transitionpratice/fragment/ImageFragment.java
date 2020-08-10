package com.example.transitionpratice.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.transitionpratice.R;
import com.example.transitionpratice.utils.Constants;

public class ImageFragment extends Fragment {
    public static ImageFragment newInstance(@DrawableRes int imageRes){
        ImageFragment imageFragment = new ImageFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.IMG_RES, imageRes);
        imageFragment.setArguments(args);

        return imageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        final ImageView imageView = (ImageView)inflater.inflate(R.layout.frag_image, container, false);

        Bundle arguments = getArguments();
        @DrawableRes int imageRes = arguments.getInt(Constants.IMG_RES);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(String.valueOf(imageRes));

            Glide.with(this)
                    .load(imageRes)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            getParentFragment().startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            getParentFragment().startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imageView);
        }

        return imageView;
    }

}
