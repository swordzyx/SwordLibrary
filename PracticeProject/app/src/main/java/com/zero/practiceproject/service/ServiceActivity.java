package com.zero.practiceproject.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.zero.practiceproject.R;
import com.zero.practiceproject.activity.MainActivity;


public class ServiceActivity extends Activity {
    static final String TAG = "ServiceActivity";
    Intent startIntent ;
    Intent bindIntent;
    MyService myService;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
             Log.d(TAG, "on MyService Connected");
             MyService.MyBinder myBinder = (MyService.MyBinder)iBinder;
             myService = myBinder.getService();
             myService.provideService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "on MyService Disconnected");
        }
    };

    @Override
    public void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_service);
    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.start_service:
                //start MyService
                startIntent = new Intent(ServiceActivity.this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                if (startIntent != null || bindIntent != null){
                    Log.d(TAG, "stop servcie");
                    stopService(startIntent);
                    startIntent = null;
                }
                break;
            case R.id.bind_servcie:
                bindIntent = new Intent(ServiceActivity.this, MyService.class);
                bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind_servcie:
                unbindService(serviceConnection);
                bindIntent = null;
                break;
            case R.id.start_intent_servcie:
                Intent startIntentService = new Intent(ServiceActivity.this, HelloIntentService.class);
                startService(startIntentService);
                break;
            case R.id.start_job_intent_servcie:
                Intent jobIntentService = new Intent();
                jobIntentService.putExtra("data", "data");
                TestJobIntentService.enqueueWork(this, TestJobIntentService.class, 100, jobIntentService);
            default:
                break;
        }

    }


}