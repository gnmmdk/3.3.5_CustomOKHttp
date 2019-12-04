package com.kangjj.okhttp.library.chain;

import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.RequestBody2;
import com.kangjj.okhttp.library.Response2;
import com.kangjj.okhttp.library.SocketRequestServer;

import java.io.IOException;
import java.util.Map;

/**
 * @Description:请求头拦截器处理
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 15:00
 */
public class RequestHeaderInterceptor implements Interceptor2 {

    @Override
    public Response2 doNext(Chain2 chain) throws IOException {
        //拼接请求头之 请求集
//        ChainManager manager = (ChainManager) chain;
        Request2 request = chain.getRequest();
        Map<String,String> mHeadList = request.getHeadList();

        // get post  hostName    Host: restapi.amap.com
        mHeadList.put("Host",new SocketRequestServer().getHost(request));

        if(Request2.POST.equalsIgnoreCase(request.getRequetMethod())){
            // 请求体   type lang
            /**
             * Content-Length: 48
             * Content-Type: application/x-www-form-urlencoded
             */
            mHeadList.put("Content-Length",request.getRequestBody().getBody());
            mHeadList.put("Content-Type", RequestBody2.TYPE);
        }
        return chain.getResponse(request);
    }
}
