package com.daerz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 * TODO Scattering：将数据写入Buffer时，可以采用Buffer数组，依次写入 [分散]
 *      Gathering：从Buffer读取数据时，可以采用Buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {

        //使用ServerSocketChannel和SocketChannel网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        //等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假定从客户端接收8个字节
        int messageLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead = " + byteRead);
                //打印buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position=" +
                        byteBuffer.position() + ", limit=" + byteBuffer.limit()).forEach(System.out::println);
            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(Buffer::flip);
            //将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }
            //将所有buffer进行clear
            Arrays.asList(byteBuffers).forEach(Buffer::clear);
            System.out.println("byteRead=" + byteRead + " byteWrite=" + byteWrite +
                    " messageLength=" + messageLength);
        }
    }
}
