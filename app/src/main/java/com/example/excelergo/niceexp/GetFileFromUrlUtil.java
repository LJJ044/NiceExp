package com.example.excelergo.niceexp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.GoBackAction;
import utils.GoBackEndCallBack;
import utils.OnProgressChangeCallBack;

public class GetFileFromUrlUtil {
    static FileOutputStream fileOutputStream=null;
    static GoBackEndCallBack callBack;
    public static void getFileFromUrl(final String url, final Handler handler, final OnProgressChangeCallBack progressChangeCallBack){
    new Thread(new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection=null;
            try {
                URL url1=new URL(url);
                connection=(HttpURLConnection) url1.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream is=connection.getInputStream();
                handleFileContent(connection,url, is, handler,progressChangeCallBack);


            }catch (IOException e){
                e.printStackTrace();
            }finally {
                {
                    if(connection!=null) {
                        connection.disconnect();
                    }
                }
            }
        }
    }).start();
    }
    private static void handleFileContent(HttpURLConnection connection,String url,InputStream inputStream,Handler handler,OnProgressChangeCallBack progressChangeCallBack){
        File file=new File(FileDownloadutil.createFile(url).getAbsolutePath());
        if(inputStream!=null){
            int length=0;
            int n=0;
            int total=0;
            int contentLength=connection.getContentLength();
            byte[] buff=new byte[2*1024];
            try {
                fileOutputStream=new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            do{
                try {
                    if((length=inputStream.read(buff))<=0) {
                        break;
                    }
                    fileOutputStream.write(buff,0,length);
                    while (n<100)
                        n++;
                        total += length;


                    if (progressChangeCallBack != null) {
                        progressChangeCallBack.OnChangeState(contentLength,total);
                    }
                    if(callBack!=null){
                        fileOutputStream=null;
                        callBack.BackEnd();
                        Message message=new Message();
                        message.what=99;
                        handler.sendMessage(message);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("字节数",String.valueOf(contentLength));
        }while (true);

            try {
                if(fileOutputStream!=null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Message message=new Message();
                    message.what=100;
                    handler.sendMessage(message);
                }
                inputStream.close();
                callBack=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setCallBack(GoBackEndCallBack callBack) {
        GetFileFromUrlUtil.callBack = callBack;
    }
}
