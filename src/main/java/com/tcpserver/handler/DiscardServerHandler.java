package com.tcpserver.handler;

import com.tcpserver.manager.Registry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenjunlong
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Registry.channelRegistry.add(ctx);
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        System.out.println(clientIp + ":" + port + "已连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Registry.channelRegistry.remove(ctx);
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
        System.out.println(clientIp + ":" + port + "已断开");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String replyTime = dateFormat.format(new Date());

        String body = (String) msg;
        for (ChannelHandlerContext ctx0 : Registry.channelRegistry) {
            body = String.format("[%s] 接收消息: %s \n", replyTime, body);
            ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
            ctx0.writeAndFlush(resp);
        }
    }

}
