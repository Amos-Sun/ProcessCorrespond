package com.sun.correspond;

import com.sun.correspond.service.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by never on 2017/5/25.
 *
 */
public class SocketToWeb {

    public static void main(String[] args) {

        ServerSocket server;//服务器监听socket
        Socket clientSocket = null;//得到客户端连接的socket
        try {
            server = new ServerSocket(8080);
            while (true) {
                System.out.println("等待客户端连接：");
                clientSocket = server.accept();

                if (clientSocket != null) {
                    ServerThread serverThread = new ServerThread(clientSocket);
                    serverThread.start();
                    serverThread.sleep(500);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
