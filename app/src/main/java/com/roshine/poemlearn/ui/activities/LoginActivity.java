package com.roshine.poemlearn.ui.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.base.Constants;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.ui.contracts.LoginContract;
import com.roshine.poemlearn.ui.presenters.LoginPresenter;
import com.roshine.poemlearn.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/14 20:55
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class LoginActivity extends BaseToolBarActivity{
    private static final int LOGIN_SUC = 200;
    private static final int LOGIN_FAIL = 201;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.til_user_name)
    TextInputLayout tilUserName;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        tvTitle.setText(getResources().getString(R.string.login));
    }

    @OnClick(R.id.btn_login)
    void loginClick() {
//        if(TextUtils.isEmpty(etUserName.getText())){
//            toast(getResources().getString(R.string.user_name_null));
//            return;
//        }
//        if(etUserName.getText().toString().length() > 15){
//            toast(getResources().getString(R.string.user_name_long));
//            return;
//        }
//        if(TextUtils.isEmpty(etUserPwd.getText())){
//            toast(getResources().getString(R.string.password_null));
//            return;
//        }
//        if(etUserPwd.getText().toString().length() > 15){
//            toast(getResources().getString(R.string.password_long));
//            return;
//        }
        showProgress(getResources().getString(R.string.logining),false);
        loadSuccess();
//        login(etUserName.getText().toString(),etUserPwd.getText().toString());
    }

    private void login(String username, String password) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Constants.POEM_DB_PATH, null);
//                Cursor cursor = database.rawQuery("select "+Constants.LOGIN_DB_PASSWORD+" from "+Constants.LOGIN_DB_TABLE+" where "+Constants.LOGIN_DB_USER_NAME+"=?", new String[]{username});
//                if (cursor != null) {
//                    if(cursor.moveToFirst()){
//                        int columnIndex = cursor.getColumnIndex(Constants.LOGIN_DB_USER_NAME);
//                        if(columnIndex != -1){
//                            String string = cursor.getString(columnIndex);
//                            if(password.equals(string)){
//                                Message message = handler.obtainMessage();
//                                message.what = LOGIN_SUC;
//                                handler.sendMessage(message);
//                            }else{
//                                Message message = handler.obtainMessage();
//                                message.what = LOGIN_FAIL;
//                                message.obj = getResources().getString(R.string.password_error);
//                                handler.sendMessage(message);
//                            }
//                        }else{
//                            LogUtil.show("登录失败，columnIndex = -1");
//                            Message message = handler.obtainMessage();
//                            message.what = LOGIN_FAIL;
//                            message.obj = getResources().getString(R.string.no_user_or_password);
//                            handler.sendMessage(message);
//                        }
//                    }else{
//                        LogUtil.show("登录失败，cursor 为空");
//                        Message message = handler.obtainMessage();
//                        message.what = LOGIN_FAIL;
//                        message.obj = getResources().getString(R.string.no_user);
//                        handler.sendMessage(message);
//                    }
//                    cursor.close();
//                    database.close();
//                }else{
//                    LogUtil.show("登录失败，moveToFirst 为false");
//                    Message message = handler.obtainMessage();
//                    message.what = LOGIN_FAIL;
//                    message.obj = getResources().getString(R.string.login_fail);
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
                case LOGIN_SUC:
                    loadSuccess();
                    break;
                case LOGIN_FAIL:
                    loadFail((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.btn_regist)
    void registClick() {
        startActivity(RegistActivity.class);
    }


    private void loadSuccess() {
        hideProgress();
        toast(getResources().getString(R.string.login_success));
        startActivity(MainActivity.class);
        finish();
    }

    private void loadFail(String message) {
        hideProgress();
        toast(message);
    }
}
