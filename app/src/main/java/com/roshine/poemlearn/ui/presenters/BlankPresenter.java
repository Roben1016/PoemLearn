package com.roshine.poemlearn.ui.presenters;

import android.support.annotation.Nullable;

import com.roshine.poemlearn.base.IBasePresenter;
import com.roshine.poemlearn.ui.contracts.BlankContract;

/**
 * @author Roshine
 * @date 2018/4/15 10:03
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class BlankPresenter extends IBasePresenter<BlankContract.IBlankView> implements BlankContract.IBlankPresenter {
    @Override
    public void getPoemData() {

    }

    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }
}
