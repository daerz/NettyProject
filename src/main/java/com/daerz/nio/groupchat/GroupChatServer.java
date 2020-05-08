package com.daerz.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 */
public class GroupChatServer {

    /**
     * 注册中心
     */
    private Selector selector;
    /**
     * 用于监听连接的客户端
     */
    private ServerSocketChannel lisChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            lisChannel = ServerSocketChannel.open();
            //绑定端口
            lisChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            lisChannel.configureBlocking(false);
            //将Channel注册到Selector
            lisChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    public void listen() {
        try {
            //循环处理
            while (true) {
                int count = selector.select(2000);
                //有事件处理
                if (count > 0) {
                    //得到selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        //监听accept
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = lisChannel.accept();
                            //注册到Selector,设置为非阻塞
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            socketChannel.configureBlocking(false);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        }
                        //监听read
                        if (key.isReadable()) {

                        }
                        //删除已处理的SelectionKey
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
