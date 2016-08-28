package cn.huimin.syn.handler;

import cn.huimin.syn.client.TcpClient;
import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by kevin  on 16/8/28.
 */
public class ClientServiceHandler extends ChannelInboundHandlerAdapter {

    private TcpClient.OnMessageReadListener listener;

    public ClientServiceHandler(TcpClient.OnMessageReadListener listener){
        this.listener = listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
        Message message = (Message) msg;
        Header header = message.getHeader();
        if(header != null && header.getType() == MessageType.SERVICE_REQ.value()){
            //service message
            long messageId = header.getMessageId();
            Object body = message.getBody();
            //callback方法处理业务
            listener.onMessageRead(body);
            //响应客户端success
            Message messageResp = bulidMessageResp(messageId);
            ctx.writeAndFlush(messageResp);
        }
    }

    private Message bulidMessageResp(long messageId) {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.SERVICE_RESP.value());
        header.setMessageId(messageId);
        message.setHeader(header);
        return message;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
