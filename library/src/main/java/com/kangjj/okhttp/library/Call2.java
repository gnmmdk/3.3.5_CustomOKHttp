package com.kangjj.okhttp.library;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 9:29
 */
public interface Call2 {

    void enqueue(Callback2 responseCallback);

    void cancel();

}
