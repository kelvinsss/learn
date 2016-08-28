package cn.huimin.syn;

import io.netty.channel.Channel;

/**
 * Created by kevin  on 16/8/28.
 */
public class Client {

    private Channel channel;

    private volatile long lastActiveTime;


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }


    private  Client(){

    }

    public static class ClientHolder{
        private  static  Client instance = new Client();
    }


    public static Client getInstance(){
        return ClientHolder.instance;
    }
}
