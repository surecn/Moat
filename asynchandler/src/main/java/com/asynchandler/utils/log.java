package com.asynchandler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class log {
	public static String tag = "com.ant.launcher";

	private final static int logLevel = Log.VERBOSE;
	private static boolean LOG_DEBUG = true;

	public static void init(Context context, boolean debug) {
		LOG_DEBUG = debug;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前版本的包名
			tag = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get The Current Function Name
	 * 
	 * @return
	 */
	private static String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}

		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(log.class.getName())) {
				continue;
			}
			return "[ " + Thread.currentThread().getName() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + " "
					+ st.getMethodName() + " ]";
		}
		return null;
	}

	/**
	 * The Log Level:i
	 * 
	 * @param str
	 */
	public static void i(Object str) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.INFO) {
				String name = getFunctionName();
				if (name != null) {
					Log.i(tag, name + " - " + str);
				} else {
					Log.i(tag, str.toString());
				}
			}
		}

	}

	/**
	 * The Log Level:d
	 * 
	 * @param str
	 */
	public static void d(Object str) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.DEBUG) {
				String name = getFunctionName();
				if (name != null) {
					Log.d(tag, name + " - " + str);
				} else {
					Log.d(tag, str.toString());
				}
			}
		}
	}

	/**
	 * The Log Level:V
	 * 
	 * @param str
	 */
	public static void v(Object str) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.VERBOSE) {
				String name = getFunctionName();
				if (name != null) {
					Log.v(tag, name + " - " + str);
				} else {
					Log.v(tag, str.toString());
				}
			}
		}
	}

	/**
	 * The Log Level:w
	 * 
	 * @param str
	 */
	public static void w(Object str) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.WARN) {
				String name = getFunctionName();
				if (name != null) {
					Log.w(tag, name + " - " + str);
				} else {
					Log.w(tag, str.toString());
				}
			}
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param str
	 */
	public static void e(Object str) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.ERROR) {
				String name = getFunctionName();
				if (name != null) {
					Log.e(tag, name + " - " + str);
				} else {
					Log.e(tag, str.toString());
				}
			}
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param ex
	 */
	public static void e(Exception e) {
		if (LOG_DEBUG) {
			if (logLevel <= Log.ERROR) {
				Log.e(tag, "error:", e);
			}
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param log
	 * @param tr
	 */
	public static void e(String log, Throwable tr) {
		if (LOG_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.v(tag, name + " - " + log + "\n", tr);
			} else {
				Log.v(tag, log);
			}
		}
	}
}