package com.kangjj.okhttp.library.chain;

import android.text.TextUtils;
import android.util.Log;

import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.Response2;
import com.kangjj.okhttp.library.SocketRequestServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.chain
 * @CreateDate: 2019/12/4 15:25
 */
public class ConnectionServerInterceptor implements Interceptor2 {
    private static final String TAG = ConnectionServerInterceptor.class.getSimpleName();
    @Override
    public Response2 doNext(Chain2 chain) throws IOException {

        SocketRequestServer srs = new SocketRequestServer();
        Request2 request = chain.getRequest();
        Socket socket = null;

        String protocol = srs.queryHttpOrHttps(request.getUrl());
        if(TextUtils.isEmpty(protocol)){
            throw new IllegalArgumentException("protocol 不能为空");
        }
        if("HTTP".equalsIgnoreCase(protocol)){
            socket = new Socket(srs.getHost(request),srs.getPort(request));
        }else if("HTTPS".equalsIgnoreCase(protocol)){
            socket = SSLSocketFactory.getDefault().createSocket(srs.getHost(request),srs.getPort(request));
        }
        if(socket == null){
            throw new IllegalArgumentException("socket 未初始化");
        }
        //请求
        OutputStream os = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        String requestAll = srs.getRequestHeaderAll(request);
        Log.d(TAG, "requestAll:" + requestAll);
        bufferedWriter.write(requestAll);
        bufferedWriter.flush();

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Response2 response = new Response2();
        //取出响应码
        String readLine = bufferedReader.readLine();// 读取第一行 响应头信息
        // 服务器响应的:HTTP/1.1 200 OK
        String[] httpStatus = readLine.split(" ");
        response.setStatusCode(Integer.parseInt(httpStatus[1]));
        try{
            while((readLine = bufferedReader.readLine())!=null){
                if("".equals(readLine)){
                    //读到空行，就代表下面是响应体了
                    response.setBody(bufferedReader.readLine());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        // input
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String readerLine = null;
//                while (true) {
//                    try {
//                        if ((readerLine = bufferedReader.readLine()) != null) {
//                            // Log.d(TAG, "服务器响应的:" + readerLine);
//                            System.out.println("服务器响应的:" + readerLine);
//                        } else {
//                            return;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();
//        response.setBody("流程走通....");
        return response;
    }
}
