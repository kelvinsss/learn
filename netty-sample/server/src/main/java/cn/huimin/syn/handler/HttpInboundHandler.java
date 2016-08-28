package cn.huimin.syn.handler;

import cn.huimin.syn.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;

import java.net.URI;

/**
 * Created by kevin  on 16/8/28.
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("qingqiuaaaaa");

//        if(msg instanceof HttpRequest){
//            System.out.println("request");
//            HttpRequest request = (HttpRequest)msg;
//            String uri = request.uri();
//            URI uri_ = new URI(uri);
//            System.out.println("path" + uri_.getPath());
//        }else if (msg instanceof HttpContent){
//            System.out.println("content");
//            HttpContent content = (HttpContent)msg;
//            System.out.println(content.content());
//        }
//        System.out.println("33333");
//        System.out.print(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        //关闭连接
        ctx.close();
    }
}
