package com.roshine.poemlearn.ui.presenters;

import android.support.annotation.Nullable;

import com.roshine.poemlearn.base.IBasePresenter;
import com.roshine.poemlearn.ui.contracts.LoginContract;

/**
 * @author Roshine
 * @date 2018/4/14 21:04
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class LoginPresenter extends IBasePresenter<LoginContract.ILoginView> implements LoginContract.ILoginPresenter{
    @Override
    public void login(String username, String password) {

    }

    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }
}
