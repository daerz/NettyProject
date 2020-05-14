package com.daerz.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/13
 * TODO     如果做单对单聊天，需要保存id和对应channel，例如用ConcurrentHashMap实现
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {


    /**
     * 定义一个channel组，用来管理所有channel
     * GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded 表示连接建立，一旦连接，第一个被执行
     * 将当前channel加入到channelGroup
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        /*
        将该客户加入聊天的信息推送给其他在线的客户端
        该方法会将channelGroup中所有channel遍历，并发送消息
        且不会遍历自己
         */
        channelGroup.writeAndFlush("[客户端" + sdf.format(new Date()) + "]"  + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    /**
     * 离开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端" + sdf.format(new Date()) + "]"  + channel.remoteAddress() + "离开~");
        System.out.println("当前在线人数：" + channelGroup.size());
    }

    /**
     * 上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    /**
     * 离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    /**
     * 将发送的数据，轮询到其他在线客户端，并回显到发送人
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户" + sdf.format(new Date()) + "]"  + channel.remoteAddress() +
                        "说：" + msg);
            } else {
                ch.writeAndFlush("[你说：" + msg + "]");
            }
        });
    }

    /**
     * 异常处理，关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
