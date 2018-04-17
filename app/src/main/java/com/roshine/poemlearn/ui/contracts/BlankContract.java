package com.roshine.poemlearn.ui.contracts;

import com.roshine.poemlearn.base.IBaseView;

/**
 * @author Roshine
 * @date 2018/4/15 10:01
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public interface BlankContract {
    interface IBlankView extends IBaseView<Object>{

    }
    interface IBlankPresenter extends IBaseView<Object>{
        void getPoemData(String poemType,String schoolType);
    }
}
