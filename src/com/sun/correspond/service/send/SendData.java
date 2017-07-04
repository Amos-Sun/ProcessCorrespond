package com.sun.correspond.service.send;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by sunGuiyong on 2017/6/23.
 */
public abstract class SendData {

    /**
     * 封装服务器返回的请求头
     * @param key key的值
     * @param clientSocket 连接的客户端的socket
     * @return PrintWriter
     */
    public abstract PrintWriter getSendPackage(String key, Socket clientSocket);

    /**
     * 打包服务器数据
     * @param conBuf nyte数组
     * @param message 数据
     * @return 发送byte数组
     */
    public abstract byte[] getSendPackage(byte[] conBuf, String message);

}
