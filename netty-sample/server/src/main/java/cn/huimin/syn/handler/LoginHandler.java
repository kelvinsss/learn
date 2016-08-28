package cn.huimin.syn.handler;

import cn.huimin.syn.Client;
import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.World;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by kevin  on 16/8/26.
 */
public class LoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;
        Header header = message.getHeader();
        if(header != null && header.getType() == MessageType.LOGIN_REQ.value()){
            Object body = message.getBody();
            JSONObject jsonObject = JSON.parseObject(body.toString());
            String wareHouseId = jsonObject.getString("whId");
            //登录消息,添加client到world
            Client client = new Client();
            client.setWareHouseId(wareHouseId);
            client.setChannel(ctx.channel());
            World world = World.getInstance();
            world.addClient(client);
            //响应登录成功到client
            Message resp = buildLoginResp(MessageType.LOGIN_RESP.value());
            ctx.writeAndFlush(resp);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private Message buildLoginResp(byte value) {
        Message message = new Message();
        Header header = new Header();
        header.setType(value);
        message.setHeader(header);
        return message;
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //移除client,关闭连接
        Channel channel = ctx.channel();
        World.getInstance().removeClientByChannel(channel);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        //移除client,关闭连接
        Channel channel = ctx.channel();
        World.getInstance().removeClientByChannel(channel);
        ctx.close();
    }
}
