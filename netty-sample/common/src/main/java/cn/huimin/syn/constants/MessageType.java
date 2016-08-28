package cn.huimin.syn.constants;

/**
 * 消息类型定义
 * Created by kevin  on 16/8/26.
 */
public enum  MessageType {

    SERVICE_REQ((byte) 0, "业务请求消息"),
    SERVICE_RESP((byte) 1, "业务响应消息"),
    LOGIN_REQ((byte) 2, "登录请求消息"),
    LOGIN_RESP((byte) 3, "登录响应消息"),
    HEARTBEAT_REQ((byte) 4, "心跳请求消息"),
    HEARTBEAT_RESP((byte) 5, "心跳响应消息");


    private byte value;

    private String comment;

    MessageType(byte value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public byte value() {
        return this.value;
    }

    private String comment(){
        return this.comment;
    }
}
