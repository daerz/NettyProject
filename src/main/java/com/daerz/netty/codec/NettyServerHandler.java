package com.daerz.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/11
 * TODO
 *      1. 自定义一个Handler 需要继承netty规定的HandlerAdapter(规范)
 *      2. 继承后自定义的Handler, 才能符合为netty的handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据(在此方法读取客户端发送的消息)
     * @param ctx   上下文对象, 含有管道pipeline, 通道channel, 地址
     * @param msg   客户端发送的消息, 默认为Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ctx.channel().writeAndFlush(
                Unpooled.copiedBuffer("hello, 客户端, 我发了一个消息1", CharsetUtil.UTF_8));

        /*
        现在有个一个非常耗时的业务 -> 异步执行 -> 提交该channel对应的
        NioEventLoop的taskQueue中
        1. 解决方案1 用户程序自定义的普通任务(同一个线程处理,所以第二天消息等待时间30s)
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.channel().writeAndFlush(
                            Unpooled.copiedBuffer("hello, 客户端, 我发了休眠10s的消息2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.err.println("发生异常 " + e.getMessage());
                }
            }
        });

        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(20 * 1000);
                ctx.channel().writeAndFlush(
                        Unpooled.copiedBuffer("hello, 客户端, 我发了休眠30s的消息3", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.err.println("发生异常 " + e.getMessage());
            }
        });

        /*
        2. 用户自定义任务 -> 该任务提交到scheduleTaskQueue中
         */
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(20 * 1000);
                ctx.channel().writeAndFlush(
                        Unpooled.copiedBuffer("hello, 客户端, 我Schedule发了延迟5s后的消息4", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.err.println("发生异常 " + e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("server ctx =" + ctx);
        /*
        将msg转成一个ByteBuf
        ByteBuf是Netty提供的, 不是NIO的ByteBuffer
         */
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写到缓存,并刷新,并且对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理,关闭通道等
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
