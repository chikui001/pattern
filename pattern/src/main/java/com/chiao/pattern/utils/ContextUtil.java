// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
/**
 *
 */
package com.chiao.pattern.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import java.util.List;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2015年12月5日
 */
public class ContextUtil {

	/**
	 * 和IMPORTANCE_FOREGROUND一个作用，判断进程ui是否在前台可以react
	 * true 前台
	 * false 后台
	 *
	 * @param context
	 * @return
	 * @deprecated
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (tasks != null && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static int getProcessPid() {
		return android.os.Process.myPid();
	}

	public static String getCurrentProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> lists = mActivityManager.getRunningAppProcesses();
		if (null != lists && lists.size() > 0) {
			for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
				if (appProcess.pid == pid) {
					return appProcess.processName;
				}
			}
		}
		return null;
	}

	/**
	 * true is main process
	 *
	 * @param application
	 * @return
	 */
	public static boolean isMainProcess(Application application) {
		String mainPackageName = application.getPackageName();
		if (mainPackageName.equals(getCurrentProcessName(application))) {
			return true;
		}
		return false;
	}

	/**
	 * 23及以上
	 * =TargetAip（23）：specific min api sdk 23
	 *
	 * @return
	 */
	public static boolean isAndroidM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	/**
	 * 21及以上
	 * =TargetAip（23）：specific min api sdk 23
	 *
	 * @return
	 */
	public static boolean isAndroidL() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	public static boolean is19() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}
}
