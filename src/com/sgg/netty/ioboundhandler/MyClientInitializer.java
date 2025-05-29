package com.sgg.netty.ioboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出站的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        // 加入一个入站的handler，对数据进行解码
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 加入一个自定义的handler，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
