package sgg.netty.dubborpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sgg.netty.dubborpc.common.Constants;
import sgg.netty.dubborpc.provider.HelloServiceImpl;

import java.util.Date;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 表示channel处于活动状态，提示xxx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端]" + ctx.channel().remoteAddress() + "已连接～");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息并调用服务
        System.out.println("msg =" + msg);

        // 客户端在调用服务器的api时，我们需要定义一个协议
        // 比如，我们要求每次发消息时都必须以某个字符串开头，eg. "HelloService#hello#"
        String msgStr = msg.toString();
        String headStr = Constants.providerName;

        if (msgStr.startsWith(headStr)) {
            String result = new HelloServiceImpl().hello(msgStr.substring(headStr.length()));

            System.out.println("发送返回消息 " + result);

            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
