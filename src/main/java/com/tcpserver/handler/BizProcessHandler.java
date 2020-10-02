package com.tcpserver.handler;

import com.google.gson.Gson;
import com.tcpserver.manager.ConnectRegistry;
import com.tcpserver.model.BehaviorEnum;
import com.tcpserver.model.MessageRequest;
import com.tcpserver.model.MessageResponse;
import com.tcpserver.push.PushService;
import com.tcpserver.utils.ChannelCacheUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * @author chenjunlong
 */
public class BizProcessHandler extends ChannelInboundHandlerAdapter {

    private final Gson gson = new Gson();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String roomId = ChannelCacheUtils.getRoomId(channel);
        Long uid = ChannelCacheUtils.getUid(channel);
        if (null == roomId || null == uid) {
            return;
        }
        ConnectRegistry.remove(roomId, uid, channel);
        ChannelCacheUtils.clear(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageRequest messageRequest = gson.fromJson((String) msg, MessageRequest.class);
        if (null == messageRequest) {
            return;
        }

        int behavior = messageRequest.getBehavior();
        String roomId = messageRequest.getRoomId();
        Long uid = messageRequest.getUid();
        Channel channel = ctx.channel();
        String clientIp = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
        int port = ((InetSocketAddress) channel.remoteAddress()).getPort();

        if (behavior == BehaviorEnum.JOIN.getBehavior()) {
            ConnectRegistry.add(roomId, uid, channel);
            ChannelCacheUtils.add(channel, roomId, uid);
            System.out.println(String.format("[%s:%s] roomId:%s, uid:%s join.", clientIp, port, roomId, uid));
        } else if (behavior == BehaviorEnum.EXIT.getBehavior()) {
            ConnectRegistry.remove(roomId, uid, channel);
            ChannelCacheUtils.clear(channel);
            System.out.println(String.format("[%s:%s] roomId:%s, uid:%s exit.", clientIp, port, roomId, uid));
        } else if (behavior == BehaviorEnum.NORMAL.getBehavior()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setRoomId(roomId);
            messageResponse.setUid(uid);
            messageResponse.setMsg(messageRequest.getMsg());
            PushService.send(messageResponse);
        } else {
            throw new UnsupportedOperationException(String.format("behavior:%s Unsupported.", behavior));
        }
    }

}
