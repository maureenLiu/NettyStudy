package com.maureen.socket.udp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        //字符串解析成字节数组byte[]
        //byte[] buf = (new String("Hello")).getBytes();

        //long类型数据转换成字节数组，打包发到server端
        long n = 1000L;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(n);
        byte[] buf = baos.toByteArray();
        //System.out.println(buf.length); //结果为8
        DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress("127.0.0.1", 5678));//每包UDP数据都要指定发到什么地址
        DatagramSocket ds = new DatagramSocket(9999); //Client占据了9999端口
        ds.send(dp);
        ds.close();

    }
}
