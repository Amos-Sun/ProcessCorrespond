package com.sun.correspond.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sunGuiyong on 2017/6/29.
 */
public class CopyPicToOtherDirectory {

    public static String copy(String originalPath){


        String oriPath = originalPath.split("_")[0];
        String picName = oriPath.split("\\\\")[oriPath.split("\\\\").length-1];
        String path = "E:\\project\\JavaProject\\ProcessCorrespond\\handledPic\\"+picName;
        System.out.println("receive " + picName);
        System.out.println("receive " + path);
        File file =new File(originalPath);
        try{
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos= new FileOutputStream(path);
                byte[] b = new byte[fis.available()];
                int len = 0;
                while ((len = fis.read(b)) != -1)
                {
                    fos.write(b, 0, len);
                    fos.flush();
                }
                fos.close();
                fis.close();
                file.delete();
                return path;
            }
        }catch(IOException e){

        }
        return null;
    }
}
