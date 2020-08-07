package com.example.transitionpratice.fragment;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.transitionpratice.MainActivity;
import com.example.transitionpratice.R;
import com.example.transitionpratice.adapter.ImagePagerAdapter;

public class ImagePagerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        ViewPager viewPager = (ViewPager)inflater.inflate(R.layout.image_pager_fragment, container, false);

        viewPager.setAdapter(new ImagePagerAdapter(getFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                MainActivity.curPosition = position;
            }
        });

        prepareTransition();


        return viewPager;
    }

    private void prepareTransition() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.grid_exit);

            setSharedElementEnterTransition(transition);
        }
    }
}
