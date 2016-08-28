package cn.huimin.syn.codec;

import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 消息解码器
 * Created by kevin  on 16/8/26.
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    //消息体解码工具
    private Decoder decoder;


    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, Decoder decoder) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.decoder = decoder;
    }


    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf msg = (ByteBuf)super.decode(ctx, in);
        if(msg == null){
            return null;
        }
        Message message = new Message();
        Header header =  new Header();
        //解析消息头
        header.setCrcCode(msg.readInt());
        header.setLength(msg.readInt());
        header.setMessageId(msg.readLong());
        header.setType(msg.readByte());
        message.setHeader(header);
        //如果消息体存在,解析消息体
        if(msg.readableBytes() > 4){
            message.setBody(decoder.decode(msg));
        }
        return message;
    }



}
