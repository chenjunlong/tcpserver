package com.tcpserver;

import com.tcpserver.handler.ChildChannelHandler;
import com.tcpserver.push.PushService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author chenjunlong
 */
public class TcpServer {

    private int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChildChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            f.addListener((GenericFutureListener<ChannelFuture>) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    new PushService();
                    System.out.println("TcpServer is Running...");
                }
            });
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new TcpServer(7777).start();
    }
}

