package com.kangjj.okhttp.library;

import com.kangjj.okhttp.library.chain.ChainManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:请求体对象 用于POST请求
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/4 15:08
 */
public class RequestBody2 {

    public static final String TYPE = "application/x-www-form-urlencoded";
    private final String ENC = "utf-8";

    // 请求体集合  a=123&b=666
    Map<String,String> bodys = new HashMap<>();

    /**
     * 添加请求体信息
     * @param key
     * @param value
     */
    public void addBody(String key,String value){
        try {
            bodys.put(URLEncoder.encode(key,ENC),URLEncoder.encode(value,ENC));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到请求体信息
     * @return
     */
    public String getBody(){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : bodys.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if(sb.length()!=0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
