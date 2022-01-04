package com.example.plugin;

import android.content.Context;
import android.widget.Toast;

public class PublicUtils {

	public void shout(Context context) {
		System.out.println("I'm shouting from a plugin!");
		Toast.makeText(context, "I'm shouting from a plugin!", Toast.LENGTH_LONG).show();
	}
}
