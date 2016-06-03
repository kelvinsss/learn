package com.kevin.test.string;

/**
 * Created by Administrator on 2016/5/28.
 */
public class TestNull {

    public static void main(String[] args){


        StringBuilder sb = new StringBuilder("empty = ");
        String empty = null;

        sb.append(empty);
        System.out.println(sb.toString());

    }

}
