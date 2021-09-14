package com.black.app.scanknow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.black.outer.zxing.config.ZXingConfig;
import com.black.outer.zxing.view.ScanCodeView;

public class EKMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ek_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ZXingConfig config = new ZXingConfig();
        config.setFullScreenScan(false);

        surfaceView = findViewById(R.id.ek_main_sv);
        scanCodeView = findViewById(R.id.ek_main_scv);
        scanCodeView.setScanCodeConfig(config);
        scanCodeView.setBitmapCallback(new ScanCodeView.Callback(){

            @Override
            public void onSuccess(Bitmap b) {
                AppCompatImageView view = findViewById(R.id.flashLightIv);
                view.setImageBitmap(b);
            }

            @Override
            public void onError() {

            }
        });

//        viewfinderView.drawViewfinder();
    }

    ZXingConfig config;
    SurfaceView surfaceView;
    ScanCodeView scanCodeView;

    @Override
    protected void onResume() {
        super.onResume();
        scanCodeView.onResume(this, surfaceView);
//        CameraManager cameraManager = new CameraManager(getApplication(), config);
//        scanCodeView.setCameraManager(cameraManager, surfaceView);
    }

    @Override
    protected void onPause() {
        scanCodeView.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
