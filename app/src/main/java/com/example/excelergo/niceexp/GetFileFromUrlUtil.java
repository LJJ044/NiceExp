package com.example.excelergo.niceexp;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.GoBackAction;
import utils.GoBackEndCallBack;

public class GetFileFromUrlUtil {
    static FileOutputStream fileOutputStream=null;
    public static void getFileFromUrl(final String url, final Handler handler, final GoBackEndCallBack callBack){
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
                if(callBack!=null){
                    callBack.BackEnd(fileOutputStream,is);
                    Message message=new Message();
                    message.what=99;
                    handler.sendMessage(message);
                }else {
                handleFileContent(url, is, handler);
                }

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                {
                    connection.disconnect();
                }
            }
        }
    }).start();
    }
    private static void handleFileContent(String url,InputStream inputStream,Handler handler){
        File file=new File(FileDownloadutil.createFile(url).getAbsolutePath());
        if(inputStream!=null){
            int length;
            byte[] buff=new byte[2*1024];
            try {
                fileOutputStream=new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            do{
                try {
                    if((length=inputStream.read(buff))<=0){
                        break;

                    }
                    fileOutputStream.write(buff,0,length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while (true);

            try {
                if(fileOutputStream!=null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message message=new Message();
        message.what=100;
        handler.sendMessage(message);
    }
}
