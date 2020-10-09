package com.sword.jetpackpratice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            ProductFragment fragment = new ProductFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, fragment, ProductFragment.TAG).commit();
        }
    }
}