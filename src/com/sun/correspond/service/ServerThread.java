package com.sun.correspond.service;

/**
 * Created by never on 2017/5/25.
 *
 */
import com.sun.correspond.JNIToC;
import com.sun.correspond.service.receive.ReceiveHandPackage;
import com.sun.correspond.service.receive.RecvData;
import com.sun.correspond.service.receive.RecvOtherData;
import com.sun.correspond.service.send.SendData;
import com.sun.correspond.service.send.SendHandPackage;
import com.sun.correspond.service.send.SendOtherData;
import com.sun.correspond.tools.CopyPicToOtherDirectory;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private volatile boolean hasHandshake;

    public void run() {
        try {
            InputStream in = clientSocket.getInputStream();

            //解析收到的请求头
            //RecvData recv = RecvData.getInstance("receiveHead");
            RecvData recv = new ReceiveHandPackage();
            String recvStr = recv.getReceiveData(in);
            recv.analysisReceive(recvStr);

            if (!hasHandshake && !recv.propertyMap.get("Key").equals("Test")) {
                //握手
                //SendData send = SendData.getInstance("sendHead");
                SendData send = new SendHandPackage();

                PrintWriter pw = send.getSendPackage(recv.propertyMap.get("Key"), clientSocket);
                pw.flush();
                //将握手标志更新，只握一次
                hasHandshake = true;
                //服务器返回成功，---握手协议完成，进行TCP通信

                //得到客户端发过来的消息
                //RecvData recvData = RecvData.getInstance("receiveOther");
                RecvData recvData = new RecvOtherData();
                in = clientSocket.getInputStream();
                String recvOtherStr = recvData.getReceiveData(in);
                recvData.analysisReceive(recvOtherStr);

                if(recvData.picPath != null){
                    try {
                        JNIToC.setInformation(recvData.picPath, recvData.algorithmNum, recvData.overlyingPicNum, recvData.rotationNum);
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                }

                else
                    System.out.println("data array null");

                String pushMsg = JNIToC.getPictureInfo();

                /*String[] pathStr = pushMsg.split("\\\\");
                String picName = pathStr[pathStr.length-1].split("_")[0];
                System.out.println("receive " + picName);
                File file = new File(pushMsg);
                file.delete();
                String path = "E:\\project\\JavaProject\\ProcessCorrespond\\handledPic\\"+picName;*/
                pushMsg = CopyPicToOtherDirectory.copy(pushMsg);

                //向客户但发送消息
                if(pushMsg != null) {
                    SendData sendData = new SendOtherData();
                    OutputStream os = null;
                    os = clientSocket.getOutputStream();
                    byte[] pushHead = sendData.getSendPackage(RecvData.buf, pushMsg);
                    os.write(pushHead);
                    os.write(pushMsg.getBytes("UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
