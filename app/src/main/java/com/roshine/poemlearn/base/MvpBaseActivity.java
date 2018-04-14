package com.roshine.poemlearn.base;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * @author Roshine
 * @date 2018/4/14 21:23
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc mvp中activity基类
 */
public abstract class MvpBaseActivity<V extends IBaseView,T extends IBasePresenter<V>> extends BaseToolBarActivity {
    T mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroyView();
        }
    }

    public abstract T getPresenter();
}
