package com.maureen.socket.tcp.example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket s = new Socket("127.0.0.1", 6666);//Client申请和服务端的6666端口建立连接

        OutputStream os = s.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF("Hello Server");
        dos.flush();
        dos.close();

    }
}
