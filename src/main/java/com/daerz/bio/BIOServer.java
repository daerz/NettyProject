package com.daerz.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/4
 */
public class BIOServer {

    private static int corePoolSize = 2;
    private static int maxPoolSize = 4;
    private static int keepAliveTime = 3;
    private static int blockQueueSize = 3;


    /**
     * 启动ServerSocket
     * @param args
     * @throws IOException
     * TODO 可以使用Telnet 127.0.0.1 6666进行连接到ServerSocket
     *      -> Ctrl+] -> send something
     */
    public static void main(String[] args) throws IOException {
        //使用线程池的方式
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(blockQueueSize),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器已启动");
        System.out.println("等待连接...");
        while (true) {
            //监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //就创建一个线程，与之通信
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }
    /**
     * 用于和客户端通讯
     * @param socket
     */
    public static void handler(Socket socket) {
        System.out.println(Thread.currentThread().getId() + "->" + Thread.currentThread().getName());
        try {
            byte[] bytes = new byte[1024];
            //获取输入流
            InputStream inputStream = socket.getInputStream();
            //读取客户端发送数据
            while (true) {
                System.out.println("read...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        }

    }
}
