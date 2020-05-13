package com.daerz.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/13
 * 异常处理
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String state = null;
            switch (event.state()) {
                case READER_IDLE:
                    state = "读空闲";
                    break;
                case WRITER_IDLE:
                    state = "写空闲";
                    break;
                case ALL_IDLE:
                    state = "读写空闲";
                    break;
                default:
                    state = "占位用的";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "----超时时间----" + state);
            System.err  .println("服务器做相应处理...");
            //如果发生空闲，关闭通道
            ctx.channel().close();
        }
    }
}
