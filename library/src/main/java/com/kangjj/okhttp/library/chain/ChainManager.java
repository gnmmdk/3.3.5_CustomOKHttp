package com.kangjj.okhttp.library.chain;

import com.kangjj.okhttp.library.RealCall2;
import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.Response2;

import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 14:33
 */
public class ChainManager implements Chain2 {

    private final List<Interceptor2> interceptors;
    private int index;
    private final Request2 request;
    private final RealCall2 call;

    public ChainManager(List<Interceptor2> interceptors, int index, Request2 request, RealCall2 call) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
        this.call = call;
    }

    @Override
    public Request2 getRequest() {
        return request;
    }

    @Override
    public Response2 getResponse(Request2 request) throws IOException {
        // 判断index++计数  不能大于 size 不能等于
        if(index >= interceptors.size()) throw new AssertionError();

        if(interceptors.isEmpty()){
            throw new IOException("interceptors is empty");
        }

        //取出拦截器
        Interceptor2 interceptor = interceptors.get(index);

        ChainManager manager = new ChainManager(interceptors,index+1,request,call);//获取下一个 ChainManager

        Response2 response = interceptor.doNext(manager);
        return response;
    }

    public List<Interceptor2> getInterceptors() {
        return interceptors;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public RealCall2 getCall() {
        return call;
    }
}
