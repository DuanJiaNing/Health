package com.example.ai.forhealth;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    OkHttpClient client = new OkHttpClient();

    //Request是OkHttp中访问的请求，Builder是辅助类。Response即OkHttp中的响应。
    @Test
    private String run(String url)  {
//        Request request = new Request.Builder().url(url).build();
//        Response response = client.newCall(request).execute();
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
        System.out.println("this is Test");
        return "asdf";
    }

}