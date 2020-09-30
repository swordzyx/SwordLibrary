package com.example.appwidgettest;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AppWidgetActivity";
    private static final String PREFS_NAME = "com.example.appwidgettest.AppWidgetTest";
    private static final String PREF_PREFIX_KEY = "prefix_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetPrefix;

    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_main);

        mAppWidgetPrefix = findViewById(R.id.appwidget_prefix);
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        mAppWidgetPrefix.setText(loadTitlePref(MainActivity.this, mAppWidgetId));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            final Context context = MainActivity.this;

            String titlePrefix = mAppWidgetPrefix.getText().toString();
            //保存内容
            saveTitlePref(context, mAppWidgetId, titlePrefix);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //更新微件
            ExampleAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, titlePrefix);

            Intent result = new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, result);
            finish();
        }
    };

    private void saveTitlePref(Context context, int appWidgetId, String text){
        SharedPreferences.Editor sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(PREF_PREFIX_KEY + appWidgetId, text);
        sp.commit();
    }

    public static String loadTitlePref(Context context, int appWidgetId){
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String text = sp.getString(PREF_PREFIX_KEY + appWidgetId, null);

        if (text != null) {
            return text;
        } else {
            return context.getString(R.string.appwidget_prefix_default);
        }
    }

    public static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds, ArrayList<String> texts) {

    }

    public static void deleteTitlePrefs(Context context, int appWidgetId){

    }
}
