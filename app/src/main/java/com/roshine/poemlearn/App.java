package com.roshine.poemlearn;

import android.app.Application;
import android.content.Context;

import com.roshine.poemlearn.base.Constants;

import cn.bmob.v3.Bmob;


/**
 * @author Roshine
 * @date 2018/4/14 20:43
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
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
