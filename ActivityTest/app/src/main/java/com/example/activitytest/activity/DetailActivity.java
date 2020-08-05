package com.example.activitytest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.activitytest.R;
import com.example.activitytest.fragment.DetailsFragment;
import com.example.activitytest.util.Shakespeare;

public class DetailActivity extends AppCompatActivity {
    TextView textView;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);

        /*if (getIntent() != null){
            position = getIntent().getIntExtra("position", 0);
        }

        textView = findViewById(R.id.detail);
        textView.setText(Shakespeare.DIALOGUE[position]);*/

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return ;
        }

        //复用 DetailFragment
        if (savedInstanceState == null){
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, detailsFragment).commit();
        }
    }
}
