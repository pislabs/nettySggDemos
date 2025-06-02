package sgg.netty.tcpp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<ProtocolMessage> {
    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10条数据 hello, server
        for (int i=0; i< 10; i++) {
            String msg = "你好！" + i;
            byte[] content = msg.getBytes(CharsetUtil.UTF_8);
            int len = content.length;

            ProtocolMessage protocolMessage = new ProtocolMessage();
            protocolMessage.setLen(len);
            protocolMessage.setContent(content);

            ctx.writeAndFlush(protocolMessage);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) throws Exception {
        byte[] content = msg.getContent();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("客户端接收到消息：");
        System.out.println("长度=" + msg.getLen());
        System.out.println("内容=" + new String(content, CharsetUtil.UTF_8));
        System.out.println("客户端接收消息数量" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息=" + cause.getMessage());
        ctx.close();
    }
}
