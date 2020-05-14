package com.daerz.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author 刘小猛 liuxiaomeng@dcocd.cn
 * @Date 2020/5/14
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器IP " + ctx.channel().remoteAddress() + "传来的Long数据：" + msg);
    }

    /**
     * 重新其发送数据
     * 1. 该处理器前一个handler是 MyLongToByteEncoder,这个handler处理后会对其发送的数据进行编码
     * 2. MyLongToByteEncoder 父类 MessageToByteEncoder
     *  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
     *         ByteBuf buf = null;
     *         try {
     *             if (acceptOutboundMessage(msg)) { //判断当前msg 是不是应该处理的类型，如果是就处理，不是就跳过encode
     *                 @SuppressWarnings("unchecked")
     *                 I cast = (I) msg;
     *                 buf = allocateBuffer(ctx, cast, preferDirect);
     *                 try {
     *                     encode(ctx, cast, buf);
     *                 } finally {
     *                     ReferenceCountUtil.release(cast);
     *                 }
     *
     *                 if (buf.isReadable()) {
     *                     ctx.write(buf, promise);
     *                 } else {
     *                     buf.release();
     *                     ctx.write(Unpooled.EMPTY_BUFFER, promise);
     *                 }
     *                 buf = null;
     *             } else {
     *                 ctx.write(msg, promise);
     *             }
     *         }
     * 3. 由上代码可知我们编写 Encoder 是要注意传入的数据类型和处理的数据类型一致
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Long msg = 123456L;
        System.out.println("客户端发送数据：" + msg);
        ctx.writeAndFlush(msg);
    }
}
