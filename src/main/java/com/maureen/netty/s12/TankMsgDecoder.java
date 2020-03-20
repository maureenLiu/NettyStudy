package com.maureen.netty.s12;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8)  //至少8个字节，不到8个字节就没读全，就不进行处理。解决了TCP拆包 粘包的问题
            return;
        ;

        //in.markReaderIndex();

        int x = in.readInt();
        int y = in.readInt();

        out.add(new TankMsg(x, y));
    }
}
