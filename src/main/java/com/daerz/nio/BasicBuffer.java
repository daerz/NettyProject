package com.daerz.nio;

import java.nio.IntBuffer;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/5
 * TODO 关于Buffer的参数
 *      hp[]
 *      mark
 *      limit
 *      capacity 缓冲区容量
 *      position 缓冲区当前位置
 */
public class BasicBuffer {

    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i);
        }
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
