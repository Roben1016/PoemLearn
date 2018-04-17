package com.roshine.poemlearn.base;

import android.Manifest;
import android.os.Environment;


/**
 * @author Roshine
 * @date 2017/7/18 16:18
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc 常量类
 */
public class Constants {

    public static class NormalConstants{
        public static final int TIME_OUT = 30;//请求超时时间
        public static final String CONTENT_TYPE = "text/xml";
    }

    public static class SharedPreferancesKeys{
    }

    public static class Urls{
        public static final String BASE_URL = "https://www.gushiwen.org/";
    }
    public static String POEM_DB_PATH = Environment.getExternalStorageDirectory()+"/poetry.db";
    public static String LOGIN_DB_TABLE = "login";
    public static String LOGIN_DB_USER_NAME = "username";
    public static String LOGIN_DB_PASSWORD = "password";
}
