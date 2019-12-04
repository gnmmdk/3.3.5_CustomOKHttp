package com.kangjj.okhttp.library;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 9:32
 */
public class Response2 {

    private String body;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String string(){
        return body;
    }

}
