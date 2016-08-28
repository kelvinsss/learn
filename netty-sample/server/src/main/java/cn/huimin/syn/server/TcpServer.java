package cn.huimin.syn.server;

import cn.huimin.syn.codec.MessageDecoder;
import cn.huimin.syn.codec.MessageEncoder;
import cn.huimin.syn.codec.string.StringDecoder;
import cn.huimin.syn.codec.string.StringEncoder;
import cn.huimin.syn.handler.HeartBeatHandler;
import cn.huimin.syn.handler.LoginHandler;
import cn.huimin.syn.handler.ServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty tcp server
 * Created by kevin  on 16/8/26.
 */
public class TcpServer {

    public void start(int port){

        EventLoopGroup  boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    ChannelPipeline pipeline = nioSocketChannel.pipeline();
                    pipeline.addLast("messageDecoder", new MessageDecoder(Integer.MAX_VALUE, 4, 4, new StringDecoder()));
                    pipeline.addLast("messageEncoder", new MessageEncoder(new StringEncoder()));
                    pipeline.addLast("loginHandler", new LoginHandler());
                    pipeline.addLast("heartBeatHandler", new HeartBeatHandler());
                    pipeline.addLast("serviceHandler", new ServiceHandler());
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
