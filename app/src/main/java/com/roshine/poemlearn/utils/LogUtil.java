package com.roshine.poemlearn.utils;

import android.util.Log;

import com.roshine.poemlearn.BuildConfig;


/**
 * @author L
 * @date 2018/4/14 20:41

 * @desc log管理相关工具类
 */
public class LogUtil {
	
	public static boolean open = BuildConfig.LOG_DEBUG;
	
	public static void show(String s){
		if (open) {
			Log.e("Roshine", s);
		}
	}
	
	public static void show(String TAG,String content){
		if (open) {
			Log.e(TAG, content);
		}
	}
	
	public static void showI(String TAG,String content){
		if (open) {
			Log.i(TAG, content);
		}
	}
	
	public static void showW(String TAG,String content){
		if (open) {
			Log.w(TAG, content);
		}
	}
	
	public static void showD(String TAG,String content){
		if (open) {
			Log.d(TAG, content);
		}
	}
	
	/**打印try catch出现的异常*/
	public static void exception(String str){
		if (open) {
			Log.e("奔溃日志", "开始出现奔溃的位置: " + str);
		}
	}
}
