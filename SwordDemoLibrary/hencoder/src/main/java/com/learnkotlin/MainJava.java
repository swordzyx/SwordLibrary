package com.learnkotlin;

import com.example.learnkotlin.View;
import com.learnkotlin.utils.CacheUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class MainJava {

    public static void main(String[] args) {
        CacheUtils.save("key", "value");
        
        new View().setOnClickListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return null;
            }
        });
    }
    
    
}
