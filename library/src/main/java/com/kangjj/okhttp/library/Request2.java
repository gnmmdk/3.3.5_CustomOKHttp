package com.kangjj.okhttp.library;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 9:35
 */
public class Request2 {
    public static final String GET = "GET";
    public static final String POST = "POST";

    private String url;
    private String requetMethod = GET;//默认请求
    private Map<String,String> mHeadList = new HashMap<>();

    private RequestBody2 requestBody;

    public Request2() {
        this(new Builder());
    }

    public Request2(Builder builder) {
        this.url = builder.url;
        this.requetMethod = builder.requetMethod;
        this.mHeadList = builder.mHeadList;
    }

    public String getUrl() {
        return url;
    }

    public String getRequetMethod() {
        return requetMethod;
    }

    public Map<String, String> getHeadList() {
        return mHeadList;
    }

    public RequestBody2 getRequestBody() {
        return requestBody;
    }

    public final static class Builder{
        private String url;
        private String requetMethod = GET;//默认请求
        private Map<String,String> mHeadList = new HashMap<>();
        private RequestBody2 requestBody;


        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder get(){
            requetMethod = GET;
            return this;
        }

        public Builder post(RequestBody2 requestBody){
            requetMethod = POST;
            this.requestBody = requestBody;
            return this;
        }

        public Builder addReuestHeader(String key,String value){
            mHeadList.put(key,value);
            return this;
        }

        public Request2 build(){
            return new Request2(this);
        }
    }
}
