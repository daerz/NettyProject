package com.daerz.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/5
 * TODO 将字符串读入到本地文件
 */
public class NIOFileChannelWrite {

    public static void main(String[] args) throws Exception{
        //UTF-8，一汉字3字节
        String str = "hello,五月";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("c:\\Users\\daerz\\Desktop\\五月\\五月.txt");
        //这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        //将缓冲区数据写入channel
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}
