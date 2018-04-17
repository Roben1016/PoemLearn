package com.roshine.poemlearn.http;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Roshine
 * @date 2017/8/17 19:54
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class RetrofitHelper {
    private static FlowableTransformer ioToMainThreadSchedulerTransformer;
    private static FlowableTransformer newThreadToMainThreadSchedulerTransformer;

    static {
        ioToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
        newThreadToMainThreadSchedulerTransformer = createNewThreadToMainThreadScheduler();
    }

    private static <T> FlowableTransformer<T, T> createIOToMainThreadScheduler() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从IO线程切换到主线程
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyIOToMainThreadSchedulers() {
        return ioToMainThreadSchedulerTransformer;
    }

    private static <T> FlowableTransformer<T, T> createNewThreadToMainThreadScheduler() {
        return tObservable -> tObservable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> applyNewThreadToMainThreadSchedulers() {
        return newThreadToMainThreadSchedulerTransformer;
    }

    /**
     * 处理服务器返回的数据，进一步处理错误信息
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> handleResult(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> flowable) {
                return flowable.flatMap(new Function<T, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(@NonNull T response) throws Exception {
//                        ResponseParser responseParser = new ResponseParser();
//                        SerializerHandler serializerHandler = new SerializerHandler(0);
//                        try {
//                            int code = response.code();
//                            if(response.isSuccessful()){
//                                ResponseBody body = response.body();
//                                String result = body.string();
//                                InputStream in = new ByteArrayInputStream(result.getBytes());
//                                Object parse = responseParser.parse(serializerHandler, in, false);
//                                Gson gson = new Gson();
//                                String json = gson.toJson(parse);
                                return createData(response);
//                            }else{
//                                return Flowable.error(new RxException(String.valueOf(code)));
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return Flowable.error(e);
//                        }
//                        catch (XMLRPCException e) {
//                            e.printStackTrace();
//                            return Flowable.error(e);
//                        }
//                        if (!tBaseBean.isError()){
//                            return createData(tBaseBean.getResults());
//                        }else{
//                            return Flowable.error(new ServerException("服务器返回错误"));
//                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @return
     */
    private static <T> Flowable<T> createData(T data) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> flowableEmitter) throws Exception {
                try {
                    flowableEmitter.onNext(data);
                    flowableEmitter.onComplete();
                }catch (Exception e){
                    flowableEmitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
