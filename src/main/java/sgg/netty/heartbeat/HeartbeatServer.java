package sgg.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HeartbeatServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在bossGroup增加日志处理器
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 加入Netty提供的idleStateHandler
                            // 1. readerIdleTime: 读空闲，表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            // 2. writeIdleTime: 写空闲，表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            // 3. allIdleTime: 读写空闲，表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                            // 4. 当IdleStateEvent触发后，就会传递给管道的下一个handler去处理（通过下一个）
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                            // 加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new HeartbeatHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(7100).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
