/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.black.outer.zxing.camera;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

public final class CameraSelector {


    private static final String TAG = "CameraSelector";

    public static Camera openWithDefault() {
        return open(-1, false);
    }

    public static Camera openBackCamera() {
        return open(-1, false);
    }

    public static Camera openFrontCamera() {
        return open(-1, true);
    }

    public static Camera open(int cameraId) {
        return open(cameraId, false);
    }

    private static Camera open(int cameraId, boolean isFront) {

        int facing = isFront?CameraInfo.CAMERA_FACING_FRONT:CameraInfo.CAMERA_FACING_BACK;

        int numCameras = Camera.getNumberOfCameras();
        if (numCameras <= 0) {
            Log.w(TAG, "No cameras!");
            return null;
        }

        boolean explicitRequest = cameraId >= 0;

        if (explicitRequest) {
            if (cameraId < numCameras) {
                return Camera.open(cameraId);
            }
        }

        int backCamera = 0;
        int index = numCameras;
        while (--index >= 0) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);

            if (cameraInfo.facing == facing){
                backCamera = index;
            }
        }

        return Camera.open(backCamera);

    }
}
