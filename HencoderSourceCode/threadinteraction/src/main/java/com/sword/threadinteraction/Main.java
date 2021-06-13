package com.sword.threadinteraction;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.HashSet;

public class Main {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
//    runThreadInteractionDemo();
//        runWaitDemo();
    }

    static void runThreadInteractionDemo() {
        new ThreadInteractionDemo().runTest();
    }

    static void runWaitDemo() {
//        new WaitDemo().runTest();
        new WaitDemo().runTestjoin();
    }
}
