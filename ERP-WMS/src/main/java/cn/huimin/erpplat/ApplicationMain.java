package cn.huimin.erpplat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by guochun on 2016/7/7.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ApplicationMain {

    public static void main(String[] args){
        SpringApplication.run(ApplicationMain.class);
    }
}
