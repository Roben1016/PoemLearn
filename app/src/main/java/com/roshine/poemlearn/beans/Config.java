package com.roshine.poemlearn.beans;

import cn.bmob.v3.BmobUser;

/**
 * @author L
 * @date 2018/4/18 19:41

 * @desc
 */
public class Config {
    private static Config instance = null;

    private Config(){
    }

    public static Config getInstance() {
        if(instance == null){
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }
    public BmobUser user;
}
