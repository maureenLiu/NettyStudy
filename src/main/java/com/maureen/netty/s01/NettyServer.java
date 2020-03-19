package com.maureen.netty.s01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

//netty版Server
public class NettyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); //bossGroup只负责客户端的连接，可以指定为多个
        EventLoopGroup workerGroup = new NioEventLoopGroup(2); //处理连接后的socket上的IO事件处理，2表示2个线程

        try {
            ServerBootstrap b = new ServerBootstrap();
            ChannelFuture f = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pl = ch.pipeline();
                            pl.addLast(new ServerChildHandler());   //每一个客户端连接都会设置一个ServerChildHandler来处理数据
                        }
                    })
                    .bind(8888)        //监听在8888端口，该方法是异步的
                    .sync();                   //等bind完成，bind成功后才往下执行

            System.out.println("server started!");

            f.channel().closeFuture().sync(); //closeFuture是阻塞的，close()->ChannelFuture，没人调close()方法就会一直等待close方法被调用
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter { //SimpleChannelInboundHandler和Codec结合才能实现泛型
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            System.out.println(new String(bytes));
            // System.out.println(buf);
            //System.out.println(buf.refCnt()); //有多少对象指向了它
        } finally {
            if (buf != null) ReferenceCountUtil.release(buf); //释放内存,防止泄露
            //System.out.println(buf.refCnt());
        }

    }
}