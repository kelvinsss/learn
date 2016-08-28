package cn.huimin.syn;

import org.junit.Test;

/**
 * Created by kevin  on 16/8/28.
 */
public class IdgeneratorTest {

    @Test
    public void nextId(){
        for (int j = 0; j < 10000; j++) {
            System.out.println(MessageIdGenerator.nextMessagId());
        }
    }

}
