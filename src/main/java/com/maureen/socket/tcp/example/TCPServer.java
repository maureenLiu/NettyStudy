package com.maureen.socket.tcp.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666); //ServerSocket在哪个端口监听Client的连接
        while (true) {
            Socket s = ss.accept(); //阻塞方法
            DataInputStream dis = new DataInputStream(s.getInputStream());
            System.out.println(dis.readUTF());
            dis.close();
            s.close();
        }

    }
}
