package com.jdkgroup.utils;

import android.util.Log;

public final class Logging {
	private static String tag = "Tag";

	private Logging() {
	}

	private static boolean mLoggingEnabled = true;

	public static void setDebugLogging(boolean enabled) {
		mLoggingEnabled = enabled;
	}

	public static boolean isDebugLogging() {
		return mLoggingEnabled;
	}

	public static void v(String msg) {
		if (mLoggingEnabled) {
			Log.v(tag, msg);
		}
	}

	public static void v(String msg, Throwable tr) {
		if (mLoggingEnabled) {
			Log.v(tag, msg, tr);
		}
	}

	public static void d(String msg) {
		if (mLoggingEnabled) {
			Log.d(tag, msg);
		}
	}

	public static void d(String msg, Throwable tr) {
		if (mLoggingEnabled) {
			Log.d(tag, msg, tr);
		}
	}

	public static void i(String msg) {
		if (mLoggingEnabled) {
			Log.i(tag, msg);
		}
	}

	public static void i(String msg, Throwable tr) {
		if (mLoggingEnabled) {
			Log.i(tag, msg, tr);
		}
	}

	public static void w(String msg) {
		if (mLoggingEnabled) {
			Log.w(tag, msg);
		}
	}

	public static void w(String msg, Throwable tr) {
		if (mLoggingEnabled) {
			Log.w(tag, msg, tr);
		}
	}

	public static void w(Throwable tr) {
		if (mLoggingEnabled) {
			Log.w(tag, tr);
		}
	}

	public static void e(String msg) {
		if (mLoggingEnabled) {
			Log.e(tag, msg);
		}
	}

	public static void e( String msg, Throwable tr) {
		if (mLoggingEnabled) {
			Log.e(tag, msg, tr);
		}
	}
}