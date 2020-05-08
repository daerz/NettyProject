package com.daerz.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 */
public class NIOServer {

    public static void main(String[] args) throws Exception{
        //创建一个服务器端socketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector
        Selector selector = Selector.open();
        //绑定端口6666,在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //将ServerSocketChannel注册到Selector,关系事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //等待客户端响应
        while (true) {
            //等待1s,无事件发生,continue
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待1s,无连接...");
                continue;
            }
            /**
             * 有事件发生，获取到SelectionKey集合
             * 1. >0 说明有事件发生,已经获取到关注的事件
             * 2. selector.selectedKeys()返回关注的事件集合
             * 3. 通过selectionKeys获取响应channel
             * 4. 注册进Selector的 selector.keys()
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();


            Iterator<SelectionKey> keysIterator = selectionKeys.iterator();
            while (keysIterator.hasNext()) {
                SelectionKey key = keysIterator.next();
                //关系事件为OP_ACCEPT,客户端建立连接,创建一个socketChannel注册进Selector
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功 生成了一个SocketChannel..." + socketChannel.hashCode());
                    //注册进Selector, 和buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("注册到Selector的SelectionKey数量：" + selector.keys().size());
                    System.out.println("发生事件的SelectionKey数量：" + selector.selectedKeys().size());
                }
                //关系事件为OP_READ
                if (key.isReadable()) {
                    //通过SelectionKey反向获取对应channel,向下转型
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取关联的buffer,将数据读入buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()));
                }
                //处理完后删除当前SelectionKey,防止重复操作
                keysIterator.remove();
            }
        }
    }
}
