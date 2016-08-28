package cn.huimin.syn.struct;

/**
 * 自定义消息结构
 * Created by kevin  on 16/8/26.
 */
public class Message {

    private Header header;

    private Object body;


    public final Header getHeader() {
        return header;
    }


    public final void setHeader(Header header) {
        this.header = header;
    }


    public final Object getBody() {
        return body;
    }


    public final void setBody(Object body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "NettyMessage [header=" + header + "]";
    }
}
