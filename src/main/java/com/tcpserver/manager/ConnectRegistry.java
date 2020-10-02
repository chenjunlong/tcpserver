package com.tcpserver.manager;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author chenjunlong
 */
public class ConnectRegistry {

    // user -> channel
    private static final ConcurrentHashMap<Long, Channel> userChannel = new ConcurrentHashMap<>(20000);
    // room -> user
    private static final SetMultimap<String, Long> roomUser =
            Multimaps.synchronizedSetMultimap(Multimaps.newSetMultimap(new HashMap<>(100), CopyOnWriteArraySet::new));

    public static void add(String roomId, Long uid, Channel channel) {
        userChannel.put(uid, channel);
        roomUser.put(roomId, uid);
    }

    public static void remove(String roomId, Long uid, Channel channel) {
        userChannel.remove(uid, channel);
        roomUser.remove(roomId, uid);
    }

    public static Set<Long> getUidsByRoomId(String roomId) {
        return roomUser.get(roomId);
    }

    public static Channel getChannelByUid(Long uid) {
        return userChannel.get(uid);
    }
}
