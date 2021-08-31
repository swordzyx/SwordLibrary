package com.sword.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.mlkit.vision.common.InputImage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private View.OnClickListener listener = view -> {
        if (view.getId() == R.id.scan_with_mlkit) {
            scanWithMlKit();
        }
    };

    private void scanWithMlKit() {
        MlKitScanner mlKitScanner = new MlKitScanner();

        //start camera, get InputImage


        //mlKitScanner.scanQRCode();
    }

    private InputImage gainInputImage() {

    }
}