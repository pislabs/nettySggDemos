package sgg.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * HttpObject客户端和服务端相互通讯的数据被封装成HttpObject
 */
public class MyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    // 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是否是HttpRequest请求
        System.out.println("user connetct" + ctx.channel().remoteAddress());

        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + ", handler hashcode =" + this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest)msg;


            URI uri = new URI(request.uri());

            // 构造一个http的响应，即httpResponse
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico， 不作响应");
                return;
            }

            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("Hello, 我是服务器", CharsetUtil.UTF_8);

            // 构造一个http的响应，即httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
