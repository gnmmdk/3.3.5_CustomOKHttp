package com.kangjj.okhttp.library.chain;

import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.Response2;

import java.io.IOException;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 14:26
 */
public interface Chain2 {
    Request2 getRequest();
    Response2 getResponse(Request2 request) throws IOException;
}
