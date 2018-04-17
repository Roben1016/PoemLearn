package com.roshine.poemlearn.ui.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.App;
import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.base.Constants;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.ui.contracts.RegistContract;
import com.roshine.poemlearn.ui.presenters.RegistPresenter;
import com.roshine.poemlearn.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/14 23:58
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class RegistActivity extends BaseToolBarActivity{
    private static final int REGIST_SUC = 100;
    private static final int REGIST_FAIL = 101;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.til_user_name)
    TextInputLayout tilUserName;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_user_pwd_second)
    EditText etUserPwdSecond;
    @BindView(R.id.til_password_second)
    TextInputLayout tilPasswordSecond;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.regist));
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @OnClick(R.id.btn_regist)
    void registClick(){
        if(TextUtils.isEmpty(etUserName.getText())){
            toast(getResources().getString(R.string.user_name_null));
            return;
        }
        if(etUserName.getText().toString().length() > 15){
            toast(getResources().getString(R.string.user_name_long));
            return;
        }
        if(TextUtils.isEmpty(etUserPwd.getText())){
            toast(getResources().getString(R.string.password_null));
            return;
        }
        if(etUserPwd.getText().toString().length() > 15){
            toast(getResources().getString(R.string.password_long));
            return;
        }
        if(TextUtils.isEmpty(etUserPwdSecond.getText())){
            toast(getResources().getString(R.string.password_second_null));
            return;
        }
        if(!etUserPwd.getText().toString().equals(etUserPwdSecond.getText().toString())){
            toast(getResources().getString(R.string.password_unequals));
            return;
        }
        showProgress(getResources().getString(R.string.loading_text),false);
//        regist(etUserName.getText().toString(),etUserPwd.getText().toString());
        loadSuccess();
    }

    private void regist(String username,String password) {
        //TODO 加权限和数据库文件是否存在的判断
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Constants.POEM_DB_PATH, null);
//                Cursor cursor = database.rawQuery("select * from "+Constants.LOGIN_DB_TABLE+" where "+Constants.LOGIN_DB_USER_NAME+"=?", new String[]{username});
//                if (cursor != null) {
//                    if(cursor.moveToFirst()) {
//                        int columnIndex = cursor.getColumnIndex(Constants.LOGIN_DB_USER_NAME);
//                        if(columnIndex == -1){
//                            ContentValues cv = new ContentValues();
//                            cv.put(Constants.LOGIN_DB_USER_NAME, username);
//                            cv.put(Constants.LOGIN_DB_PASSWORD, password);
//                            long index = database.insert(Constants.LOGIN_DB_TABLE, null, cv);
//                            if(index != -1){
//                                LogUtil.show("注册成功 columnIndex=-1 index:"+index);
//                                Message message = handler.obtainMessage();
//                                message.what = REGIST_SUC;
//                                handler.sendMessage(message);
//                            }else{
//                                LogUtil.show("注册失败，columnIndex = -1 但是插入失败:"+index);
//                                Message message = handler.obtainMessage();
//                                message.what = REGIST_FAIL;
//                                message.obj = getResources().getString(R.string.regist_failed);
//                                handler.sendMessage(message);
//                            }
//                        }else{
//                            LogUtil.show("注册失败，columnIndex != -1 表示已存在该账号");
//                            Message message = handler.obtainMessage();
//                            message.what = REGIST_FAIL;
//                            message.obj = getResources().getString(R.string.has_user);
//                            handler.sendMessage(message);
//                        }
//                    }else{
//                        ContentValues cv = new ContentValues();
//                        cv.put(Constants.LOGIN_DB_USER_NAME, username);
//                        cv.put(Constants.LOGIN_DB_PASSWORD, password);
//                        long index = database.insert(Constants.LOGIN_DB_TABLE, null, cv);
//                        if(index != -1){
//                            LogUtil.show("注册成功 index:"+index);
//                            Message message = handler.obtainMessage();
//                            message.what = REGIST_SUC;
//                            handler.sendMessage(message);
//                        }else{
//                            LogUtil.show("moveToFirst为false，但注册失败，插入失败:"+index);
//                            Message message = handler.obtainMessage();
//                            message.what = REGIST_FAIL;
//                            message.obj = getResources().getString(R.string.regist_failed);
//                            handler.sendMessage(message);
//                        }
//                    }
//                    cursor.close();
//                    database.close();
//                } else {
//                    LogUtil.show("注册失败，cursor为空");
//                    Message message = handler.obtainMessage();
//                    message.what = REGIST_FAIL;
//                    message.obj = getResources().getString(R.string.regist_failed);
//                    handler.sendMessage(message);
//                }
//            }
//        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGIST_SUC:
                    loadSuccess();
            	    break;
                case REGIST_FAIL:
                    loadFail((String) msg.obj);
                    break;
                default:
            	    break;
            }
        }
    };

    private void loadSuccess() {
        hideProgress();
        toast(getResources().getString(R.string.regist_success));
        finish();
    }

    private void loadFail(String message) {
        hideProgress();
        toast(message);
    }
}
