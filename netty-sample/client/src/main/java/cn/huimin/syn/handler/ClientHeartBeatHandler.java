package cn.huimin.syn.handler;

import cn.huimin.syn.Client;
import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin  on 16/8/28.
 */
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
        Message message = (Message) msg;
        Header header = message.getHeader();
        // 握手成功，主动发送心跳消息
        if (header != null&& header.getType() == MessageType.LOGIN_RESP.value()) {
            Client client = Client.getInstance();
            client.setChannel(ctx.channel());
            client.setLastActiveTime(System.currentTimeMillis());

            heartBeat = ctx.executor().scheduleAtFixedRate(new ClientHeartBeatHandler.HeartBeatTask(ctx), 0, 10,TimeUnit.SECONDS);
        } else if (header != null && header.getType() == MessageType.HEARTBEAT_RESP.value()) {
            Client.getInstance().setLastActiveTime(System.currentTimeMillis());
            System.out.println("heartbeat message from server");
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            //十秒内如果client没有收到过server的消息,发送心跳消息
            long lastActiveTime = Client.getInstance().getLastActiveTime();
            long now = System.currentTimeMillis();
            if(now - lastActiveTime >= 10000){
                Message heatBeat = buildHeatBeat();
                ctx.writeAndFlush(heatBeat);
            }
        }

        private Message buildHeatBeat() {
            Message message = new Message();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.close();
    }
}
