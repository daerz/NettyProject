package com.daerz.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/14
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 1. 对入站的数据进行解码
     * 2. 业务处理
     * 3. 对出站的数据进行编码
     * 4. 业务处理
     * 解码 - 业务处理 - 编码
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyServerHandler());

    }
}
