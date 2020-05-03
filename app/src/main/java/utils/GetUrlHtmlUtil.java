package utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUrlHtmlUtil {
    public static String htmlContent;
    public static void getUrlHtml(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url1 = new URL(url);
                    connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream is = connection.getInputStream();
                    handleMsg(is,handler);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

        }).start();
    }
    private static void handleMsg(final InputStream inputStream,Handler handler) throws IOException {
        StringBuilder sb=new StringBuilder();
        String str;
        if(inputStream!=null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
          while ((str=br.readLine())!=null) {
             sb.append(str);
            }
            htmlContent=sb.toString();
            inputStream.close();
            /*handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("html数据",htmlContent);
                    webView.loadDataWithBaseURL(null,htmlContent,"text/html","utf-8",null);
                }
            });*/
            Message message=new Message();
            message.what=11;
            message.obj=htmlContent;
            handler.sendMessage(message);
        }
    }


}
