package sgg.netty.tcpp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode 方法被调用");

        // 需要将得到的二进制字节码转换成ProtocalMessage数据包（对象）
        int len = in.readInt();

        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        // 封装成MessageProtocol对象，放入out，传递给下一个handler进行业务处理
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setLen(len);
        protocolMessage.setContent(bytes);

        out.add(protocolMessage);
    }
}
