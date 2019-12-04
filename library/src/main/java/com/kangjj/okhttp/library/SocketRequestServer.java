package com.kangjj.okhttp.library;

import android.util.Base64;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 9:34
 */
public class SocketRequestServer {
    private static final String BLANK_SPACE = " ";
    private final String VERSION = "HTTP/1.1";
    private static final String GRGN = "\r\n";
    /**
     * 通过Request对象，寻找到域名HOST
     * @param request
     * @return
     */
    public String getHost(Request2 request){
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param request
     * @return
     */
    public int getPort(Request2 request) {
        try {
            URL url = new URL(request.getUrl());
            int port = url.getPort();
            return port==-1?url.getDefaultPort():port;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获得urlString HTTP or HTTPS
     * @param urlStr
     */
    public String queryHttpOrHttps(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return url.getProtocol();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取请求头的所有信息
     * @param request
     * @return
     */
    public String getRequestHeaderAll(Request2 request) {
        URL url = null;
        try {
            url= new URL(request.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String file = url.getFile();//v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2
        StringBuffer sb = new StringBuffer();
        sb.append(request.getRequetMethod())            //GET or POST
        .append(BLANK_SPACE)
        .append(file)
        .append(BLANK_SPACE)
        .append(VERSION)
        .append(GRGN);
        //获取请求集  进行拼接
        /**
         * Content-Length: 48\r\n
         * Host: restapi.amap.com\r\n
         * Content-Type: application/x-www-form-urlencoded\r\n
         */
        if(!request.getHeadList().isEmpty()){
            Map<String, String> mapList = request.getHeadList();
            for (Map.Entry<String, String> entry : mapList.entrySet()) {
                sb.append(entry.getKey())
                        .append(":").append(BLANK_SPACE)
                        .append(entry.getValue())
                        .append(GRGN);
            }
            // 拼接空行，代表下面的POST，请求体了
            sb.append(GRGN);
        }
        if(Request2.POST.equalsIgnoreCase(request.getRequetMethod())){
            sb.append(request.getRequestBody().getBody()).append(GRGN);
        }
        return sb.toString();
    }
}
