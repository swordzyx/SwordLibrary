package com.example.bluetoothpratice;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

import androidx.fragment.app.FragmentTransaction;

import com.example.bluetoothpratice.bluetoothChat.BluetoothChatFragment;
import com.example.bluetoothpratice.common.SimpleActivityBase;
import com.example.bluetoothpratice.common.logger.Log;
import com.example.bluetoothpratice.common.logger.LogFragment;
import com.example.bluetoothpratice.common.logger.LogWrapper;
import com.example.bluetoothpratice.common.logger.MessageOnlyLogFilter;

public class MainActivity extends SimpleActivityBase {
    boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator viewAnimator = findViewById(R.id.sample_output);
                if (mLogShown){
                    viewAnimator.setDisplayedChild(1);
                }else
                    viewAnimator.setDisplayedChild(0);
                //刷新菜单
                invalidateOptionsMenu();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void initializeLogging() {
        LogWrapper logWrapper = new LogWrapper();

        Log.setLogNode(logWrapper);

        MessageOnlyLogFilter filter = new MessageOnlyLogFilter();
        logWrapper.setLogNode(filter);

        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        filter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}
