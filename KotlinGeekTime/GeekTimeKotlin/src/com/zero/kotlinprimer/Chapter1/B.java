package com.zero.kotlinprimer.Chapter1;

public class B {
    public static String format(String str){
        return str.isEmpty() ? null : str;
    }

    public static void main(String[] args){
        Utils.INSTANCE.sayMessage(null);
    }
}
