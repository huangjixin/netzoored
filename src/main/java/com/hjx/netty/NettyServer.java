package com.hjx.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Service;

@Service
@ChannelHandler.Sharable
public class NettyServer {
    private static final String TAG = "NettyServer";
    public  static Channel channel = null;
    //端口
    private static final int PORT = 7010;

    ServerBootstrap b = new ServerBootstrap();
    /**
     * 启动tcp服务端
     */
    public void startServer() {
        try {
            // 創建反應器綫程組，第一個俗稱“包工頭”，負責連接IO事件的監聽，第二個俗稱“工人”，負責傳輸通道的IO事件的處理。
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            // 設置反應器的綫程組
            b.group(bossGroup, workerGroup)
                    // 設置通道類型
                    .channel(NioServerSocketChannel.class)
                    // 設置子通道流水綫
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加发送数据编码器
                            pipeline.addLast(new ServerEncoder());
                            //添加解码器，对收到的数据进行解码
                            pipeline.addLast(new ServerDecoder());
                            //添加数据处理
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            //服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成
            b.bind(PORT).sync();
            System.out.println("TCP 服务启动成功 PORT = " + PORT);
//            Log.d(TAG, );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyServer nettyServer = new NettyServer();
        nettyServer.startServer();

        /*Thread.sleep(10000);
        if(channel != null){
            boolean isWritable = channel.isWritable();
            if(isWritable){
                channel.writeAndFlush("客户端已经和服务端建立起来长连接了");
            }
        }*/

    }
}
