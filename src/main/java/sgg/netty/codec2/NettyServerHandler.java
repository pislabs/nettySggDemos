package sgg.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 * 1. 我们自定义一个handler需要继承Netty规定好的Handler适配器（规范）
 * 2. 这时我们自定义的handler，才能称为handler
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        if (msg.hasStudent()) {
            System.out.println("客户端发送的数据" + msg.getStudent().getId() + ", " + msg.getStudent().getName());
        } else if (msg.hasWorker()) {
            System.out.println("客户端发送的数据" + msg.getWorker().getName() + ", " + msg.getWorker().getAge());
        }

        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            System.out.println("客户端发送的数据" + msg.getStudent().getId() + ", " + msg.getStudent().getName());
        } else  if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            System.out.println("客户端发送的数据" + msg.getWorker().getName() + ", " + msg.getWorker().getAge());
        } else {
            System.out.println("其他传输类型");
        }
    }

//    /**
//     * 读取数据事件（这里我们可以读取客户端发送的消息）
//     * @param ctx 上下问对象，含有管道pipeline（业务逻辑处理）,通道channel(数据处理)，地址
//     * @param msg 客户端发送的数据 需根据实际情况转换
//     * @throws Exception
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        // 读取从客户端发送的StudentPojo.Student
//        StudentPOJO.Student student = (StudentPOJO.Student)msg;
//
//        System.out.println("客户端发送的数据" + student.getId() + ", " + student.getName());
//    }

    /*
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
