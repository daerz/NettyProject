package com.daerz.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/5
 * TODO 将本地文件中数据读出，打印到控制台
 *      File -> Chanel -> Buffer
 */
public class NIOFileChannelRead {

    public static void main(String[] args) throws Exception{
        //创建输入流
        File file = new File("c:\\Users\\daerz\\Desktop\\五月\\五月.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
