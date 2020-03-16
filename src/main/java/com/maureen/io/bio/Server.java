package com.maureen.io.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("127.0.0.1", 8888));
        while (true) {
            Socket s = ss.accept(); //accept()是阻塞方法，有客户端连接时才会继续往下执行
            new Thread(() -> {
                handle(s);
            }).start();
        }
    }

    /**
     * read和write方法也都是阻塞的
     * read():如果client端没有写数据到server端，那么就一直阻塞在这里
     * write():如果client端接收，那么就一直阻塞在这里
     *
     * @param s
     */
    static void handle(Socket s) {
        try {
            byte[] bytes = new byte[1024];
            int len = s.getInputStream().read(bytes);
            System.out.println(new String(bytes, 0, len));

            s.getOutputStream().write(bytes, 0, len);
            s.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
