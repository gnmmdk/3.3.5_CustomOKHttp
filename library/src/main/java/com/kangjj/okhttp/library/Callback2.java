package com.kangjj.okhttp.library;

import java.io.IOException;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 9:30
 */
public interface Callback2 {
    void onFailure(Call2 call, IOException e);

    void onResponse(Call2 call,Response2 response) throws IOException;
}
