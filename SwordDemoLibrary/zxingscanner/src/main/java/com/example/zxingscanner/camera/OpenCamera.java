package com.example.zxingscanner.camera;

import android.hardware.Camera;

public class OpenCamera {
    private final Camera camera;
    private final int orientation;
    
    public OpenCamera(Camera camera, int orientation){
        this.camera = camera;
        this.orientation = orientation;
    }
}
