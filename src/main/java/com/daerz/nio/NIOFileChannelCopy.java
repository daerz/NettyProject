package com.daerz.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/5
 * TODO 用transferForm方式copy文件
 *      16
 */
public class NIOFileChannelCopy {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("c:\\Users\\daerz\\Desktop\\五月\\五月.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("c:\\Users\\daerz\\Desktop\\五月\\五月copy2.txt");

        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel writeChannel = fileOutputStream.getChannel();

        //transferForm形式copy
        writeChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        sourceChannel.close();
        writeChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
