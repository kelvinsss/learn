package com.kevin.test.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


/**
 * Created by Administrator on 2016/5/18.
 */
public class ActiveConsumer {


//    private static final String USERNAME = "hmdevelop";//默认连接用户名
//    private static final String PASSWORD = "HM2016@amq";//默认连接密码
//    private static final String BROKEURL = "tcp://182.92.66.103:51551";//默认连接地址


    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL = "tcp://121.42.11.176:61616";//默认连接地址

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接
        Session session;//会话 接受或者发送消息的线程
        Destination destination;//消息的目的地
        MessageConsumer messageConsumer;//消息的消费者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            //创建一个连接HelloWorld的消息队列
                destination = session.createQueue("ERP_FINAL_TOPIC_TEST");
            //创建消息消费者
            messageConsumer = session.createConsumer(destination);//指定selector
            while (true) {
                messageConsumer.setMessageListener(new MessageListener() {
                    public void onMessage(Message message) {
                        System.out.println(message);
                    }
                });//同步的方式消费消息

            }


        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
