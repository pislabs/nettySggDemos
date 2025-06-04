package sgg.netty.dubborpc.customer;

import sgg.netty.dubborpc.common.Constants;
import sgg.netty.dubborpc.common.HelloService;
import sgg.netty.dubborpc.netty.NettyClient;
import sgg.netty.dubborpc.provider.HelloServiceImpl;

public class ClientBootstrap {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个消费者
        NettyClient customer = new NettyClient("127.0.0.1", 7200);

        // 创建代理对象
        HelloService service = (HelloService)customer.getBean(HelloService.class, Constants.providerName);

        for(;;) {
            // 通过代理对象调用服务提供者的方法（服务）
            String res = service.hello("你好 dubbo ...");
            System.out.println("调用的结果 res = " + res);
            System.out.println();
            System.out.println();

            Thread.sleep(2 * 1000);
        }
    }
}
