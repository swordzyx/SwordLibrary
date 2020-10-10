package com.sword.jetpackpratice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import com.sword.jetpackpratice.databinding.FragmentProductBinding;

public class ProductFragment extends Fragment {
    public static final String TAG = "product_fragment";

    private FragmentProductBinding productBinding;

    private CommentAdapter commentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productBinding = FragmentProductBinding.inflate(inflater, container, false);

        CommentAdapter commentAdapter = new CommentAdapter(mCommentClickCallback);
        productBinding.commentList.setAdapter(commentAdapter);

        return productBinding.getRoot();
    }

    private final CommentClickCallback mCommentClickCallback = comment -> {

    };
}
