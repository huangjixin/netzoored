package com.hjx.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class NettyClient {
    private static final String TAG = "NettyClient";
    private final int PORT = 7010;
    //连接的服务端ip地址
    private final String IP = "127.0.0.1";
    private static NettyClient nettyClient;
    //与服务端的连接通道
    private Channel channel;
    private Bootstrap bootstrap;

    public static NettyClient getInstance() {
        if (nettyClient == null) {
            nettyClient = new NettyClient();
        }
        return nettyClient;
    }
    /**
     *需要在子线程中发起连接
     */
    private NettyClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();
    }
    /**
     * 获取与服务端的连接
     */
    public static Channel getChannel() {
        if (nettyClient == null) {
            return null;
        }
        return nettyClient.channel;
    }
    /**
     * 连接服务端
     */
    public void connect() {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            bootstrap = new Bootstrap()
                    // 指定channel类型
                    .channel(NioSocketChannel.class)
                    // 指定EventLoopGroup
                    .group(group)
                    // 指定Handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加发送数据编码器
                            pipeline.addLast(new ClientEncoder());
                            //添加解码器，对收到的数据进行解码
                            pipeline.addLast(new ClientDecoder());
                            //添加数据处理器
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            // 连接到服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(IP, PORT));
            //获取连接通道
            channel = channelFuture.sync().channel();
            // 添加连接状态监听
            channelFuture.addListener(new ConnectListener(this));

//            handler.obtainMessage(0, "连接成功").sendToTarget();
        } catch (Exception e) {
//            Log.e(TAG, "连接失败：" + e.getMessage());
            System.out.println("连接失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        try {
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(IP, PORT));
            // 添加连接状态监听
            channelFuture.addListener(new ConnectListener(this));
            channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.connect();
        Channel channel = NettyClient.getChannel();
        boolean isWritable = channel.isWritable();
        if(channel == null){
            return;
        }
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        if (isWritable) {
            while (true) {
                String name;
                try {
                    name = console.readLine();
                    if ("exit".equals(name)) {
                        System.exit(0);
                    }
//                    client.send(name);
                    if (!channel.isActive()) {
                        nettyClient.reConnect();
                    }
                    channel.writeAndFlush(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        if(isWritable){
//            channel.writeAndFlush("模拟客户端发送消息");
//            PkgDataBean bean = new PkgDataBean();
//            bean.setCmd((byte) 0x01);
//            bean.setData("hello,world".toString());
//            bean.setDataLength((byte) bean.getData().getBytes().length);
            //写入数据
//            channel.writeAndFlush(bean);

            /**
             * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
             * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
             * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
             * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
             * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
             * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
             */
            /*ObjectMapper mapper = new ObjectMapper();
            PkgDataBean pkgDataBean = new PkgDataBean();
            pkgDataBean.setData("21312321");
            pkgDataBean.setCmd((byte) 0x01);
            pkgDataBean.setDataLength((byte)"21312321".getBytes().length);
            //输出结果：{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}
            try {
                String json = mapper.writeValueAsString(pkgDataBean);
                System.out.println(json);
                channel.writeAndFlush(json);
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }*/

//        }

    }
}