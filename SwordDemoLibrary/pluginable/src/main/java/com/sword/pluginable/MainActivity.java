package com.sword.pluginable;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	}

	public static void main(String[] args) {
		//reflectInvoke();
		reflectInvokePrivate();
	}

	/**
	 * 反射访问 public 修饰的类
	 */
	private static void reflectInvoke() {
		Class cls = PublicUtils.class;
		try {
			Object publicUtils = cls.newInstance();
			Method shoutMethod = cls.getDeclaredMethod("shout");
			shoutMethod.invoke(publicUtils);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 反射访问非公开的类，通过 class.forName("${className}")
	 */
	private static void reflectInvokePrivate() {
		try {
			Class cls = Class.forName("com.sword.pluginable.PrivateUtils");
			Constructor constructor = cls.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			Object PrivateUtils = constructor.newInstance();
			Method shoutMethod = cls.getDeclaredMethod("shout");
			shoutMethod.setAccessible(true);
			shoutMethod.invoke(PrivateUtils);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 访问同一个工程下其他 apk 模块的类，使用 ClassLoader
	 */
}
