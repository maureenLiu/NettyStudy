package com.maureen.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO的单线程模型
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open()
                .bind(new InetSocketAddress(8888));
        /**
         * 1、accept不阻塞了，调用完了，如果没有其他操作，main方法就结束了
         * 2、所以如果main方法执行按错当有Client连接来的时候，是谁来处理Client呢？就是CompletionHandler来处理连接上来的Client
         * 3、整个过程就是：主线程告诉os，给你一个Handler，什么时候有客户端连上来就调用这段代码
         */
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Object attachment) { //已经连接
                serverChannel.accept(null, this);
                try {
                    System.out.println(client.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //read()方法原本是阻塞的，现在也可以写成非阻塞的，和上面的accept一样
                    client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            System.out.println(new String(attachment.array(), 0, result));
                            client.write(ByteBuffer.wrap("HelloClient".getBytes()));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) { //连接失败
                            exc.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable exc, Object o) {
                exc.printStackTrace();
            }
        });
        //此处为了main方法不结束，所以加了一个while循环
        /*while (true) {
            Thread.sleep(1000);
        } */
    }
}
