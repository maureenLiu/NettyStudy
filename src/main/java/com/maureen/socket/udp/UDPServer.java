package com.maureen.socket.udp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        byte buf[] = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length); //DatagramPacket接收对方UDP发过来的数据，存放在buf中
        DatagramSocket ds = new DatagramSocket(5678); //DatagramSocket:接收数据包的Socket，5678是UDP端口，所以TCP端口也可以是5678
        while (true) {
            ds.receive(dp); //有Client往端口中发数据，就将数据放在dp中
            //System.out.println(new String(buf, 0, dp.getLength())); //dp.getLength()收到的数据大小
            //接收到的数据包转换为long
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            DataInputStream dis = new DataInputStream(bais);
            System.out.println(dis.readLong());
        }
    }
}
