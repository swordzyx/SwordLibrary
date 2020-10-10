package com.sword.jetpackpratice;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends ListAdapter<CommentEntity, CommentAdapter.CommentViewHolder> {

    CommentClickCallback mCommentClickCallback;

    protected CommentAdapter(@NonNull CommentClickCallback commentClickCallback) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<CommentEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommentEntity oldItem, @NonNull CommentEntity newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommentEntity oldItem, @NonNull CommentEntity newItem) {
                return false;
            }
        }).build());

        this.mCommentClickCallback = commentClickCallback;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
