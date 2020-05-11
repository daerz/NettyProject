package com.daerz.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/9
 * TODO 客户端
 */
public class NewIOClient {


    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress("localhost", 7001));
        String fileName = "拷贝的文件路径";
        long startTime = System.currentTimeMillis();
        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        /**
         * 在linux下一个transferTo方法就可以完成传输
         * 在windows下, 一次调用transferTo只能发送8m, 就需要分段传输文件,
         * 并且要记录每次传输的位置(position)
         * transferTo底层使用到零拷贝
         */
        int position = 0;
        long transferCount = fileChannel.transferTo(position, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数 = " + transferCount + " 耗时：" +
                (System.currentTimeMillis() - startTime));
        //关闭
        fileChannel.close();
    }
}
