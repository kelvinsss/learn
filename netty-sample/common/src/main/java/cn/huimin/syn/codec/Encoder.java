package cn.huimin.syn.codec;

/**
 * 消息body序列化工具
 * Created by kevin  on 16/8/26.
 */
public interface Encoder {


    byte[] encode(Object object) throws Exception;
}
