package com.tcpserver.utils;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author chenjunlong
 */
public class ChannelCacheUtils {

    private static final String UID_S = "uid";
    private static final String ROOM_ID_S = "roomId";

    private static final AttributeKey<Long> UID = AttributeKey.valueOf(UID_S);
    private static final AttributeKey<String> ROOM_ID = AttributeKey.valueOf(ROOM_ID_S);

    public static void add(Channel channel, String roomId, Long uid) {
        channel.attr(ROOM_ID).set(roomId);
        channel.attr(UID).set(uid);
    }

    public static void clear(Channel channel) {
        channel.attr(ROOM_ID).set("");
        channel.attr(UID).set(0L);
    }

    public static Long getUid(Channel channel) {
        return channel.attr(UID).get();
    }

    public static String getRoomId(Channel channel) {
        return channel.attr(ROOM_ID).get();
    }
}
