package com.kangjj.okhttp.library;

import com.kangjj.okhttp.library.chain.ChainManager;
import com.kangjj.okhttp.library.chain.ConnectionServerInterceptor;
import com.kangjj.okhttp.library.chain.Interceptor2;
import com.kangjj.okhttp.library.chain.ReRequestInterceptor;
import com.kangjj.okhttp.library.chain.RequestHeaderInterceptor;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 15:38
 */
public class RealCall2 implements Call2 {
    private final OkHttpClient2 okHttpClient;
    private final Request2 request;

    private boolean executed;

    private volatile boolean canceled;

    public OkHttpClient2 getOkHttpClient() {
        return okHttpClient;
    }

    public RealCall2(OkHttpClient2 okHttpClient, Request2 request) {
        this.okHttpClient= okHttpClient;
        this.request = request;
    }

    @Override
    public void enqueue(Callback2 responseCallback) {
        synchronized(this){
            if(executed){
                throw new IllegalStateException("不能被重复的执行 enqueue Already Executed");
            }
            executed = true;
        }
        okHttpClient.dispatcher().enqueue(new AsyncCall2(responseCallback));
    }

    @Override
    public void cancel() {
        canceled = true;//原本这个值是放在retryAndFollowUpInterceptor里面
    }

    public boolean isCanceled() {
        return canceled;
    }

    final class AsyncCall2 implements  Runnable{
        public Callback2 callback;

        public Request2 getRequest() {
            return RealCall2.this.request;
        }

        public AsyncCall2(Callback2 callback){
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean signalledCallback = false;
            try {
                Response2 response = getResponseWithInterceptorChain();
                if (isCanceled()) {
                    signalledCallback = true;
                    callback.onFailure(RealCall2.this, new IOException("用户取消了 Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onResponse(RealCall2.this, response);
                }
            }catch (IOException e){
                // 责任的划分
                if(signalledCallback){// 如果等于true，回调给用户了，是用户操作的时候 报错
                    System.out.println("用户再使用过程中 出错了...");
                }else{
                    callback.onFailure(RealCall2.this,new IOException("OKHTTP getResponseWithInterceptorChain 错误... e:" + e.toString()));
                }
            }finally {
                okHttpClient.dispatcher().finished(this);
            }
        }

        private Response2 getResponseWithInterceptorChain() throws IOException{
            List<Interceptor2> interceptorList = new ArrayList<>();
            interceptorList.add(new ReRequestInterceptor());
            interceptorList.add(new RequestHeaderInterceptor());
            interceptorList.add(new ConnectionServerInterceptor());

            ChainManager chainManager = new ChainManager(interceptorList,0,request,RealCall2.this);

            return chainManager.getResponse(request);
        }

    }
}
