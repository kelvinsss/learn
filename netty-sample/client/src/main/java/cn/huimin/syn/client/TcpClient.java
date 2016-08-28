package cn.huimin.syn.client;

import cn.huimin.syn.handler.ClientHeartBeatHandler;
import cn.huimin.syn.handler.LoginRequestHandler;
import cn.huimin.syn.handler.ClientServiceHandler;
import cn.huimin.syn.codec.MessageDecoder;
import cn.huimin.syn.codec.MessageEncoder;
import cn.huimin.syn.codec.string.StringDecoder;
import cn.huimin.syn.codec.string.StringEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by kevin  on 16/8/28.
 */
public class TcpClient {

    public interface OnMessageReadListener{
        void onMessageRead(Object object);
    }


    public void start(String host, int port, final OnMessageReadListener listener){
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("messageDecoder", new MessageDecoder(Integer.MAX_VALUE, 4, 4, new StringDecoder()));
                    pipeline.addLast("messageEncoder", new MessageEncoder(new StringEncoder()));
                    pipeline.addLast("loginHandler", new LoginRequestHandler());
                    pipeline.addLast("heartBeatHandler", new ClientHeartBeatHandler());
                    pipeline.addLast("serviceHandler", new ClientServiceHandler(listener));
                }
            });

            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }

    }

}
