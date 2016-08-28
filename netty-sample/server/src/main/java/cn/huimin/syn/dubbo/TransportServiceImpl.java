package cn.huimin.syn.dubbo;

import cn.huimin.syn.Client;
import cn.huimin.syn.constants.MessageType;
import cn.huimin.syn.World;
import cn.huimin.syn.struct.Header;
import cn.huimin.syn.struct.Message;
import cn.huimin.syn.service.TransportService;

/**
 * Created by kevin  on 16/8/28.
 */
public class TransportServiceImpl implements TransportService {

    public void transportMessage(String whId, String message) {
        Message message_ = new Message();
        Header header = new Header();
        header.setType(MessageType.SERVICE_REQ.value());
        message_.setHeader(header);
        message_.setBody(message);
        Client client = World.getInstance().findClientByWhId(whId);
        if(client == null){
            //TODO whId对应仓库的client不在线
        }else {
            //客户端在线发送消息
            client.getChannel().writeAndFlush(message_);
        }
    }
}
