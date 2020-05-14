package com.daerz.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/14
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 1. 对出站数据编码
     * 2. 对入站数据解码
     * 3. 出站时候编码，入站解码，互不干扰
     * 业务处理 -> 编码
     * 解码 -> 业务处理
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
