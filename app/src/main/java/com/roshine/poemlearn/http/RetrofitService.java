package com.roshine.poemlearn.http;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Roshine
 * @date 2017/8/23 23:58
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public interface RetrofitService {

    /**
     * 热映中
     * @return
     */
    @POST("gushi/tangshi.aspx")
    Flowable<Object> getPoems();
}

