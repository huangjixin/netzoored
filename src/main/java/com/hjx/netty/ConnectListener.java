package com.hjx.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

public class ConnectListener implements ChannelFutureListener {

    private static final String TAG = "ConnectListener";
    private NettyClient nettyClient;

    public ConnectListener(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

//    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        //连接失败发起重连
        if (!channelFuture.isSuccess()) {
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
//                @Override
                public void run() {
//                    Log.d(TAG, "连接失败，发起重连");
                    nettyClient.reConnect();
                }
            }, 10, TimeUnit.SECONDS);
        }
    }

}