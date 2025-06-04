package sgg.netty.dubborpc.common;

// 这个接口是服务提供方和服务消费方都需要
public interface HelloService {
    String hello(String msg);
}
