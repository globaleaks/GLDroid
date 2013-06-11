package org.globaleaks.util;

import android.util.Log;

public class Logger {

	public static final boolean DEBUG = true;
	public static final String TAG = "GL";
	
	public static void e(String msg, Throwable t) {
		Log.e(TAG, msg, t);
	}

	public static void e(String string) {
		e(string, null);
	}
	
	public static void i(String msg) {
		Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if(!DEBUG) return;
		Log.d(TAG, msg);
	}

}
