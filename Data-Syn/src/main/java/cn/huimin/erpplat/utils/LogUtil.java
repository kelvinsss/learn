package cn.huimin.erpplat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guochun on 2016/6/8.
 */
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger("CommonLog");

    public static void info(String message){
        logger.info(message);
    }
    public static void info(String format, Object...arguments){
        logger.info(format,arguments);
    }
    public static void info(String message, Throwable t){
        logger.info(message, t);
    }

    public static void debug(String message){
        if(logger.isDebugEnabled()){
            logger.debug(message);
        }
    }
    public static void debug(String format, Object...arguments){
        if(logger.isDebugEnabled()){
            logger.debug(format,arguments);
        }
    }
    public static void debug(String message, Throwable t){
        if(logger.isDebugEnabled()){
            logger.debug(message, t);
        }
    }

    public static void error(String message){
        logger.error(message);
    }
    public static void error(String format, Object...arguments){
        logger.error(format,arguments);
    }
    public static void error(String message, Throwable t){
        logger.error(message, t);
    }




    private static Logger updataLogger = LoggerFactory.getLogger("UPLOAD_DATA");

    public static void updataError(String message){
        updataLogger.error(message);
    }
    public static void updataError(String format, Object...arguments){
        updataLogger.error(format,arguments);
    }

}
