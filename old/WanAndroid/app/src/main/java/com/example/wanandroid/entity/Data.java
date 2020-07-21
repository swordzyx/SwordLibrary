package com.example.wanandroid.entity;

import android.graphics.drawable.Drawable;

public class Data {
    public int resourceId;
    public Drawable resource;

    public Data(int resourceId){
        this.resourceId = resourceId;
    }

    public Data(Drawable resource){
        this.resource = resource;
    }
}
