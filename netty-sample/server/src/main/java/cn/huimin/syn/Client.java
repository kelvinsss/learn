package cn.huimin.syn;


import io.netty.channel.Channel;

/**
 * Created by kevin  on 16/8/28.
 */
public class Client {

    private String  wareHouseId;

    private Channel channel;

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
