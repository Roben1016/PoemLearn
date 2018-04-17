package com.roshine.poemlearn.utils;

import com.roshine.poemlearn.base.Constants;

import java.io.File;

/**
 * @author Roshine
 * @date 2018/4/16 20:58
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class FileUtil {

    public static String getDbPath() {
        File file = new File(Constants.POEM_DB_PATH);
        if (file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
