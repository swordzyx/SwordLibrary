package com.example.swordlibrary.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.swordlibrary.R;
import com.sword.ScreenSize;

import java.util.ArrayList;
import java.util.List;

public class FloatBallData {
	final int[] iconResArray;
	final String[] titleStringArray;
	public FloatBallData() {
		iconResArray = new int[]{R.drawable.user, R.drawable.feedback, R.drawable.float_menu_auth, R.drawable.switch_account};
		titleStringArray = new String[]{"账户", "客服", "实名", "切换"};
	}

	List<MenuItem> getFloatData(Context context) {
		List<MenuItem> items = new ArrayList<>();
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		for (int i=0; i<iconResArray.length; i++) {
			ImageView accountIcon = buildImageView(context, params, iconResArray[i]);
			TextView accountTitle = buildTextView(context, params, titleStringArray[i]);
			items.add(new MenuItem(accountIcon, accountTitle));
		}
		return items;
	}


	private TextView buildTextView(Context context, ViewGroup.LayoutParams params, String text) {
		TextView view = new TextView(context);
		view.setLayoutParams(params);
		view.setText(text);
		view.setTextColor(Color.WHITE);
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	private ImageView buildImageView(Context context, ViewGroup.LayoutParams params, int imageRes) {
		ImageView view = new ImageView(context);
		view.setPadding(ScreenSize.dp(3), ScreenSize.dp(3), ScreenSize.dp(3), ScreenSize.dp(1));
		view.setLayoutParams(params);
		view.setImageResource(imageRes);
		return view;
	}
}
