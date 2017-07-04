package com.sun.correspond.service.send;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by sunGuiyong on 2017/6/23.
 */
public class SendOtherData extends SendData{
    @Override
    public PrintWriter getSendPackage(String key, Socket clientSocket) {
        return null;
    }

    @Override
    public byte[] getSendPackage(byte[] conBuf, String message) {
        byte[] contentBytes = new byte[2];
        try {
            contentBytes[0] = conBuf[0];
            contentBytes[1] = (byte) message.getBytes("UTF-8").length;
        }catch(IOException e){
            e.printStackTrace();
        }
        return contentBytes;
    }
}
