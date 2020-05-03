package utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import interfaces.OnJsonStringCallBack;

public class MyOkHttpClientUtil  {
    private static OkHttpClient okHttpClient;

    public static void sendRequestForResult(final String jsonUrl, final OnJsonStringCallBack callBack){
        okHttpClient=new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {

                Request request=new Request.Builder().url(jsonUrl).build();
                final Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                            e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String newsItems=response.body().string();
                        if(callBack!=null){
                            callBack.goWithNewsString(newsItems);
                        }
                    }
                });
            }
        }).start();
    }

}
