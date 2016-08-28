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
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin  on 16/8/28.
 */
public class TcpClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public interface OnMessageReadListener{
        void onMessageRead(Object object);
    }


    public void start(final String host, final int port, final OnMessageReadListener listener){
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ReadTimeoutHandler(20, TimeUnit.SECONDS));
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
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            start(host, port, listener);// 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public static void main(String[] args){
        new TcpClient().start("localhost", 10001, new OnMessageReadListener() {
            @Override
            public void onMessageRead(Object object) {
                System.out.print(object);
            }
        });
    }

}
