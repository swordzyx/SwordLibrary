package com.sword.pluginable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utilclass.TextUtil;
import com.example.utilclass.ToastUtilKt;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MainActivity extends AppCompatActivity {
	private static final String HOTFIX_DEX_NAME = "hotfix.dex";
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
		if (view.getId() == R.id.loadHofixApk) {
			downloadHotfix();
			copyAssetsFileToCache("hotfix.apk", "hotfix.apk");
		}
		if (view.getId() == R.id.loadHofixDex) {
			copyAssetsFileToCache("hotfix.dex", "hotfix.dex");
		}
		if (view.getId() == R.id.showTitle) {
			Title title = new Title();
			((TextView)findViewById(R.id.title)).setText(title.getTitle());
		}

		if (view.getId() == R.id.removeHotfix) {
			File hotfixDex = new File(getCacheDir() + File.separator + "hotfix.dex");
			if (hotfixDex.exists() && hotfixDex.delete()) {
				Toast.makeText(this, "unstall hotfix success, please restart app", Toast.LENGTH_SHORT).show();
			}
		}

		if (view.getId() == R.id.restart) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			Runtime.getRuntime().exit(0);
		}

	}

	private void downloadHotfix() {
		OkHttpClient client = new OkHttpClient();
		final Request request = new Request.Builder()
				.url("https://api.hencoder.com/patch/upload/hotfix.dex")
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NonNull Call call, @NonNull IOException e) {
				runOnUiThread(() -> {
					ToastUtilKt.toast(MainActivity.this, "补丁下载失败");
				});
			}

			@Override
			public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
				try(BufferedSink sink = Okio.buffer(Okio.sink(new File(getCacheDir() + File.separator + HOTFIX_DEX_NAME)))) {
					sink.write(response.body().bytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ToastUtilKt.toast(MainActivity.this, "补丁加载成功");
			}
		});
	}

	private void copyAssetsFileToCache(String filePathInAssets, String filePathInCache) {
		//拷贝热修复 apk 到缓存目录
		File apk = new File(getCacheDir() + File.separator + filePathInCache);
		try(Source source = Okio.source(getAssets().open(filePathInAssets));
				BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
			sink.writeAll(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
