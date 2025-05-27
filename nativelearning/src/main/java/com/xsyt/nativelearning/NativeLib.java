package com.xsyt.nativelearning;

public class NativeLib {

    // Used to load the 'nativelearning' library on application startup.
    static {
        System.loadLibrary("nativelearning");
    }

    /**
     * A native method that is implemented by the 'nativelearning' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}