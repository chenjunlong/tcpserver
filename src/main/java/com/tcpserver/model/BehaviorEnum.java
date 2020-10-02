package com.tcpserver.model;

/**
 * @author chenjunlong
 */
public enum BehaviorEnum {

    JOIN(1), EXIT(2), NORMAL(3);

    private int behavior;

    BehaviorEnum(int behavior) {
        this.behavior = behavior;
    }

    public int getBehavior() {
        return behavior;
    }
}
