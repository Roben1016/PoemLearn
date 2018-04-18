package com.roshine.poemlearn.beans;

import cn.bmob.v3.BmobUser;

/**
 * @author Roshine
 * @date 2018/4/18 19:41
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
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
