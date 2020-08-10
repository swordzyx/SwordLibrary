package com.example.bluetoothpratice.common.logger;

public class MessageOnlyLogFilter implements LogNode {
    private LogNode mNext;

    public MessageOnlyLogFilter(){ }

    public MessageOnlyLogFilter(LogNode node){
        mNext = node;
    }

    public void setNext(LogNode node){
        mNext = node;
    }

    public LogNode getNext(){
        return mNext;
    }

    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        if (mNext != null){
            mNext.println(Log.NONE, null, msg, null);
        }
    }
}
