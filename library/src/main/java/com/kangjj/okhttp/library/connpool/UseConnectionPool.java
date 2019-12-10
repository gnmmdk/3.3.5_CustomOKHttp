package com.kangjj.okhttp.library.connpool;

import android.util.Log;

/**
 * @Description: 使用 连接服务器拦截器的动作 测试
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.connpool
 * @CreateDate: 2019/12/10 17:35
 */
public class UseConnectionPool {
    private final static String TAG = UseConnectionPool.class.getSimpleName();
    public void useConnectionPool(ConnectionPool connectionPool,String host,int port){
        // 开始模拟 连接服务器拦截器的动作
        // 首先从连接池里面获取，是否有连接对象可用
        HttpConnection httpConnection = connectionPool.getConnection(host,port);
        if(httpConnection == null){
            httpConnection = new HttpConnection(host,port);
            Log.d(TAG, "连接池里面没有 连接对象 ，需要实例化一个连接对象...");
        }else{
            Log.d(TAG, "复用池 里面有一个连接对象");
        }
        // 模拟请求
        // ....
        Log.d(TAG, "给服务器 发送请求...");

        httpConnection.hasUseTime = System.currentTimeMillis();
        connectionPool.putConnection(httpConnection);
    }
}

