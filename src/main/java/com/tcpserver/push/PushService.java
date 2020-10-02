package com.tcpserver.push;

import com.google.gson.Gson;
import com.tcpserver.manager.ConnectRegistry;
import com.tcpserver.model.MessageResponse;
import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjunlong
 */
public class PushService {

    private static final LinkedBlockingQueue<MessageResponse> messageQueue = new LinkedBlockingQueue<>(20000);

    private static final ThreadPoolExecutor sendThreadPool =
            new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());

    public static void send(MessageResponse msg) {
        messageQueue.offer(msg);
    }

    public PushService() {
        this.start();
    }

    private void start() {
        ThreadPoolExecutor sendBossThread =
                new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());
        sendBossThread.execute(() -> {
            while (true) {
                MessageResponse msg = null;
                try {
                    msg = messageQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendThreadPool.execute(new PushTask(msg));
            }
        });
    }

}


class PushTask implements Runnable {

    private final Gson gson = new Gson();

    private MessageResponse msg;

    public PushTask(MessageResponse msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        Set<Long> uids = ConnectRegistry.getUidsByRoomId(msg.getRoomId());
        for (Long uid : uids) {
            Channel channel = ConnectRegistry.getChannelByUid(uid);
            channel.writeAndFlush(gson.toJson(msg));
        }
    }
}
