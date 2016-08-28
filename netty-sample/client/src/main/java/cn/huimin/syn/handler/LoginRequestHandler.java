package cn.huimin.syn.handler;

import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by kevin  on 16/8/28.
 */
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    private Message buildLoginReq() {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("whId", "101");
        message.setBody(jsonObject.toJSONString());
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        ctx.close();
    }
}
