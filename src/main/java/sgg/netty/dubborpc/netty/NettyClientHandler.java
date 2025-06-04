package sgg.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;  // 上下文

    private String result;  // 返回的结果

    private String params;  // 客户端调用方法时传入的参数

    /**
     * 与服务器的连接创建后，就会被调用 （1）
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 被调用");

        this.context = ctx; // 因为我们在其他方法会使用到ctx;
    }

    /**
     * 设置参数 （2）
     * @param params
     */
    public void setParams(String params) {
        System.out.println("setParams 被调用:" + params);

        this.params = params;
    }

    /**
     * 收到服务器的数据后，调用方法 （4）
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用:" + msg);

        this.result = msg.toString();

        // 收到服务器方法后，唤起等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 被代理对象调用，发送数据给服务器，发送外后等待被唤起（await ）-> 返回结果 （3）
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1 被调用:" + params);

        this.context.writeAndFlush(params);

         wait(); // 等待channelRead方法获取到服务器的结果后唤醒

        System.out.println("call2 被调用:" + result);

        return result;  // 服务方返回的结果 （5）
    }
}
