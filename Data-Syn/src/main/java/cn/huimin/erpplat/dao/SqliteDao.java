package cn.huimin.erpplat.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by guochun on 2016/7/8.
 */
public interface SqliteDao {


    void insertDataDownSynError(String beanId, String methodName, String params);

    List<Map<String, Object>>  queryDataDownSynErrRecord();

    void updateFlagById(int id);


}
