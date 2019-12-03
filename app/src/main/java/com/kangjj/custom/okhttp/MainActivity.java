package com.kangjj.custom.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;

import com.kangjj.okhttp.library.Call2;
import com.kangjj.okhttp.library.Callback2;
import com.kangjj.okhttp.library.OkHttpClient2;
import com.kangjj.okhttp.library.Request2;
import com.kangjj.okhttp.library.Response2;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String PATH = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void useMyOkhttp(View view) {
        OkHttpClient2 okHttpClient2 = new OkHttpClient2.Builder().build();

        Request2 request2 = new Request2.Builder().url(PATH).build();

        Call2 call2 = okHttpClient2.newCall(request2);

        call2.enqueue(new Callback2() {
            @Override
            public void onFailure(Call2 call, IOException e) {
                System.out.println("自定义OKHTTP请求失败....");
            }

            @Override
            public void onResponse(Call2 call, Response2 response) throws IOException {
                System.out.println("OKHTTP请求成功.... result:" + response.string());
            }
        });
    }
}
