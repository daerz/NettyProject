package com.daerz.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置为非阻塞的
        socketChannel.configureBlocking(false);
        //建立连接
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接失败
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("正在连接服务器...请耐心等待");
            }
        }
        String str = "hello,大耳贼";
        //固定大小的Buffer,将发送的数据放入buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //将buffer里的数据写入到channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
