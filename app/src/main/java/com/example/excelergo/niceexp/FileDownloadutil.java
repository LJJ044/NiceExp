package com.example.excelergo.niceexp;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDownloadutil {
    static String fileName;
    static String FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/myinfo/myFiles/";
    public static File createFile(String url){
        int i=url.lastIndexOf("/");
        String unConFileName=url.substring(i+1);
        if(unConFileName.endsWith(".mp3")||unConFileName.endsWith(".m4a")||unConFileName.endsWith(".wav")||unConFileName.endsWith(".mp4")||unConFileName.endsWith(".3gp")||
                unConFileName.endsWith(".jpg")||unConFileName.endsWith(".jpeg")||unConFileName.endsWith(".png")||unConFileName.endsWith(".apk")||unConFileName.endsWith(".pdf")||
                unConFileName.endsWith(".word")||unConFileName.endsWith(".ppt")||unConFileName.endsWith(".txt")) {
            fileName = unConFileName;
        }else if(unConFileName.contains(".mp3")) {
            fileName=unConFileName+".mp3";
        }else if(unConFileName.contains(".m4a")){
            fileName=unConFileName+".m4a";
        }else if(unConFileName.contains(".wav")){
            fileName=unConFileName+".wav";
        }else if(unConFileName.contains(".mp4")){
            fileName=unConFileName+".mp4";
        }else if(unConFileName.contains(".3gp")){
            fileName=unConFileName+".3gp";
        }else if(unConFileName.contains(".jpg")){
            fileName=unConFileName+".jpg";
        }else if(unConFileName.contains(".jpeg")){
            fileName=unConFileName+".jpeg";
        }else if(unConFileName.contains(".png")){
            fileName=unConFileName+".png";
        }else if(unConFileName.contains(".apk")){
            fileName=unConFileName+".apk";
        }else if(unConFileName.contains(".pdf")){
            fileName=unConFileName+".pdf";
        }else if(unConFileName.contains(".word")){
            fileName=unConFileName+".word";
        }else if(unConFileName.contains(".ppt")){
            fileName=unConFileName+".ppt";
        }else if(unConFileName.contains(".txt")) {
            fileName=unConFileName;
        }else {
            fileName=unConFileName;
        }

        File fileDir=new File(FilePath);
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        File file=new File(FilePath+fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public static void deleteFile(){
        File file=new File(FilePath+fileName);
        if(file.exists()){
            file.delete();
        }
    }
    public static void deleteSpecificFile(String fileDeleteName){
        File file=new File(FilePath+fileDeleteName);
        if(file.exists()){
            file.delete();
        }
    }
    public static List<String> listAllFileNmae(){
        List<String> list=new ArrayList<>();
        File file=new File(FilePath);
        if(file.exists()&&file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    list.add(files[i].getName());
                }
            }
        }
        return list;
    }
    //获取文件类型
    public static String getMimeType(File file){
        String type="";
        String name=file.getName();
        String end=name.substring(name.lastIndexOf(".")+1,name.length()).toLowerCase();
        switch (end) {
            case "m4a":
            case "mp3":
            case "wav":
                type = "audio";
                break;
            case "mp4":
            case "3gp":
                type = "video";
                break;
            case "jpg":
            case "png":
            case "jpeg":
                type = "imge";
                break;
            case "apk":
                type = "application";
                break;
            case "pdf":
                type="pdf";
                break;
            case "word":
                type="word";
                break;
            case "ppt":
                type="ppt";
                break;
            case "txt":
                type="txt";
                break;
            default:
                type = "*";
                break;
        }
        type+="/*";
        return type;
    }
}
