package sgg.netty.dubborpc.provider;

import sgg.netty.dubborpc.common.HelloService;

public class HelloServiceImpl implements HelloService {
    private static int count = 0;

    // 当前有消费方调用该方法时就返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息 =" + msg);

        // 根据msg返回不同的结果
        if(msg != null) {
            return "你好客户端，我已经收到你的消息 [ " + msg + "] 第" + (++count) ;
        }

        return "你好客户端，我已经收到你的消息了";
    }
}
