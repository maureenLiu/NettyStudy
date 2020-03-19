package com.maureen.netty.s01;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

//BIO版的server
public class BIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(8888));

        Socket s = ss.accept();
        System.out.println("a client connected!");
    }
}
