package com.example.activitytest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.activitytest.R;
import com.example.activitytest.util.Ipsum;

public class ArticleFragment extends Fragment {
    public static final String POS_KEY = "curposition";

    int curPosition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        if (saveInstanceState != null){
            curPosition = saveInstanceState.getInt(POS_KEY, 0);
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        Bundle args = getArguments();
        if (args != null){
            updateArticle(args.getInt(POS_KEY));
        }else if (curPosition != -1){
            updateArticle(curPosition);
        }
    }

    public void updateArticle(int position){
        TextView textView = getActivity().findViewById(R.id.text);
        textView.setText(Ipsum.Articles[position]);
        curPosition = position;
    }


}
