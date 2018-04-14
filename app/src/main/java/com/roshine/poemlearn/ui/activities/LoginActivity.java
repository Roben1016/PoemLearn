package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.ui.contracts.LoginContract;
import com.roshine.poemlearn.ui.presenters.LoginPresenter;

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
public class LoginActivity extends MvpBaseActivity<LoginContract.ILoginView, LoginPresenter> implements LoginContract.ILoginView {
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

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }
    @OnClick(R.id.btn_login)
    void loginClick(){
        startActivity(MainActivity.class);
        finish();
    }
    @OnClick(R.id.btn_regist)
    void registClick(){
        startActivity(RegistActivity.class);
    }

}
