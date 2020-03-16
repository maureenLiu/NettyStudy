package com.maureen.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Single Thread模型：
 * Selector在Server端的Socket上设置了很多的key(事件)，会一直轮询
 * 如果轮询到有事件发生(key)，就加入到Set<SelectionKey>中,如有client想连接到server，就建立一条通道，再在这条通道上注册读事件感兴趣
 * 缺点：假设在处理某个Client的读操作的时候阻塞了，其他的客户端就无法连接了
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open(); //NIO对ServerSocket的封装，该通道是双向的，可以同时读写
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
        ssc.configureBlocking(false); //非阻塞模型

        System.out.println("server started,listening on :" + ssc.getLocalAddress());
        Selector selector = Selector.open(); //打开一个Selector
        ssc.register(selector, SelectionKey.OP_ACCEPT); //注册对什么感兴趣，最先感兴趣的是accept操作

        while (true) { //轮询
            selector.select(); //阻塞方法，感兴趣的操作发生了之后才会继续往下执行
            Set<SelectionKey> keys = selector.selectedKeys(); //keys即是selector往每一个ServerSocket都设置的监听器
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove(); //处理了一个事件后，就要remove，否则下一次轮询还会再处理
                handle(key);
            }
        }
    }

    private static void handle(SelectionKey key) {
        //判断是什么事件
        if (key.isAcceptable()) { //有客户端想连到Server端
            try {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept(); //nio中叫SocketChannel，bio中叫Socket
                sc.configureBlocking(false);
                sc.register(key.selector(), SelectionKey.OP_READ); //监控Read事件
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        } else if (key.isReadable()) { //
            SocketChannel sc = null;
            try {
                sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(512); //NIO模型中所有通道都是和Buffer连在一起的，BIO中都是一个字节一个字节
                buffer.clear();
                int len = sc.read(buffer);

                if (len != -1) {
                    System.out.println(new String(buffer.array(), 0, len));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (sc != null) {
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
