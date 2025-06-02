package sgg.netty.tcpp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ProtocolMessage> {
    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) throws Exception {
        // 接收到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();

        // 将buffer转成字符串
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("服务器接收到数据：");
        System.out.println("长度 = " + len);
        System.out.println("内容 = " + new String(content, CharsetUtil.UTF_8));

        System.out.println("服务器接收到消息量" + (++this.count));




        // 回复消息
        String respContent = UUID.randomUUID().toString();

        byte[] bytes = respContent.getBytes(CharsetUtil.UTF_8);

        // 构建一个协议包
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setLen(bytes.length);
        protocolMessage.setContent(bytes);

        ctx.writeAndFlush(protocolMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
