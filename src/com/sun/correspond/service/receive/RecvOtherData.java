package com.sun.correspond.service.receive;

import com.sun.correspond.tools.ImgCompressTools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sunGuiyong on 2017/6/23.
 */
public class RecvOtherData extends RecvData {
    @Override
    public String getReceiveData(InputStream in) {
        byte[] res = null;
        int len = 0;
        try {
            len = getByteLen(in);
            System.out.println(len);
            res = copyByte(len);
            return getData(res, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void analysisReceive(String recv) {

        String[] str = recv.split("\r\n");
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
            if(i == 0) {
                saveToFile(str[i]);
            } else if(i == 1) {
                algorithmNum = getStringToInt(str[i]);
            }else if(i == 2){
                overlyingPicNum = getStringToInt(str[i]);
            } else if(i == 3){
                rotationNum = getStringToFloat(str[i]);
            }
        }
    }


    public int getStringToInt(String type){
        return Integer.parseInt(type);
    }

    public float getStringToFloat(String type){
        return Float.parseFloat(type);
    }

    /**
     * 根据url保存图片
     *
     * @param destUrl 图片的url地址
     */
    public void saveToFile(String destUrl) {
       /* FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        try {
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            String name = System.currentTimeMillis() + ".png";
            fos = new FileOutputStream("E:\\project\\JavaProject\\ProcessCorrespond\\originalPic\\"+name);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();

            picPath = "E:\\project\\JavaProject\\ProcessCorrespond\\originalPic\\"+name;
        } catch (IOException e) {
        } catch (ClassCastException e) {
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }*/

        String picName = destUrl.split("\\\\")[destUrl.split("\\\\").length-1];
        System.out.println("picName " + picName);
        File file =new File(destUrl);
        picPath = "E:\\project\\JavaProject\\ProcessCorrespond\\originalPic\\"+picName;
        try{
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos= new FileOutputStream(picPath);
                byte[] b = new byte[fis.available()];
                int len = 0;
                while ((len = fis.read(b)) != -1)
                {
                    fos.write(b, 0, len);
                    fos.flush();
                }
                fos.close();
                fis.close();
                //file.delete();
               /* return path;*/
            }
        }catch(IOException e){

        }
    }

    /**
     * 解析客户端数据包
     *
     * @param recBytes      服务器接收的数据包
     * @param recByteLength 有效数据长度
     * @return 接收到的字符串
     */

    public String getData(byte[] recBytes, int recByteLength) {
        if (recByteLength < 2) {
            return null;
        }
        boolean fin = (recBytes[0] & 0x80) == 0x80; // 1bit，1表示最后一帧
        if (!fin) {
            return null;
        }
        boolean mask_flag = (recBytes[1] & 0x80) == 0x80; // 是否包含掩码
        if (!mask_flag) {
            return null;// 不包含掩码的暂不处理
        }

        int payload_len = recBytes[1] & 0x7F; // 数据长度

        byte[] masks = new byte[4];
        byte[] payload_data;

        if (payload_len == 126) {
            System.arraycopy(recBytes, 4, masks, 0, 4);
            payload_len = (int) (recBytes[2] << 8 | recBytes[3]);
            payload_data = new byte[payload_len];
            System.arraycopy(recBytes, 8, payload_data, 0, payload_len);

        } else if (payload_len == 127) {
            System.arraycopy(recBytes, 10, masks, 0, 4);
            byte[] uInt64Bytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                uInt64Bytes[i] = recBytes[9 - i];
            }
            int len = uInt64Bytes[0];

            payload_data = new byte[len];
            for (int i = 0; i < len; i++) {
                payload_data[i] = recBytes[i + 14];
            }
        } else {
            System.arraycopy(recBytes, 2, masks, 0, 4);
            payload_data = new byte[payload_len];
            System.arraycopy(recBytes, 6, payload_data, 0, payload_len);
        }

        for (int i = 0; i < payload_len; i++) {
            payload_data[i] = (byte) (payload_data[i] ^ masks[i % 4]);
        }
        return new String(payload_data);
    }
}
