package com.tcpserver.model;

/**
 * @author chenjunlong
 */
public class MessageRequest {

    private String roomId;
    private Long uid; // 观众uid
    private int behavior; // 1建立连接 2断开连接
    private String msg;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getBehavior() {
        return behavior;
    }

    public void setBehavior(int behavior) {
        this.behavior = behavior;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
