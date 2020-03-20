package com.maureen.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class HelloNetty {
    public static void main(String[] args) {
        new NettyServer(8888).serverStart();
    }
}

class NettyServer {
    int port = 8888;

    public NettyServer(int port) {
        this.port = port;
    }

    public void serverStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //负责连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //负责连接之后的IO处理
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup) //两个group传给server启动的封装类
                .channel(NioServerSocketChannel.class) //指定连接通道的类型
                .childHandler(new ChannelInitializer<SocketChannel>() { //将每个连接的Client视作child，连接的Channel初始化后的操作，给通道加一个监听器来处理
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new Handler());//在通道上加一个通道的监听器
                    }
                });

        try {
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}

class Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { //出现异常，该方法会被调用
        cause.printStackTrace();
        ctx.close();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { //有数据写进来，channelRead会被自动调用
        System.out.println("server: channel read");
        ByteBuf buf = (ByteBuf) msg;

        System.out.println(buf.toString(CharsetUtil.UTF_8));

        ctx.writeAndFlush(msg);  //写回到客户端
        ctx.close();

        //buf.release();
    }
}
