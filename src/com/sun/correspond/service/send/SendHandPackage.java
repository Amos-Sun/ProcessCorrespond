package com.sun.correspond.service.send;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;

/**
 * Created by never on 2017/6/1.
 */
public class SendHandPackage extends SendData{

    @Override
    public PrintWriter getSendPackage(String key, Socket clientSocket){

        PrintWriter pw = null;//将向客户端写的字节流转成输出流
        OutputStream os = null;
        try {
            //拼接WEBSOCKET传输协议的安全校验字符串
            key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            //通过SHA-1算法进行更新
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-1");
            md.update(key.getBytes("utf-8"), 0, key.length());
            byte[] sha1Hash = md.digest();
            //进行Base64加密
            sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            key = encoder.encode(sha1Hash);
            //服务器端返回输出内容
            os = clientSocket.getOutputStream();
            pw = new PrintWriter(os);
            pw.println("HTTP/1.1 101 Switching Protocols");
            pw.println("Upgrade: websocket");
            pw.println("Connection: Upgrade");
            pw.print("Sec-WebSocket-Accept: " + key + "\r\n");
            pw.print("\r\n");
            //pw.print("\r\n");
            //将握手标志更新，只握一次
        }catch(Exception e){
            e.printStackTrace();
        }
        return pw;
    }

    @Override
    public byte[] getSendPackage(byte[] conBuf, String message) {
        return null;
    }
}
