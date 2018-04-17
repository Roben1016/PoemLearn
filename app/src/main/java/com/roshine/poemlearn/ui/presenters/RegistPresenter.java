package com.roshine.poemlearn.ui.presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.roshine.poemlearn.App;
import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.Constants;
import com.roshine.poemlearn.base.IBasePresenter;
import com.roshine.poemlearn.ui.contracts.RegistContract;

/**
 * @author Roshine
 * @date 2018/4/15 0:01
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class RegistPresenter extends IBasePresenter<RegistContract.IRegistView> implements RegistContract.IRegistPresenter {
    @Override
    public void regist(Context context,String username, String password) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Constants.POEM_DB_PATH, null);
        Cursor cursor = database.rawQuery("select name from "+Constants.LOGIN_DB_TABLE+" where "+Constants.LOGIN_DB_USER_NAME+"=?", new String[]{username});
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(Constants.LOGIN_DB_TABLE);
                if(columnIndex == -1){
                    ContentValues cv = new ContentValues();
                    cv.put(Constants.LOGIN_DB_USER_NAME, username);
                    cv.put(Constants.LOGIN_DB_PASSWORD, password);
                    long index = database.insert(Constants.LOGIN_DB_TABLE, null, cv);
                    if(index != -1){
                        getView().loadSuccess(null);
                    }
                }
            }
            database.close();
            cursor.close();
        } else {
            getView().loadFail(App.getContext().getResources().getString(R.string.regist_failed));
        }
    }

    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }
}
