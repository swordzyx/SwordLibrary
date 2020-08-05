package com.example.activitytest.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.activitytest.util.Shakespeare;

public class DetailsFragment extends Fragment {
    public static DetailsFragment getInstance(int index){
        DetailsFragment  detailsFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    public int getShowIndex(){
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        if (container == null){
            return null;
        }

        ScrollView scrollView = new ScrollView(getActivity());

        TextView textView = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics());
        textView.setPadding(padding, padding, padding, padding);
        scrollView.addView(textView);

        textView.setText(Shakespeare.DIALOGUE[getShowIndex()]);
        return scrollView;
    }
}
