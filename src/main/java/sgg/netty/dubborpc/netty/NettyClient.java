package sgg.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private String hostname;
    private int port;

    public NettyClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    // 创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    private int count = 0;

    // 编写方法使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, (
                // 这段代码，每调用一次hello就会执行一次
                (proxy, method, args) -> {
                    System.out.println("(proxy, method, args) 进入 ..." + (++count));

                    if (client == null) {
                        initClient(this.hostname, this.port);
                    }

                    // 设置发给服务器端的信息
                    // providerName 协议头 args[0] 就是客户端调用api hello(???)的参数
                    client.setParams(providerName + args[0]);

                    String result =  (String) executor.submit(client).get();

                    System.out.println("返回结果 =" + result);

                    return result;
                }
        ));
    }

    // 初始化客户端
    private static void initClient(String hostname, int port) throws InterruptedException {
        client = new NettyClientHandler();

        // 创建NioEventGroup
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();

        System.out.println("--------客户端启动---------");
    }
}
