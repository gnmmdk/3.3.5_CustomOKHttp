package com.kangjj.custom.okhttp;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.custom.okhttp.testsocket
 * @CreateDate: 2019/12/4 10:18
 */
public class Test01 {
    private static final String PATH = "https://restapi.amap.com/v3/weather/weatherInfo" +
            "?city=110101&key=13cb58f5884f9749287abbead9c658f2";

//    public static void main(String[] args){
//        urlAction();
//    }
    @Test
    public void urlAction() {
        try {
            URL url = new URL(PATH);
            System.out.println(url.getProtocol()+"");//HTTP or HTTPS
            System.out.println(url.getHost());          //restapi.amap.com
            System.out.println(url.getFile());          //v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2
            System.out.println(url.getPort()+" ******** "+url.getDefaultPort());//HTTPS 443 HTTP 80
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void socketHTTPS(){
        try {
            // SSL 握手   访问HTTPS的socket客户端
            Socket socket = SSLSocketFactory.getDefault().createSocket("www.baidu.com",443);
            //写出去 请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("GET / HTTP/1.1\n");
            bw.write("Host: www.baidu.com\r\n\r\n");//第一个\r\n 是换行 第二个\r\n是换行
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String readline = null;
                if((readline = br.readLine())!=null){
                    System.out.println("响应数据:"+readline);
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意 公司网络会有限制
     */
    @Test
    public void socketHttp(){
        try {
            Socket socket = new Socket("restapi.amap.com",80);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n");
            bw.write("Host:restapi.amap.com\r\n\r\n");
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String readline = null;
                if((readline=br.readLine())!=null){
                    System.out.println("响应的数据:"+readline);
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void socketHttpPost(){
        try {
            Socket socket = new Socket("restapi.amap.com", 80); //  http:80
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("POST /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n");
            bw.write("Content-Length: 48\r\n");
            bw.write("Content-Type: application/x-www-form-urlencoded\r\n");
            bw.write("Host: restapi.amap.com\r\n\r\n");
            //下面是 POST请求体
            bw.write("city=110101&key=13cb58f5884f9749287abbead9c658f2\r\n");

            bw.flush();

            // TODO 读取数据 响应
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String readLine = null;
                if ((readLine = br.readLine()) != null) {
                    System.out.println("响应的数据：" + readLine);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void inputHostGetInfo() throws IOException{
        System.out.println("请输入网址，然后回车...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputPath = br.readLine();
        URL url = new URL("https://"+inputPath);
        String hostName = url.getHost();
        Socket socket = null;
        int port = 0;
        if("HTTP".equalsIgnoreCase(url.getProtocol())){
            port = 80;
            socket = new Socket(hostName,port);
        }else if("HTTPS".equals(url.getProtocol())){
            port = 443;
            socket = SSLSocketFactory.getDefault().createSocket(hostName,port);
        }
        if(socket == null){
            System.out.println("error");
            return;
        }
        // TODO 写出去  请求
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        /**
         * GET / HTTP/1.1
         * Host: www.baud.com
         */
        bw.write("GET / HTTP/1.1\r\n");
        bw.write("Host: "+hostName+"\r\n\r\n");
        bw.flush();

        // TODO 读取数据 响应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String readLine = null;
            if ((readLine = bufferedReader.readLine()) != null) {
                System.out.println("响应的数据：" + readLine);
            } else {
                break;
            }
        }
    }
}
