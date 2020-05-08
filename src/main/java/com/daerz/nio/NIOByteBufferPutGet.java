package com.daerz.nio;

import java.nio.ByteBuffer;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(6);
        byteBuffer.putLong(100);
        byteBuffer.putChar('刘');

        byteBuffer.flip();
        System.out.println("-----");
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());

    }
}
