package com.example.transitionpratice.fragment;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.transitionpratice.MainActivity;
import com.example.transitionpratice.R;
import com.example.transitionpratice.adapter.ImagePagerAdapter;

import java.util.List;
import java.util.Map;

public class ImagePagerFragment extends Fragment {
    ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        viewPager = (ViewPager)inflater.inflate(R.layout.image_pager_fragment, container, false);

        viewPager.setAdapter(new ImagePagerAdapter(this));
        viewPager.setCurrentItem(MainActivity.curPosition);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                MainActivity.curPosition = position;
            }
        });

        prepareTransition();

        if(saveInstanceState == null){
            postponeEnterTransition();
        }

        return viewPager;
    }

    private void prepareTransition() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.grid_exit);

            setSharedElementEnterTransition(transition);

            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    Fragment curFragment = (Fragment)viewPager.getAdapter().instantiateItem(viewPager, MainActivity.curPosition);
                    View view = curFragment.getView();
                    if (view == null){
                        return ;
                    }
                    sharedElements.put(names.get(0), view.findViewById(R.id.image));
                }
            });
        }
    }
}
