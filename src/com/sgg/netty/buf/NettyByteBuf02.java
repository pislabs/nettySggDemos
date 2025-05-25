package com.sgg.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        // 1. 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);

        // 使用相关方法
        if(byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 将content转成字符串
            System.out.println(new String(content, CharsetUtil.UTF_8));

            System.out.println("byteBuf" + byteBuf);
            System.out.println(byteBuf.arrayOffset());  // 0
            System.out.println(byteBuf.readerIndex());  // 0
            System.out.println(byteBuf.writerIndex());  // 12
            System.out.println(byteBuf.capacity()); // 64

            System.out.println("readable len = " + byteBuf.readableBytes()); // 可读的字节数
            byteBuf.readByte();
            System.out.println("readable len = " + byteBuf.readableBytes());

            // 使用for循环取出各个字节
            for(int i=byteBuf.readerIndex(); i< byteBuf.readableBytes(); i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            System.out.println(byteBuf.getCharSequence(0, 4, CharsetUtil.UTF_8));
            System.out.println(byteBuf.getCharSequence(4, 6, CharsetUtil.UTF_8));
        }
    }
}
