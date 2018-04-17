package com.roshine.poemlearn.ui.contracts;

import android.content.Context;

import com.roshine.poemlearn.base.IBaseView;

/**
 * @author Roshine
 * @date 2018/4/14 23:59
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public interface RegistContract {
    interface IRegistView extends IBaseView<Object>{
    }
    interface IRegistPresenter extends IBaseView<Object>{
        void regist(Context context,String username, String password);
    }
}
