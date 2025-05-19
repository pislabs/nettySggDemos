package com.sgg.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup和WorkerGroup
        // 说明
        // 1. 创建两个线程组： bossGroup和workerGroup
        // 2. bossGroup只是处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        // 3. 两个都是无限循环
        // 4. bossGroup和workerGroup含有的子线程数为可用处理器的数量*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用
            serverBootstrap.group(bossGroup, workerGroup)   // 设置两个线程组
                    .channel(NioServerSocketChannel.class)    // NioServerSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象）
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户socketChannel hashcode =" + ch.hashCode());   // 可以使用一个集合管理SocketChannel, 在推送消息时，可以将业务加入到各个channel对应的NioEventLoop的taskQueue或scheTaskQueue
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给我们的workGrouop的EventLoop对应的管道设置处理器

            System.out.println("服务器 is ready...");

            // 绑定一个端口并同步处理
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
