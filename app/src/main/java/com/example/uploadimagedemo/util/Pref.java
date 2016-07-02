package com.example.uploadimagedemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {
	private static SharedPreferences sharedPreferences = null;

	public static void openPref(Context context) {
		sharedPreferences = context.getSharedPreferences(Config.PREF_FILE,
				Context.MODE_PRIVATE);
	}

	public static String getValue(Context context, String key,
			String defaultValue) {
		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");
		Pref.openPref(context);
		String result = Pref.sharedPreferences.getString(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}

	public static void setValue(Context context, String key, String value) {

		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");

		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putString(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}

	public static int getValue(Context context, String key, int defaultValue) {
		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");
		Pref.openPref(context);
		int result = Pref.sharedPreferences.getInt(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}

	public static void setValue(Context context, String key, int value) {
		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");
		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putInt(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}

	public static long getValue(Context context, String key, long defaultValue) {
		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");
		Pref.openPref(context);
		long result = Pref.sharedPreferences.getLong(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}

	public static void setValue(Context context, String key, long value) {
		// if (key == null || key.equals("")) throw new
		// IllegalArgumentException("Key can't be null or empty string");
		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putLong(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}
}