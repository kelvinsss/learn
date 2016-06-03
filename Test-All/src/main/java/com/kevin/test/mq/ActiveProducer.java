package com.kevin.test.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ActiveProducer {

//    private static final String USERNAME = "hmdevelop";//默认连接用户名
//    private static final String PASSWORD = "HM2016@amq";//默认连接密码
//    private static final String BROKEURL = "tcp://182.92.66.103:51551";//默认连接地址

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址
    //发送的消息数量
    private static final int SENDNUM = 10;

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection = null;
        //会话 接受或者发送消息的线程
        Session session;
        //消息的目的地
        Destination destination;
        //消息生产者
        MessageProducer messageProducer;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

            //创建一个名称为HelloWorld的消息队列
            destination = session.createQueue("Hello world");
            //创建消息生产者
            messageProducer = session.createProducer(destination);
//            messageProducer.setTimeToLive(0l);
            //发送消息
            sendMessage(session, messageProducer);

//            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * 发送消息
     * @param session
     * @param messageProducer  消息生产者
     * @throws Exception
     */
    public static void sendMessage(Session session,MessageProducer messageProducer) throws Exception{

        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//		messageProducer.setTimeToLive(arg0);
        for (int i = 0; i < SENDNUM; i++) {
            //创建一条文本消息
            TextMessage message = session.createTextMessage("ActiveMQ 发送消息" +i );
//            message.setStringProperty("reciver", "A");//设置自定义属性  reciver 为 A

            System.out.println("发送消息：Activemq 发送消息" + i + "messageid:" + message.getJMSMessageID());
            //通过消息生产者发出消息
            messageProducer.send(message);
        }

    }



}
