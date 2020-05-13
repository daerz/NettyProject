package com.daerz.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/13
 * TODO
 *      1. 创建对象,包含一个byte[10]数组
 *      2. netty的buffer中, 不需要使用flip进行反转,
 *          因为底层维护了readerIndex 和 writerIndex 和 capacity, 将buffer分成了三部分
 *      3. readerIndex：已经读取的区域、readerIndex--writerIndex：可读的区域、writerIndex--capacity：可写的区域
 */
public class NettyByteBuf01 {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        for (int i = 0; i < buf.capacity(); i++) {
            buf.writeByte(i);
        }

        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.readByte());
        }
    }
}
