/*
 * Copyright (C) 2008 ZXing authors
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.black.outer.zxing.config.ZXingConfig;
import com.black.outer.zxing.decode.DecodeFormat;
import com.black.outer.zxing.view.ScanCodeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 *
 */
public final class CameraManager {

    private static final String TAG = "CameraManager";

    private final Context context;
    private ZXingConfig zXingConfig;
    private Camera camera;
    private CameraConfig cameraConfig;
    private Rect framingRect;
    private AutoFocusControl autoFocusManager;
    private Rect framingRectInPreview;
    private boolean initialized;
    private boolean previewing;
    private int requestedCameraId = -1;
    private int requestedFramingRectWidth;
    private int requestedFramingRectHeight;

    public CameraManager(Context context, ZXingConfig zXingConfig) {
        this.context = context;
        this.zXingConfig = zXingConfig;
    }

    public synchronized void openDriver(SurfaceHolder holder)
            throws IOException {
        Camera theCamera = camera;

        if (theCamera == null) {

            if (requestedCameraId >= 0) {
                theCamera = CameraSelector.open(requestedCameraId);
            } else {
                theCamera = CameraSelector.openBackCamera();
            }

            if (theCamera == null) {
                throw new IOException();
            }
            camera = theCamera;
        }
        theCamera.setPreviewDisplay(holder);
        Log.e("Black","setPreviewDisplay");

        if (!initialized) {
            initialized = true;
            cameraConfig.initFromCameraParameters(theCamera);
            if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
                setManualFramingRect(requestedFramingRectWidth,
                        requestedFramingRectHeight);
                requestedFramingRectWidth = 0;
                requestedFramingRectHeight = 0;
            }
        }

        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters
                .flatten(); // Save these, temporarily
        try {
            cameraConfig.setDesiredCameraParameters(theCamera);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG,
                    "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: "
                    + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    cameraConfig.setDesiredCameraParameters(theCamera);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG,
                            "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

    }

    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
            // Make sure to clear these each time we close the camera, so that
            // any scanning rect
            // requested by intent is forgotten.
            framingRect = null;
            framingRectInPreview = null;
        }
    }


    /*切换闪光灯*/
    public boolean switchFlashLight() {
        Camera.Parameters parameters = camera.getParameters();

        Message msg = new Message();

        String flashMode = parameters.getFlashMode();
        boolean state = false;
        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            /*关闭闪光灯*/
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            /*打开闪光灯*/
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            state = true;
        }
        camera.setParameters(parameters);
        return state;
    }


    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public synchronized void startPreview() {
        Log.e("Black","startPreview called " + previewing);
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            Log.e("Black","startPreview ...");
            previewing = true;
            autoFocusManager = new AutoFocusControl(camera);
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewing = false;
        }
    }

    MultiFormatReader multiFormatReader;
    ScanCodeView scanCodeView;
    public synchronized void setOneShotPreviewCallback(ScanCodeView scanCodeView) {
        Log.d("Black","setOneShotPreviewCallback called~");
        Camera theCamera = camera;
        if(scanCodeView != null){
            this.scanCodeView = scanCodeView;
        }
        if(this.scanCodeView == null){
            return;
        }

        final ScanCodeView scanCodeViewNew = this.scanCodeView;
        Log.d("Black","setOneShotPreviewCallback called~1");
        if (theCamera != null && previewing) {
            theCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Point cameraResolution = cameraConfig.getCameraResolution();
                    int width = cameraResolution.x;
                    int height = cameraResolution.y;
                    Result rawResult = null;
                    Log.d("Black","setOneShotPreviewCallback called~2");

                    byte[] rotatedData = new byte[data.length];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            rotatedData[x * height + height - y - 1] = data[x + y * width];
                        }
                    }
                    int tmp = width; // Here we are swapping, that's the difference to #11
                    width = height;
                    height = tmp;
                    data = rotatedData;

                    PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);

                    if(multiFormatReader == null){
                        multiFormatReader = new MultiFormatReader();
                        ConcurrentHashMap<DecodeHintType, Object> map = new ConcurrentHashMap<>();
                        Vector<BarcodeFormat> decodeFormats = new Vector<>();

                        /*是否解析有条形码（一维码）*/
                        decodeFormats.addAll(DecodeFormat.ONE_D_FORMATS);
                        decodeFormats.addAll(DecodeFormat.DATA_MATRIX_FORMATS);
                        decodeFormats.addAll(DecodeFormat.QR_CODE_FORMATS);
                        map.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
                        map.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                        map.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ResultPointCallback(){
                            @Override
                            public void foundPossibleResultPoint(ResultPoint point) {
                                scanCodeViewNew.addPossibleResultPoint(point);
                            }
                        });
                        multiFormatReader.setHints(map);
                        Log.d("Black","setOneShotPreviewCallback called~3");
                    }
                    if (source != null) {
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        Log.d("Black","setOneShotPreviewCallback called~4");
                        try {
                            Log.d("Black","setOneShotPreviewCallback bitmap -> " + bitmap.getHeight() + bitmap.getWidth());
                            rawResult = multiFormatReader.decodeWithState(bitmap);
                            Log.d("Black","setOneShotPreviewCallback called~5");
                            if (rawResult != null) {
                                Log.i("Black","Decode Success ~ " + rawResult.getText());

                                int width1 = source.getThumbnailWidth();
                                int height1 = source.getThumbnailHeight();
                                Bitmap bitmap1 = Bitmap.createBitmap(source.renderThumbnail(), 0, width1, width1, height1, Bitmap.Config.ARGB_8888);

                                scanCodeViewNew.testBitmap(bitmap1);

                            } else {
                                Log.e("Black","Decode Error ~");
                                setOneShotPreviewCallback(null);
                            }
                        } catch (ReaderException e) {
                            Log.e("Black","setOneShotPreviewCallback ReaderException -> " + e.toString());
                            setOneShotPreviewCallback(null);
                        } finally {
                            multiFormatReader.reset();
                        }
                    }
                }
            });
        }
    }

    /*取景框*/
    public synchronized Rect getFramingRect() {
        Point screenResolution = cameraConfig.getScreenResolution();
        return getFramingRect(screenResolution);
    }

    public synchronized Rect getFramingRect(Point screenResolution) {
        if (framingRect == null) {
            if (camera == null) {
                return null;
            }
            if (screenResolution == null) {
                // Called early, before init even finished
                return null;
            }

            cameraConfig.setScreenResolution(screenResolution);

            int screenResolutionX = screenResolution.x;

            int width = (int) (screenResolutionX * 0.6);
            int height = width;


            /*水平居中  偏上显示*/
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 5;

            framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
                    topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }


    /**
     * Like {@link #getFramingRect} but coordinates are in terms of the preview
     * frame, not UI / screen.
     *
     * @return {@link Rect} expressing barcode scan area in terms of the preview
     * size
     */
    public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }
            Rect rect = new Rect(framingRect);
            Point cameraResolution = cameraConfig.getCameraResolution();
            Point screenResolution = cameraConfig.getScreenResolution();
            if (cameraResolution == null || screenResolution == null) {
                // Called early, before init even finished
                return null;
            }

            /******************** 竖屏更改1(cameraResolution.x/y互换) ************************/
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    /**
     * Allows third party apps to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means
     *                 "no preference".
     */
    public synchronized void setManualCameraId(int cameraId) {
        requestedCameraId = cameraId;
    }

    /**
     * Allows third party apps to specify the scanning rectangle dimensions,
     * rather than determine them automatically based on screen resolution.
     *
     * @param width  The width in pixels to scan.
     * @param height The height in pixels to scan.
     */
    public synchronized void setManualFramingRect(int width, int height) {
        if (initialized) {
            Point screenResolution = cameraConfig.getScreenResolution();
            if (width > screenResolution.x) {
                width = screenResolution.x;
            }
            if (height > screenResolution.y) {
                height = screenResolution.y;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 5;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
                    topOffset + height);
            Log.d(TAG, "Calculated manual framing rect: " + framingRect);
            framingRectInPreview = null;
        } else {
            requestedFramingRectWidth = width;
            requestedFramingRectHeight = height;
        }
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on
     * the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
                                                         int width, int height) {
        Rect rect = getFramingRectInPreview();
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.


        if (zXingConfig == null) {
            zXingConfig = new ZXingConfig();
        }

        if (zXingConfig.isFullScreenScan()) {
            return new PlanarYUVLuminanceSource(data, width, height, 0,
                    0, width, height, false);
        } else {
            return new PlanarYUVLuminanceSource(data, width, height, rect.left,
                    rect.top, rect.width(), rect.height(), false);
        }


    }

}
