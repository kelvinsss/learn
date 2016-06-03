package com.kevin.test.reflect;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/5/24.
 */
public class TestPrivate {


    public static void main(String[] args)throws Exception{


        TestModel testModel = new TestModel("zhangsan");


        Field field = testModel.getClass().getField("name");

        field.setAccessible(true);

//        field.

    }

}
