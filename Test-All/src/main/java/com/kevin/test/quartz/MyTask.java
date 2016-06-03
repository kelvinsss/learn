package com.kevin.test.quartz;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MyTask {

    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());


        System.out.println(new Date(1464159130325l));
    }



    public void execute(){
        System.out.println("开始了。。。");
        try {
            Thread.sleep(10000l);
        }catch (Exception e){

        }

        System.out.println("结束了。。。");
    }


}
