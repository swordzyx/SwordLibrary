package com.example.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.activitytest.util.Constants;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    String saveString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            saveString = savedInstanceState.getString(Constants.SAVE_STRING);
        }

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        textView.setText(saveString);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        textView.setText(savedInstanceState.getString(Constants.SAVE_STRING));
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(Constants.SAVE_STRING, textView.getText().toString());

        super.onSaveInstanceState(outState);
    }


}
