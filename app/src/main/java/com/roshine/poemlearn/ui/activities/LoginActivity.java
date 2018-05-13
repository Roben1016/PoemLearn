package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;
import com.roshine.poemlearn.utils.DESUtil;
import com.roshine.poemlearn.utils.SPUtil;
import com.roshine.poemlearn.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author L
 * @date 2018/4/14 20:55
 * @desc
 */
public class LoginActivity extends BaseToolBarActivity {
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_remember)
    AppCompatCheckBox cbRemember;
    private int type;//登录类型， 0 表示点击按钮登录； 1 表示自动登录

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        tvTitle.setText(getResources().getString(R.string.login));

        //获取上次成功登录时保存的账号密码
        String username = (String) SPUtil.getParam(this, "username", "");
        String password = (String) SPUtil.getParam(this, "password", "");
        boolean isChecked = (boolean) SPUtil.getParam(this,"checed",false);
        cbRemember.setChecked(isChecked);
        if(!StringUtils.isEmpty(username)){//如果密码不为空，且账号不为空，则进行自动登录
            etUserName.setText(username);
            etUserName.setSelection(username.length());
            if(!StringUtils.isEmpty(password)){//如果密码不为空，在将密码设置到密码输入框
                etUserPwd.setText(DESUtil.Decryption(password));
                type = 1;
                loginClick();
            }
        }
    }

    @OnClick(R.id.btn_login)
    void loginClick() {
        if (TextUtils.isEmpty(etUserName.getText())) {
            toast(getResources().getString(R.string.user_name_null));
            return;
        }
        if (etUserName.getText().toString().length() > 15) {
            toast(getResources().getString(R.string.user_name_long));
            return;
        }
        if (TextUtils.isEmpty(etUserPwd.getText())) {
            toast(getResources().getString(R.string.password_null));
            return;
        }
        if (etUserPwd.getText().toString().length() > 15) {
            toast(getResources().getString(R.string.password_long));
            return;
        }
        showProgress(getResources().getString(R.string.logining), false);
        type = 0;
        login(etUserName.getText().toString(), etUserPwd.getText().toString());
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
        if(type == 1){
            toast(getResources().getString(R.string.auto_login_success));
        }else {
            toast(getResources().getString(R.string.login_success));
        }
        //登录成功后保存账号
        SPUtil.setParam(this,"username",etUserName.getText().toString());
        //登录成功后保存是否开启了记住密码
        SPUtil.setParam(this,"checed",cbRemember.isChecked());
        //如果点击了记住密码，则保存密码，但需要加密, 如果未记住密码，则保存空
        if(cbRemember.isChecked()){
            SPUtil.setParam(this,"password", DESUtil.Encryption(etUserPwd.getText().toString()));
        }else{
            SPUtil.setParam(this,"password", "");
        }
        startActivity(MainActivity.class);
        finish();
    }

    private void loadFail(String message) {
        hideProgress();
        toast(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
