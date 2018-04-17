package com.roshine.poemlearn.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Roshine
 * @date 2018/4/16 19:13
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class PoemDataBaseHelper extends SQLiteOpenHelper {

    public PoemDataBaseHelper(Context context){
        super(context,"test.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table poetry(" +
                "id integer primary key autoincrement," +
                "name varchar(20)," +
                "number varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
