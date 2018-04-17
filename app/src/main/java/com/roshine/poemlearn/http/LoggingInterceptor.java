package com.roshine.poemlearn.http;

import android.support.annotation.NonNull;

import com.roshine.poemlearn.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Roshine
 * @date 2017/8/24 12:25
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogUtil.showI("Roshine","请求的url:"+request.url());
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        long t2 = System.nanoTime();
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
//        LogUtil.showI("Roshine",String.format(Locale.CHINA, "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
//                response.request().url(),
//                responseBody.string(),
//                (t2 - t1) / 1e6d,
//                response.headers()));
//        LogUtil.showI("Roshine","结果"+responseBody.string());
        LogUtil.showI("Roshine","请求时间："+(t2 - t1) / 1e6d);
        return response;
    }
}
