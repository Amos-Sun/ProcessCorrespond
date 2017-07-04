package com.sun.correspond.service.receive;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunGuiyong on 2017/6/23.
 */
public abstract class RecvData {

    public RecvData(){}
    //存放请求头的键值对
    public Map<String, String> propertyMap = new HashMap<>();
    public String picPath;
    public int algorithmNum;
    public int overlyingPicNum;
    public float rotationNum;

    /**
     * 得到客户端发过来的String
     * @param in 客户端输入流
     * @return 字符串
     */
    public abstract String getReceiveData(InputStream in);

    /**
     *对请求头进行解析，并未每一个对应的请求头标题赋值。
     * @param recv 发送过来的请求头
     */
    public abstract void analysisReceive(String recv);
    

    public static byte[] buf = new byte[1024];
    public int getByteLen(InputStream in) throws IOException {
        return in.read(buf, 0, 1024);//读到字节（读取输入流数据到缓存）,同时返回byte长度
    }
    public byte[] copyByte(int len){
        byte[] res = new byte[len];
        System.arraycopy(buf, 0, res, 0, len);//将buf内中数据拷贝到res中
        return res;
    }
}
