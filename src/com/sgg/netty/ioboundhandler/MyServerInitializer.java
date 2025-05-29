package com.sgg.netty.ioboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站的handler进行解码MyByteToLongDecoder
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 出站的handler进行编码MyLongToByteEncoder
        pipeline.addLast(new MyLongToByteEncoder());

        // 自定义handler进行业务处理
        pipeline.addLast(new MyServerHandler());
    }
}
