package cn.huimin.syn.codec;

import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 消息编码器
 * Created by kevin  on 16/8/26.
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {



    private Encoder encoder;


    public MessageEncoder(Encoder encoder){
        this.encoder = encoder;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if(msg == null){
            throw new Exception("message need encode is null");
        }

        //写消息头
        Header header = msg.getHeader();
        out.writeInt(header.getCrcCode());
        out.writeInt(header.getLength());//消息长度站位,非真实消息长度
        out.writeLong(header.getMessageId());//消息id
        out.writeByte(header.getType());
        //写消息体
        byte[] body = encoder.encode(msg.getBody());
        out.writeInt(body.length);
        out.writeBytes(body);
        //设置消息长度
        out.setInt(4,out.readableBytes() - 8);
    }
}
