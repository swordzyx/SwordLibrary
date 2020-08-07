package com.example.transitionpratice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.transitionpratice.fragment.GridFramgment;
import com.example.transitionpratice.utils.Constants;

public class MainActivity extends AppCompatActivity {
    public static int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            curPosition = savedInstanceState.getInt(Constants.IMG_POSITION, -1);
        }

        getSupportFragmentManager().beginTransaction()
            .add(R.id.frame_layout, new GridFramgment(), GridFramgment.class.getSimpleName())
            .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putInt(Constants.IMG_POSITION, curPosition);
    }
}
