package com.kevin.test.json;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2016/6/1.
 */
public class TestFastJson {

    public static void main(String[] args){
        Model model = new Model("2016-05-11 00:00:00","2016-05-11 00:00:00");

        System.out.println(JSON.toJSON(model));



    }

}
