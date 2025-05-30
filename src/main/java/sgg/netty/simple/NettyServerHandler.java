package sgg.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 1. 我们自定义一个handler需要继承Netty规定好的Handler适配器（规范）
 * 2. 这时我们自定义的handler，才能称为handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件（这里我们可以读取客户端发送的消息）
     * @param ctx 上下问对象，含有管道pipeline（业务逻辑处理）,通道channel(数据处理)，地址
     * @param msg 客户端发送的数据 需根据实际情况转换
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 如果这里有耗时非常长的业务 -> 异步执行 -> 提交该channel对应的NioEventLoop的taskQueue中

        // 方案1：用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10* 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～喵2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });

        // 用户自定义定时任务，该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10* 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～喵2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on ...");

//        System.out.println("服务器读取线程" + Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        System.out.println("看看channel和pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();  // 本质时一个双向链表
//
//        // 将msg转成ByteBuf
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址是：" + channel.remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓冲并刷新
        // 一般来讲，我们对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
