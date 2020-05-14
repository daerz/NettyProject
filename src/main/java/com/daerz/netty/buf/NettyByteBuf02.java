package com.daerz.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/13
 * TODO
 *      1. 创建对象,包含一个byte[10]数组
 *      2. netty的buffer中, 不需要使用flip进行反转,
 *          因为底层维护了readerIndex 和 writerIndex 和 capacity, 将buffer分成了三部分
 *      3. readerIndex：已经读取的区域、readerIndex--writerIndex：可读的区域、writerIndex--capacity：可写的区域
 */
public class NettyByteBuf02 {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("hello, netty~", CharsetUtil.UTF_8);
        if (buf.hasArray()) {
            byte[] content = buf.array();
            System.out.println(new String(content, CharsetUtil.UTF_8));
            System.out.println("buf: "+ buf);
            System.out.println(buf.arrayOffset());
            System.out.println(buf.readerIndex());
            System.out.println(buf.writerIndex());
            System.out.println(buf.capacity());
            //编码
            System.out.println(buf.getByte(0));
            //可读字节数
            int len = buf.readableBytes();
            for (int i = 0; i < len; i++) {
                System.out.println((char) buf.getByte(i));
            }
            //按照范围读取
            System.out.println(buf.getCharSequence(4, 6, CharsetUtil.UTF_8));
        }
    }
}
