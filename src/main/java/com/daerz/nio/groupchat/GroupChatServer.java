package com.daerz.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/7
 * TODO 单Reactor单线程
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
            System.err.println("服务器已启动...");
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
                //阻塞,非阻塞传入事件参数s
                int count = selector.select();
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
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        }
                        //监听read
                        if (key.isReadable()) {
                            readData(key);
                        }
                        //删除已处理的SelectionKey
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取消息,并将消息转发到其他客户端
     * @param key
     */
    public void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            //读到缓冲
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count值处理
            if (count > 0) {
                //输出缓冲区消息
                String message = new String(buffer.array());
                System.out.println("客户端：" + message);
                //向其他客户端转发消息(去除自己)
                this.sendInfoToOtherClients(message, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 服务器将消息转发到其他客户端
     * @param message
     * @param self
     * @throws IOException
     */
    private void sendInfoToOtherClients(String message, SocketChannel self) throws IOException {
        //System.out.println("服务器转发消息中...");
        for (SelectionKey key : selector.keys()) {
            SelectableChannel targetChannel = key.channel();
            //排除自己和ServerSocketChannel
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel channel = (SocketChannel) targetChannel;
                //message存入buffer,写入channel
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                channel.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }

}
