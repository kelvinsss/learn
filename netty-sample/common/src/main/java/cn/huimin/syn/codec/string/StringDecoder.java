package cn.huimin.syn.codec.string;

import cn.huimin.syn.codec.Decoder;
import io.netty.buffer.ByteBuf;

/**
 * Created by kevin  on 16/8/26.
 */
public class StringDecoder implements Decoder {


    public Object decode(ByteBuf buf)throws Exception{
        if(buf.readableBytes() < 4){
            return null;
        }
        int length = buf.readInt();
        byte[] msgArr = new byte[length];
        buf.readBytes(msgArr);
        return new String(msgArr, "utf-8");
    }


}
