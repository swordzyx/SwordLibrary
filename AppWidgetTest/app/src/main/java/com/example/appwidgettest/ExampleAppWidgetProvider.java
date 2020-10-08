package com.example.appwidgettest;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "MainAppWidgetProvider";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        Log.d(TAG, "onUpdate");

        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            String titlePrefix = MainActivity.loadTitlePref(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, titlePrefix);
        }
    }


    //when deletes Widget, delete the Prefs associated with the it
    @Override
    public void onDeleted(Context context, int[] appWidgetIds){
        Log.d(TAG, "onDeleted");

        for (int id : appWidgetIds) {
            MainActivity.deleteTitlePrefs(context, id);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnable");

        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName("com.example.appwidgettest", ".appwidget.ExampleBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onDisabled(Context context){
        Log.d(TAG, "onDisable");
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName("com.example.appwidgettest", ".appwidget.ExampleBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String titlePrefix){
        Log.d(TAG, "updateAppWidget appWidgetId = " + appWidgetId + "titlePrefix = " + titlePrefix);

        CharSequence text = context.getString(R.string.appwidget_text_format,
                MainActivity.loadTitlePref(context, appWidgetId), "0x" + Long.toHexString(SystemClock.elapsedRealtime()));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider);
        views.setTextViewText(R.id.appwidget_text, text);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
