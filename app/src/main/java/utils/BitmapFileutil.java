package utils;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fragment.Fragment2;

public class BitmapFileutil {
    public static String ImgPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/myinfo/jpgs/";

    public static void saveImage(Bitmap icon) {
        File file2 = null;
        try {
            File file1 = new File(ImgPath);
            if (!file1.exists()) {
                file1.mkdir();
            }
            file2 = new File(ImgPath + Fragment2.webView.getTitle()+".JPG");
            if (!file2.exists()) {
                FileOutputStream fos = new FileOutputStream(file2);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void deletFiles(){
        File file=new File(ImgPath);
        if(file.exists()&&file.isDirectory()){
           File[] files=file.listFiles();
           if(files!=null) {
               for (int i = 0; i < files.length; i++) {
                   files[i].delete();
               }
           }
           file.delete();
        }
    }
    public static void createDir() {
        File file1 = new File(ImgPath);
        if (!file1.exists()) {
            file1.mkdir();
        }
    }
}
