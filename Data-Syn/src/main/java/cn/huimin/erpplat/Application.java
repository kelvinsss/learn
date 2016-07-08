package cn.huimin.erpplat;

import cn.huimin.erpplat.service.IDataSynService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by guochun on 2016/7/8.
 */
public class Application {

    public static void main(String[] args){


        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        IDataSynService dataSynService = (IDataSynService)context.getBean("defaultDataSynService");

    }
}
