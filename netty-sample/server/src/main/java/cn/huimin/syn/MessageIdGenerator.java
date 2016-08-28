package cn.huimin.syn;

import java.util.Date;

/**
 * timestamp 左移12位 + sequence 作为id
 * Created by kevin  on 16/8/28.
 */
public class MessageIdGenerator {

    private long benchmarkTime = 1472367932511l ;//基准时间,某一天开始计时  Sun Aug 28 15:05:32 CST 2016

    private long lastTimestamp = -1;

    private int sequence = 0;

    private int sequenceBits = 12;

    private int sequenceMask = 1 << (sequenceBits + 1) -1;//00000000000000000001111111111111

    private synchronized long generate(){
        long timeStamp = timeGen();//当前时间
        if(timeStamp == lastTimestamp){//和前一次生成id在同一毫秒内
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0){//当前毫秒内可使用的id全部已经被使用
                timeStamp = tilNextMillis(timeStamp);
            }
        }else {
            sequence = 0;
        }
        lastTimestamp = timeStamp;
        return (timeStamp - benchmarkTime) << sequenceBits | sequence;
    }


    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


    /**
     * 单例idgenerator
     */
    private MessageIdGenerator(){

    }

    private static class MessageIdGeneratorHolder{
        private static MessageIdGenerator instance = new MessageIdGenerator();
    }

    private static MessageIdGenerator getInstance(){
        return MessageIdGeneratorHolder.instance;
    }

    public static long nextMessagId(){
        return getInstance().generate();
    }






    public static void main(String[] args){
        System.out.print(new Date().getTime());
        System.out.print(new Date());
    }

}
