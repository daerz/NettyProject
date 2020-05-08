package com.daerz.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/8
 */
public class GroupChatClient {

    private static final String IP = "127.0.0.1";
    private static final int HOST = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    /**
     * init
     */
    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, HOST));
            socketChannel.configureBlocking(false);
            //将channel注册到Selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送数据
     */
    public void sendInfo(String info) {
        info = userName + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受服务器信息
     */
    public void receiveInfo() {
        try {
            //阻塞
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.keys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //有客户端连入,读取相关通道中buffer内容
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        String message = new String(buffer.array());
                        System.out.println(message.trim());
                    }
                    iterator.remove();
                }
            }/*else {
                System.out.println("没有可用的通道...");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();
        //开启一个线程,每隔3s读取一次服务器端数据
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    client.receiveInfo();
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String sc = scanner.nextLine();
            client.sendInfo(sc);
        }
    }
}
