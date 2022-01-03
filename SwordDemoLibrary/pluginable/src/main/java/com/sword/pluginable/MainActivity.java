package com.sword.pluginable;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//reflectInvokePrivate();
		refectInvokeOtherApk();
	}

	/**
	 * 反射访问 public 修饰的类
	 */
	private void reflectInvoke() {
		Class<?> cls = PublicUtils.class;
		try {
			Object publicUtils = cls.newInstance();
			Method shoutMethod = cls.getDeclaredMethod("shout");
			shoutMethod.invoke(publicUtils);
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 反射访问非公开的类，通过 class.forName("${className}")
	 */
	private void reflectInvokePrivate() {
		try {
			Class<?> cls = Class.forName("com.sword.pluginable.PrivateUtils");
			Constructor<?> constructor = cls.getDeclaredConstructors()[0];
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
	private void refectInvokeOtherApk() {
		//先将 assets/plugin-debug.apk 拷贝到缓存目录中
		File apk = new File(getCacheDir() + "/plugin.apk");
		try(Source source = Okio.source(getAssets().open("plugin-debug.apk"));
				BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
			sink.writeAll(source);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DexClassLoader classLoader = new DexClassLoader(apk.getPath(), getCacheDir().getPath(), null, null);
		try {
			Class<?> cls = classLoader.loadClass("com.example.plugin.PublicUtils");
			Object publicUtils = cls.newInstance();
			Method shoutMethod = cls.getDeclaredMethod("shout");
			shoutMethod.invoke(publicUtils);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}
