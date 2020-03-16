package com.maureen.socket.tcp.example2;

import java.io.*;
import java.net.Socket;

public class TestTCPClient {
    public static void main(String[] args) {
        InputStream in = null;
        OutputStream out = null;

        try {
            Socket socket = new Socket("127.0.0.1", 5888);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            DataInputStream dis = new DataInputStream(in);
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF("hey");
            String s = null;
            if ((s = dis.readUTF()) != null) {
                System.out.println(s);
            }
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
