package com.roshine.poemlearn;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.roshine.poemlearn.base.Constants;

import cn.bmob.v3.Bmob;


/**
 * @author L
 * @date 2018/4/14 20:43

 * @desc
 */
public class App extends Application {
    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        //第一：默认初始化bmob-sdk
        Bmob.initialize(this, Constants.BMOB_SDK_KEY);
        //初始化讯飞sdk
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));
    }

    public static Context getContext(){
        return applicationContext;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
