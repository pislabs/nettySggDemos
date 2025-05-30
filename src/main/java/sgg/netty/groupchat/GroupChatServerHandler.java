package sgg.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GroupChatServerHandler extends SimpleChannelInboundHandler {
    // 定义channel组，管理所有的channel
    // GlobalEventExecutor.INSTANCE是一个全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//    public static Map<String, Channel> channels = new HashMap<String, Channel>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded表示连接建立，一旦连接建立第一个被执行
    // 将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息，推送给其他在线的客户端
        // 该方法会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + sdf.format(new Date()) +"\n");
        channelGroup.add(channel);
    }

    // 表示channel处于活动状态，提示xxx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了～");
    }

    // 表示channel处于非活动状态，提示xxx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了～");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户离开聊天的信息，推送给其他在线的客户端
        // 该方法会将channelGroup中所有的channel遍历，并发送消息

        // 触发channelRemove后，会自动从channelGroup中移除，无需手动移除
//        channelGroup.remove(channel);

        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开聊天\n");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取当前channel
        Channel channel = ctx.channel();

        // 遍历channelGrup，根据不同情况回送不同的消息

        channelGroup.forEach(ch -> {
            if (ch != channel) { // 不为当前channel直接转发
                ch.writeAndFlush("【客户】" + ch.remoteAddress() + " 发送了消息:" + msg + "\n");
            } else {
                // 回显自己发送的消息
                ch.writeAndFlush("【自己】发送了消息" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
