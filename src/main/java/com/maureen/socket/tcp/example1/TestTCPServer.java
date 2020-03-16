package com.maureen.socket.tcp.example1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestTCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            while (true) {
                Socket s1 = ss.accept();
                OutputStream os = s1.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("Hello," + s1.getInetAddress() + "port#" + s1.getPort() + " bye-bye!");
                dos.close();
                s1.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("程序运行出错：" + e);
        }

    }
}
