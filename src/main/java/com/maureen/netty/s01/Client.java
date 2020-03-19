package com.maureen.netty.s01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(1); //默认是核数 * 2个线程，Client通常只需要一个线程传入1即可：EventLoopGroup group = new NioEventLoopGroup(1); 当读写频繁的时候就传入2或者其他数值

        Bootstrap b = new Bootstrap(); //Bootstrap--辅助启动类
        try {
            //netty中所有方法都是异步的，如果要等方法执行完成，就要调用sync()。
            //如下code即可连上server：b.group().channel().handler().connect().sync()
//            b.group(group)                                      //指定哪个线程池来执行
//                    .channel(NioSocketChannel.class)           //指定连到服务器上的channel的类型；如果传入SocketChannel，就是BIO版，是阻塞版
//                    .handler(new ClientChannelInitializer())   //事件发生时交给哪个Handler处理
//                    .connect("localhost",8888)      //线程池中的一个线程去完成的connect操作，connect是一个异步方法，要想等该方法执行完成才能往下继续执行，必须要执行sync()
//                    .sync();                        //sync()等待执行结束才会继续往下执行

            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())     //connect执行了才被执行，不需要等connect执行结束
                    .connect("localhost", 8888); //connect是否成功，要看返回值
            //如果不加sync，ChannelFuture是否执行成功，需要设置Listener来监听，因为connect比较耗时，所以不能直接就判断future结果
            f.addListener(new ChannelFutureListener() {
                //执行结果一出来，operationComplete就会被调用
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess())  //不启动Server，直接执行Client时就会输出"Not connected!"
                        System.out.println("Not connected!");
                    else
                        System.out.println("connected!");
                }
            });
            f.sync(); //阻塞直到有结果为止

            System.out.println("sync over！......");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception { //channel初始化之后就会被调用
        System.out.println("SocketChannel:" + ch);
        ch.pipeline().addLast(new ClientHandler()); //在已经初始化的channel上添加handler处理读写等事件
    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channel第一次连上可用，写出一个字符串Direct Memory
        //ByteBuf在虚拟机中直接访问操作系统的内存,叫做direct memory。buf指向的是直接内存，是操作系统的内存；不同于指向虚拟机内存，如果指向虚拟机内存，是不用管它释放与否
        //netty中往外写的所有内容都必须搞成ByteBuf，因为netty中读数据(channelRead)的时候，Object msg就是一个ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //
    }


}