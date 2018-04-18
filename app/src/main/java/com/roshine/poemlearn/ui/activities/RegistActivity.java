package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
        regist(etUserName.getText().toString(),etUserPwd.getText().toString());
    }

    private void regist(String username,String password) {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    loadSuccess();
                } else {
                    loadFail(getResources().getString(R.string.regist_failed));
                }
            }
        });
    }

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
