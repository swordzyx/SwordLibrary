package com.sword.jetpackpratice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class UserProfileFragment extends Fragment {
    private UserProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new UserProfileViewModel();

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
}
