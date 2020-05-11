package com.daerz.nio.zerocopy;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/8
 * java io 客户端
 */
public class OldIOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 7001);
        String fileName = "文件.txt";
        FileInputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount = 0;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总字节数：" + total + ",耗时：" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
