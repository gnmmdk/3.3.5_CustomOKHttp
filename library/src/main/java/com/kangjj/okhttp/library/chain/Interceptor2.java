package com.kangjj.okhttp.library.chain;

import com.kangjj.okhttp.library.Response2;

import java.io.IOException;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 14:35
 */
public interface Interceptor2 {
    Response2 doNext(Chain2 chain) throws IOException;
}
