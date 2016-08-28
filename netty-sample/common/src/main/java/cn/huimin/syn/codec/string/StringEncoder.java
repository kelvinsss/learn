package cn.huimin.syn.codec.string;

import cn.huimin.syn.codec.Encoder;

/**
 * Created by kevin  on 16/8/26.
 */
public class StringEncoder implements Encoder {

    public byte[] encode(Object object) throws Exception{
        String body = (String) object;
        return body.getBytes("utf-8");
    }

}
