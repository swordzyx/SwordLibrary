package com.sword.pluginable;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
		//refectInvokeOtherApk();
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
			//执行 PrivateUtils#shout()
			Class<?> cls = Class.forName("com.sword.pluginable.PrivateUtils");
			//获取构造方法，并通过构造方法获取实例
			Constructor<?> constructor = cls.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			Object PrivateUtils = constructor.newInstance();
			//获取 PrivateUtils#shout 方法实例
			Method shoutMethod = cls.getDeclaredMethod("shout");
			shoutMethod.setAccessible(true);
			//执行 PrivateUtils.shout() 方法
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
		if (!apk.exists()) {
			try(Source source = Okio.source(getAssets().open("plugin-debug.apk"));
			    BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
				sink.writeAll(source);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (apk.exists()) {
			//创建插件 apk 对应的 ClassLoader，这个 ClassLoader 加载插件 apk 中所有的类。
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

	public void onClick(View view) {
		if (view.getId() == R.id.loadPlugin) {
			refectInvokeOtherApk();
		}
		if (view.getId() == R.id.loadHofix) {
			copyHofixApkToCache();
		}
		if (view.getId() == R.id.showTitle) {
			Title title = new Title();
			((TextView)findViewById(R.id.title)).setText(title.getTitle());
		}
	}

	private void copyHofixApkToCache() {
		//拷贝热修复 apk 到缓存目录
		File apk = new File(getCacheDir() + "/hotfix.apk");
		try(Source source = Okio.source(getAssets().open("hotfix.apk"));
				BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
			sink.writeAll(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
