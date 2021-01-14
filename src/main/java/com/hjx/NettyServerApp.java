package com.hjx;

import com.hjx.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyServerApp implements ApplicationRunner {
    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread.sleep(5000);
        this.nettyServer.startServer();
    }
}
