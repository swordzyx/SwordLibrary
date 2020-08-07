package com.example.transitionpratice.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.transitionpratice.MainActivity;
import com.example.transitionpratice.R;
import com.example.transitionpratice.fragment.ImagePagerFragment;
import com.example.transitionpratice.utils.ImageData;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    RequestManager reuqestManager;
    ViewHolderListener viewHolderListener;


    interface ViewHolderListener{
        void onLoadCompleted(ImageView view, int adapterPosition);

        void onItemClicked(View view, int adapterPosition);
    }

    public GridAdapter(Fragment fragment){
        reuqestManager = Glide.with(fragment);
        viewHolderListener = new GridViewHolderListener(fragment);
    }


    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new GridViewHolder(view, reuqestManager, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return ImageData.IMAGE_DRAWABLES.length;
    }

    class GridViewHolderListener implements ViewHolderListener{
        final Fragment fragment;

        public GridViewHolderListener(Fragment fragment){
            this.fragment = fragment;
        }

        @Override
        public void onLoadCompleted(ImageView view, int adapterPosition) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainActivity.curPosition != adapterPosition){
                return;
            }
            fragment.startPostponedEnterTransition();
        }

        @Override
        public void onItemClicked(View view, int adapterPosition) {
            MainActivity.curPosition = adapterPosition;

            ImagePagerFragment imagePagerFragment = new ImagePagerFragment();
            ImageView imageView = view.findViewById(R.id.card_image);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.getFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .addSharedElement(imageView, imageView.getTransitionName())
                        .replace(R.id.frame_layout, imagePagerFragment, ImagePagerFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    //ViewHolder 仅仅用于加载图片
    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        final RequestManager requestManager;
        final ViewHolderListener viewHolderListener;

        public GridViewHolder(@NonNull View itemView, @NonNull RequestManager requestManager, @NonNull ViewHolderListener viewHolderListener) {
            super(itemView);

            this.requestManager = requestManager;
            this.viewHolderListener = viewHolderListener;
            imageView = itemView.findViewById(R.id.card_image);
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
        }

        public void onBind(){
            int position = getAdapterPosition();
            setImage(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(String.valueOf(ImageData.IMAGE_DRAWABLES[position]));
            }
        }

        void setImage(final int position){
            reuqestManager
                    .load(ImageData.IMAGE_DRAWABLES[position])
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(imageView, position);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(imageView, position);
                            return false;
                        }
                    })
                    .into(imageView);
        }

        @Override
        public void onClick(View view) {
            viewHolderListener.onItemClicked(view, getAdapterPosition());
        }
    }
}
