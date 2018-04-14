package com.roshine.poemlearn.ui.contracts;

import com.roshine.poemlearn.base.IBaseView;



/**
 * @author Roshine
 * @date 2018/4/14 20:58
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public interface LoginContract {
    interface ILoginView extends IBaseView<Object>{
    }
    interface ILoginPresenter extends IBaseView<Object>{
        void login(String username,String password);
    }
}
