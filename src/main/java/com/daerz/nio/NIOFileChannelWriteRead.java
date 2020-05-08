package com.daerz.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/5
 * TODO 将本地文件数据,拷贝到另一文件,只能使用一个Buffer
 */
public class NIOFileChannelWriteRead {

    public static void main(String[] args) throws Exception{
        File file = new File("c:\\Users\\daerz\\Desktop\\五月\\五月.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //读本地文件通道
        FileChannel readChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);


        //写入本地文件
        FileOutputStream fileOutputStream = new FileOutputStream("c:\\Users\\daerz\\Desktop\\五月\\五月copy.txt");
        FileChannel writeChannel = fileOutputStream.getChannel();
        while (true) {
            //清空buffer,复位position，limit，因为flip并write后position=limit，read为0
            byteBuffer.clear();
            int read = readChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            //需要反转下,因为当前position指向的是上次读入后位置
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
