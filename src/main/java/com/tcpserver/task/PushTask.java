package com.tcpserver.task;

import com.tcpserver.manager.Registry;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenjunlong
 */
public class PushTask {

    private AtomicLong atomicLong = new AtomicLong(0L);

    public PushTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (null != Registry.channelRegistry && Registry.channelRegistry.size() > 0) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        String replyTime = dateFormat.format(new Date());
                        String body = String.format("[%s] 推送消息: %s \n", replyTime, atomicLong.incrementAndGet());
                        System.out.println(body);
                        for (ChannelHandlerContext ctx : Registry.channelRegistry) {
                            ctx.writeAndFlush(body);
                        }
                    } else {
                        System.out.println("client active is 0");
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
