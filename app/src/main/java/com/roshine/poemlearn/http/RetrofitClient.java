package com.roshine.poemlearn.http;

import com.roshine.poemlearn.base.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Roshine
 * @date 2017/8/23 23:48
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class RetrofitClient {
    private static RetrofitClient instance;
    private RetrofitService service;
    private OkHttpClient mOkHttpClient;
    private Retrofit retrofit;

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }

        }
        return instance;
    }

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava2适配器
                .client(getDefaultHttpClient())
                .build();
    }

    public RetrofitService getApiService() {
        if (service == null) {
            service = retrofit.create(RetrofitService.class);
        }
        return service;
    }

    private OkHttpClient getDefaultHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Constants.NormalConstants.TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.NormalConstants.TIME_OUT, TimeUnit.SECONDS)
//                    .addInterceptor(new HeaderInterceptor())  //公共参数的封装
                    .addNetworkInterceptor(new LoggingInterceptor())
//                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext))) //cookie 保存方案
                    .build();
        }
        return mOkHttpClient;
    }
}
