package com.sgg.netty.ioboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从客户端发送数据" + msg);
    }

    // 重写ChannelAction发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
//        ctx.writeAndFlush(1234567L);

        // 分析
        // 1. 因为long8个字节，需要判断有8个字节才能读取一个long
        // 2. 该处理器的前一个处理器是MyLongToByteEncoder
        // 3. 该处理器的前一个处理器是MyLongToByteEncoder父类是 MessageToByteEncoder
        // 4. 父类MessageToByteEncoder会判断当前数据类型是否为当前应该处理的类型，否则直接跳过
        // 5. 如下为字符串类型 则 会跳过MyLongToByteEncoder处理
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
