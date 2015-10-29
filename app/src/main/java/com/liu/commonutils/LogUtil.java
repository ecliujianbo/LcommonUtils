package com.liu.commonutils;

import android.content.Context;
import android.util.Log;

/**
 * 调试程序信息
 * 
 * @author liu 2013-6-5
 */
public class LogUtil {

	private static String tag = "otouzi";

	private static boolean flag = true;

	public static void logD(String strLog) {
		if (flag) {
			StackTraceElement invoker = getInvoker();
			Log.d(tag,
					"【" + invoker.getClassName() + ":"
							+ invoker.getMethodName() + ":"
							+ invoker.getLineNumber() + "】" + strLog);
		}
	}

	public static void logI(String strLog) {
		if (flag) {
			StackTraceElement invoker = getInvoker();
			Log.i(tag,
					"【" + invoker.getClassName() + ":"
							+ invoker.getMethodName() + ":"
							+ invoker.getLineNumber() + "】" + strLog);
		}
	}

	public static void logE(Exception e) {
		if (flag) {
			StackTraceElement invoker = getInvoker();
			Log.e(tag,
					"【" + invoker.getClassName() + ":"
							+ invoker.getMethodName() + ":"
							+ invoker.getLineNumber() + "】" + e);
		}
	}

	public static void logE(String strLog) {
		if (flag) {
			StackTraceElement invoker = getInvoker();
			Log.e(tag,
					"【" + invoker.getClassName() + ":"
							+ invoker.getMethodName() + ":"
							+ invoker.getLineNumber() + "】" + strLog);
		}
	}

	private static StackTraceElement getInvoker() {
		return Thread.currentThread().getStackTrace()[4];
	}

	public static void logExpireTime(Context contxt) {
		// ShareUtils.getTokenExpiresIn(contxt)
		logD(String.format(
				"保存的时间: %s,现在的时间:%s",
				Constants.getDate(LSharePreference.getInstance(contxt).getLong(
						SharePreferenceName.TOKENEXPIRESIN)),
				Constants.getDate(System.currentTimeMillis())));
	}

	public static void logToken(Context context) {
		logI("token:  "
				+ LSharePreference.getInstance(context).getString(
						SharePreferenceName.ACCESS_TOKEN)
				+ "; Constants token: " + Constants.sToken);
	}
}
