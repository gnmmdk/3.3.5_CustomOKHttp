package com.kangjj.okhttp.library.chain;

import android.util.Log;

import com.kangjj.okhttp.library.OkHttpClient2;
import com.kangjj.okhttp.library.RealCall2;
import com.kangjj.okhttp.library.Response2;

import java.io.IOException;

/**
 * @Description:重试拦截器  TODO 优化
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 14:45
 */
public class ReRequestInterceptor implements Interceptor2 {

    private final String TAG = ReRequestInterceptor.class.getSimpleName();

    @Override
    public Response2 doNext(Chain2 chain) throws IOException {
        Log.d(TAG, "我是重试拦截器，执行了");
        ChainManager chainManager = (ChainManager) chain;

        RealCall2 realCall = chainManager.getCall();
        OkHttpClient2 client = realCall.getOkHttpClient();

        IOException ioException = null;
        if(client.getRecout()!=0){
            for (int i = 0; i < client.getRecout(); i++) {
                try{
                    Log.d(TAG, "我是重试拦截器，我要Return Response2了");
                    Response2 response = chain.getResponse(chainManager.getRequest());
                    return response;
                }catch (IOException e){
                    ioException = e;
                }
            }
        }

        throw ioException;
    }
}
