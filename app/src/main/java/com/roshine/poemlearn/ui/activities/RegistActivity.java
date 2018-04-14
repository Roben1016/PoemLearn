package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.ui.contracts.RegistContract;
import com.roshine.poemlearn.ui.presenters.RegistPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class RegistActivity extends MvpBaseActivity<RegistContract.IRegistView, RegistPresenter> implements RegistContract.IRegistView {
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

    @Override
    public RegistPresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
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
    @OnClick(R.id.iv_back)
    void backClick(){
        finish();
    }

}
