package com.sgg.netty.ioboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * decode 会根据接收的数据，被调用多次，知道确定没有新的元素被添加到list，或者ByteBuf没有更多的可读字节为止
 * 如果list out不为空，就会将list内容传递给下一个
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param ctx           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in            the {@link ByteBuf} 入站的ByteBuf
     * @param out           the {@link List} 将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 被调用");
        // 因为Long8个字节，需要判断有8个字节，才能读取一个long
        if(in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
