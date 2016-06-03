package com.kevin.test.quartz;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ApplicationContext {

    public static void main(String[]  args){

        org.springframework.context.ApplicationContext  applicationContext = new ClassPathXmlApplicationContext("spring-all.xml");

    }

}
