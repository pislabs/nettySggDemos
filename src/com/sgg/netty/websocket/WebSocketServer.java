package com.sgg.netty.websocket;

import com.sgg.netty.http.MyHttpServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 因为是基于http协议，需要使用http的编解码器
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写，添加ChunkedWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 说明
                             * 1. 因为http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
                             * 2. 这就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明
                             * 1. 对于Websocket，数据是以帧（frame）的形式传递
                             * 2. 可以看到WebSocketFrame下面有6个子类
                             * 3. 浏览器请求时，ws://localhost:7100/hello 表示请求的uri
                             * 4. WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                             *    http协议升级为websocket是通过发送状态码101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义的handler, 处理业务逻辑
                            pipeline.addLast(new TextWebScoketFrameHandler());
                        }
                    });

            // 这里的端口使用7200, 6668端口浏览器会提示非安全端口
            ChannelFuture channelFuture = serverBootstrap.bind(7200).sync();

            System.out.println("server is ready");

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
