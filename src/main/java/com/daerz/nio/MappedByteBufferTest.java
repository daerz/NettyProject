package com.daerz.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 * TODO 1.MappedByteBuffer 可以让文件直接在内存(堆外内存) 修改, 操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception{


        RandomAccessFile randomAccessFile = new RandomAccessFile("c:\\Users\\daerz\\Desktop\\五月\\五月.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2：0：可以直接修改的起始位置
         * 参数3：5：映射到内存的大小(不是索引),即将五月.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5
         * 实际类型DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        //IndexOutOfBoundsException
        //mappedByteBuffer.put(5, (byte) 'Y');
        randomAccessFile.close();
        System.out.println("修改成功");

    }
}
