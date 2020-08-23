package com.example.bluetoothpratice.common.logger;

import android.util.Log;

public class LogWrapper implements LogNode {
    LogNode mNext;

    public void setLogNode(LogNode node){
        mNext = node;
    }

    public LogNode getNext(){
        return mNext;
    }

    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        String useMsg = msg;
        if (msg == null){
            useMsg = "";
        }

        if (tr != null){
            msg += "\n" + Log.getStackTraceString(tr);
        }

        Log.println(priority, tag, useMsg);

        if (mNext != null){
            mNext.println(priority, tag, msg, tr);
        }
    }
}
