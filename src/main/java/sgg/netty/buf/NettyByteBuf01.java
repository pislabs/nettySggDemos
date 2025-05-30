package sgg.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        // 1. 创建一个对象，对象包含一个数组arr, 是一个byte[10]
        // 2. 在netty的buffer中，不需要使用flip进行反转
        //    底层维护了raderindex和writeindex
        // 3. 通过raderindex和writeindex和capacity将buffer分为三个区
        //    0 - readindex 已读区域
        //    readindex - writeindex 可读区域
        //    writeindex - capacity 可写区域
        ByteBuf buffer = Unpooled.buffer(10);

        for(int i = 0; i< 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity" + buffer.capacity());

        // 输出
        for(int i = 0; i< buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
            System.out.println(buffer.getByte(i));
        }

        System.out.println("执行完毕");
    }
}
