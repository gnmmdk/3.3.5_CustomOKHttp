package com.kangjj.okhttp.library.chain;

import android.text.TextUtils;
import android.util.Log;

import com.kangjj.okhttp.library.OkHttpClient2;
import com.kangjj.okhttp.library.RealCall2;
import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.Response2;
import com.kangjj.okhttp.library.SocketRequestServer;
import com.kangjj.okhttp.library.connpool.ConnectionPool;
import com.kangjj.okhttp.library.connpool.HttpConnection;
import com.kangjj.okhttp.library.connpool.UseConnectionPool;

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

        ChainManager chainManager = (ChainManager) chain;

        RealCall2 realCall = chainManager.getCall();
        OkHttpClient2 client = realCall.getOkHttpClient();
        ConnectionPool connectionPool = client.getConnectionPool();

        String protocol = srs.queryHttpOrHttps(request.getUrl());

        String host =srs.getHost(request);
        int port =srs.getPort(request);
//        UseConnectionPool useConnectionPool = new UseConnectionPool();
//        useConnectionPool.useConnectionPool(connectionPool,host,port,protocol);

        // 开始模拟 连接服务器拦截器的动作
        // 首先从连接池里面获取，是否有连接对象可用
        HttpConnection httpConnection = connectionPool.getConnection(host,port);
        if(httpConnection == null){
            httpConnection = new HttpConnection(host,port,protocol);
            Log.d(TAG, "连接池里面没有 连接对象 ，需要实例化一个连接对象...");
        }else{
            Log.d(TAG, "复用池 里面有一个连接对象");
        }
//        // 模拟请求
//        // ....
//        Log.d(TAG, "给服务器 发送请求...");
        Socket socket= httpConnection.getSocket();

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
        }finally {
            httpConnection.hasUseTime = System.currentTimeMillis();
            connectionPool.putConnection(httpConnection);
            if(os!=null){       //记得必须关闭流，否则socket重新调用会获取不到数据
                os.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(bufferedReader!=null){
                bufferedWriter.close();
            }
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
