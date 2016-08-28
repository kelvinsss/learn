package cn.huimin.syn.handler;

import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.World;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by kevin  on 16/8/26.
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;
        Header header = message.getHeader();
        if(header != null && header.getType() == MessageType.HEARTBEAT_REQ.value()){
            //心跳消息
            System.out.print("heartbeat message from client: " + World.getInstance().findClientByChannel(ctx.channel()).getWareHouseId());
            //响应client心跳消息
            Message heartBeatResp = buildHeartBeatResp();
            ctx.writeAndFlush(heartBeatResp);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private Message buildHeartBeatResp() {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        //移除client,关闭连接
        Channel channel = ctx.channel();
        World.getInstance().removeClientByChannel(channel);
        ctx.close();
    }

}
