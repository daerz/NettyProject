package com.daerz.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/12
 * TODO 其实也是个Handler, 会放在头部
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        /*
        加入一个netty 提供的httpServerCodec codec => [coder - decoder]
        1. HttpServerCodec 是netty提供的处理http的 编-解码器
         */
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2. 增加一个自定义的handler
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
        System.out.println("init channel ok...");
    }
}
