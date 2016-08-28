package cn.huimin.syn.handler;

import cn.huimin.syn.World;
import cn.huimin.syn.struct.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by kevin  on 16/8/26.
 */
public class ServiceHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        //业务消息,此处是客户端对消息的响应
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        //移除client,关闭连接
        Channel channel = ctx.channel();
        World.getInstance().removeClientByChannel(channel);
        ctx.close();
    }
}
