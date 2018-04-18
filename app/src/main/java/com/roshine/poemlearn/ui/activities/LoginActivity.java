package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
        showProgress(getResources().getString(R.string.logining),false);
        login(etUserName.getText().toString(),etUserPwd.getText().toString());
    }

    private void login(String username, String password) {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null && bmobUser != null) {
                    Config.getInstance().user = bmobUser;
                    loadSuccess();
                } else {
                    loadFail(getResources().getString(R.string.login_fail));
                }
            }
        });
    }

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
