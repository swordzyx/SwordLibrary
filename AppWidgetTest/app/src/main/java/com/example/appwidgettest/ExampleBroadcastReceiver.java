package com.example.appwidgettest;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class ExampleBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "sword";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent=" + intent);

        String action = intent.getAction();
        if (action.equals(Intent.ACTION_TIMEZONE_CHANGED) || action.equals(Intent.ACTION_TIME_CHANGED)) {
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            ArrayList<Integer> appWidgetIds = new ArrayList<>();
            ArrayList<String> texts = new ArrayList<>();

            MainActivity.loadAllTitlePrefs(context, appWidgetIds, texts);

            final int N = appWidgetIds.size();
            for (int i=0; i<N; i++) {
                ExampleAppWidgetProvider.updateAppWidget(context, gm, appWidgetIds.get(i), texts.get(i));
            }
        }
    }
}
