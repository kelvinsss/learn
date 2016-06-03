package com.kevin.test.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MapTest {


    public static void main(String[] args){

        Map<String, String> map = new HashMap<String, String>();

        String m = map.get("aaa");

        System.out.println(m);

    }
}
