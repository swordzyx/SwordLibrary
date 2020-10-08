package com.example.bluetoothpratice.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bluetoothpratice.common.logger.Log;
import com.example.bluetoothpratice.common.logger.LogWrapper;

public class SimpleActivityBase extends AppCompatActivity {
    public static final String TAG = "SimpleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        initializeLogging();
    }

    protected void initializeLogging() {
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }

}
