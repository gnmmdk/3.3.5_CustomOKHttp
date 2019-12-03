package com.kangjj.okhttp.library;

import android.widget.Button;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 15:28
 */
public class OkHttpClient2 {

    private Dispatcher2 dispatcher;
    public OkHttpClient2(){
        this(new Builder());
    };

    public OkHttpClient2(Builder builder){
        this.dispatcher = builder.dispatcher;
    }

    public final static class Builder{
        Dispatcher2 dispatcher;
//        boolean isCanceled;

        public Builder(){
            dispatcher = new Dispatcher2();
        }

        public Builder dispatcher(Dispatcher2 dispatcher){
            this.dispatcher = dispatcher;
            return this;
        }

        public OkHttpClient2 build(){
            return new OkHttpClient2(this);
        }
    }

    public Call2 newCall(Request2 request){
        return new RealCall2(this,request);
    }

    public Dispatcher2 dispatcher(){
        return dispatcher;
    }
}
