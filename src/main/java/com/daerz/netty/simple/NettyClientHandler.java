package com.daerz.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/12
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道准备就绪会触发该方法
     * @param ctx 上下文对象,包含大量信息
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        System.out.println("client " + ctx);
        System.out.println("看下channel 和 pipeline的关系,相互引用");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: im ready", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时, 会触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 当有异常抛出时, 会触发
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
