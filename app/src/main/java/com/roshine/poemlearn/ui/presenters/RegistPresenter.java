package com.roshine.poemlearn.ui.presenters;

import android.support.annotation.Nullable;

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
    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }

    @Override
    public void regist(String username, String password) {

    }
}
