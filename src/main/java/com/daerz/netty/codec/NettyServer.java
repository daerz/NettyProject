package com.daerz.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/11
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        /**
         * 创建BossGroup和 WorkerGroup
         * 1. 创建两个线程组bossGroup 和 workerGroup
         * 2. bossGroup只处理连接请求, 真正进行客户端业务处理, 会交给workerGroup完成
         * 3. 两个都是无限循环
         * 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)默认实际 cpu核数*2
         *    NettyRuntime.availableProcessors() * 2
         * 5. EventLoop对应一个selector
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象, 配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        /**
         * 使用链式编程设置参数
         * group: 设置bossGroup(parent)和workerGroup(child)
         * channel: 使用NioServerSocketChannel作为服务器的通道实现
         * option: 设置线程队列得到连接个数
         * childOption: 设置保存活动连接状态
         * childHandler: 创建一个通信测试对象(匿名对象)
         */
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 给pipeline设置处理器
                         * 给workerGroup的EventLoop对应的管道设置处理器
                         * @param channel
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            /*
                            可以使用集合管理SocketChannel,再推送消息时,可以将业务加入到各个channel
                            对应的NioEventLoop的taskQueue 或者 scheduleTaskQueue
                             */
                            System.out.println("客户socketChannel hashcode = " + channel.hashCode());
                            channel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器 is ready...");

            //绑定一个端口并且同步, 生成一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();
            //给channelFuture注册监听器, 监控我们关系的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
