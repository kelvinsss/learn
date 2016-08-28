package cn.huimin.syn;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存所有连接到server的client
 * Created by kevin  on 16/8/28.
 */
public class World {

    private  ConcurrentHashMap<String, Client>  clients;

    private World(){
        clients = new ConcurrentHashMap<String, Client>();
    }

    public static class WorldHolder{
        private static World instance = new World();
    }

    public static World getInstance(){
        return WorldHolder.instance;
    }

    public void addClient(Client client){
        clients.put(client.getWareHouseId(), client);
    }


    public Client findClientByChannel(Channel channel){
        Collection<Client> clientSet = clients.values();
        if(clientSet == null || clientSet.size() <= 0){
            return null;
        }
        for(Client client : clientSet){
            Channel channel_ = client.getChannel();
            if(channel.equals(channel_)){
                return client;
            }
        }
        return null;
    }


    public void removeClientByChannel(Channel channel){
        Client client = findClientByChannel(channel);
        if(client == null){
            return;
        }
        clients.remove(client);
    }

    public Client findClientByWhId(String whId){
        return clients.get(whId);
    }

}
