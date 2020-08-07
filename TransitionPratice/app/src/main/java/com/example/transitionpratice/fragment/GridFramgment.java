package com.example.transitionpratice.fragment;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transitionpratice.MainActivity;
import com.example.transitionpratice.R;
import com.example.transitionpratice.adapter.GridAdapter;

public class GridFramgment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        recyclerView = (RecyclerView)inflater.inflate(R.layout.grid_fragment, container, false)
                .findViewById(R.id.grid_image);
        recyclerView.setAdapter(new GridAdapter(this));

        prepareTransition();
        postponeEnterTransition();

        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstance){
        super.onViewCreated(view, saveInstance);
        scrollToPosition();
    }

    private void scrollToPosition() {
        //为什么要设置这个监听器？
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                recyclerView.removeOnLayoutChangeListener(this);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                View v = layoutManager.findViewByPosition(MainActivity.curPosition);

                if (v == null || layoutManager.isViewPartiallyVisible(v, false, true)){
                    layoutManager.scrollToPosition(MainActivity.curPosition);
                }
            }
        });
    }


    private void prepareTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.grid_exit));
        }
    }


}
