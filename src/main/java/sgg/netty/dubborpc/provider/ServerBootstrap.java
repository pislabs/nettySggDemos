package sgg.netty.dubborpc.provider;

import sgg.netty.dubborpc.netty.NettyServer;

// ServerBootstrap会启动一个服务提供者，就是NettyServer
public class ServerBootstrap {
    public static void main(String[] args) throws Exception {
        NettyServer.startServer("127.0.0.1", 7200);
    }
}
