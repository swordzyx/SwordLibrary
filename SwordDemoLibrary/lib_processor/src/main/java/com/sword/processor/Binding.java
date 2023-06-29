package com.sword.processor;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Binding {
    public static void bind(Activity activity) {
        try {
            Class<?> activityBindingClass = Class.forName(activity.getClass().getCanonicalName() + "Binding");
            Constructor<?> constructor = activityBindingClass.getDeclaredConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
