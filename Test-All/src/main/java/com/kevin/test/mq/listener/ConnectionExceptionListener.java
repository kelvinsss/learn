package com.kevin.test.mq.listener;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ConnectionExceptionListener implements ExceptionListener {
    public void onException(JMSException e) {
        
    }
}
