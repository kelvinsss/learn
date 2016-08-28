package cn.huimin.syn.codec;

import io.netty.buffer.ByteBuf;

/**
 * 消息body反序列化工具
 * Created by kevin  on 16/8/26.
 */
public interface Decoder {

    Object  decode(ByteBuf buf)throws Exception;

}
