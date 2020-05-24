package com.tcpserver.manager;

import io.netty.channel.ChannelHandlerContext;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author chenjunlong
 */
public class Registry {

    public static Set<ChannelHandlerContext> channelRegistry = new LinkedHashSet();

}
