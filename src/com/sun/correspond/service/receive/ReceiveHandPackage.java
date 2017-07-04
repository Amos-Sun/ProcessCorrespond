package com.sun.correspond.service.receive;

import com.sun.correspond.service.receive.RecvData;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by never on 2017/5/27.
 */
public class ReceiveHandPackage extends RecvData{

    //标志请求头的所有标题
    private final String[] INFO_STR = {
            "GET","Host","Connection","Pragma","Control"
            ,"Upgrade","Origin","Version","Agent","Encoding"
            ,"Language","Cookie","Key","Extensions"};

    public ReceiveHandPackage(){
        initPropertyMap();
    }

    @Override
    public String getReceiveData(InputStream in) {
        byte[] res=null;
        int len;
        try {
            len = getByteLen(in);
            res = copyByte(len);
            return new String(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void analysisReceive(String recv){

        String[] strArr = recv.split("\r\n");
        /*for(int i = 0; i < strArr.length; i++){
            System.out.println(strArr[i]);
        }*/
        for(int i = 0; i < strArr.length; i++){
            strArr[i] = strArr[i].substring(strArr[i].indexOf(INFO_STR[i]) + INFO_STR[i].length() + 1
                    , strArr[i].length()).trim();
        }

        for(int i = 0; i < strArr.length; i++){
            propertyMap.put(INFO_STR[i], strArr[i]);
        }
    }

    /**
     * 初始化请求头的键值对
     */
    private void initPropertyMap(){
        propertyMap.put("GET","Test");
        propertyMap.put("Host","Test");
        propertyMap.put("Connection","Test");
        propertyMap.put("Pragma","Test");
        propertyMap.put("Control","Test");
        propertyMap.put("Upgrade","Test");
        propertyMap.put("Origin","Test");
        propertyMap.put("Version","Test");
        propertyMap.put("Agent","Test");
        propertyMap.put("Encoding","Test");
        propertyMap.put("Language","Test");
        propertyMap.put("Cookie","Test");
        propertyMap.put("Key","Test");
        propertyMap.put("Extensions","Test");

    }
}
