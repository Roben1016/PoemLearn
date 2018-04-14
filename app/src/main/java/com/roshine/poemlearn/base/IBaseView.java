package com.roshine.poemlearn.base;


import android.support.annotation.Nullable;

/**
 * @author Roshine
 * @date 2018/4/14 21:23
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public interface IBaseView<T>{
    @Nullable
    void loadSuccess(T datas);
    void loadFail(String message);
}
