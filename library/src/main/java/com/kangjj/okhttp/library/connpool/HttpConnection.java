package com.kangjj.okhttp.library.connpool;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * @Description: 连接对象对socke的封装
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.connpool
 * @CreateDate: 2019/12/10 16:59
 */
public class HttpConnection {
    private final static String TAG = HttpURLConnection.class.getSimpleName();

    Socket socket;
    public long hasUseTime; //连接对象 最后使用的时间
    public HttpConnection(String host,int port,String protocol){
        try {

            if(TextUtils.isEmpty(protocol)){
                throw new IllegalArgumentException("protocol 不能为空");
            }
            if("HTTP".equalsIgnoreCase(protocol)){
                socket = new Socket(host,port);
            }else if("HTTPS".equalsIgnoreCase(protocol)){
                socket = SSLSocketFactory.getDefault().createSocket(host,port);
            }
            if(socket == null){
                throw new IllegalArgumentException("socket 未初始化");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过端口和域名判断是否相同
     * @param host
     * @param port
     * @return
     */
    public boolean isConnectionAction(String host,int port){
        if(socket == null){
            return false;
        }
        return socket.getPort()==port && socket.getInetAddress().getHostName().equalsIgnoreCase(host);
    }

    public void closeSocket(){
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "closeSocket: IOException e:" + e.getMessage());
            }
        }
    }

    public Socket getSocket(){
        return socket;
    }
}
