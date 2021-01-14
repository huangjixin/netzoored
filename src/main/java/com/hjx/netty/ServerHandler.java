package com.hjx.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final String TAG = "ServerHandler";

    /**
     * 当收到数据的回调
     *
     * @param channelHandlerContext 封装的连接对像
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("收到了解码器处理过的数据：" + o.toString());
        channelHandlerContext.writeAndFlush("服务端发来信息，已经和客户端建立起来长连接了,你发的信息是："+o.toString());
//        Log.d(TAG, );
    }

    /**
     * 有客户端连接过来的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        NettyServer.channel = ctx.channel();
        /*System.out.println("有客户端连接过来：" + ctx.toString());
        Channel channel = ctx.channel();
        boolean isWritable = channel.isWritable();
        if(isWritable){
            channel.writeAndFlush("客户端已经和服务端建立起来长连接了");
        }*/
//        Log.d(TAG, "有客户端连接过来：" + ctx.toString());
    }

    /**
     * 有客户端断开了连接的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("有客户端断开了连接：" + ctx.toString());
//        Log.d(TAG, "有客户端断开了连接：" + ctx.toString());
    }
}