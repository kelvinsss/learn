package cn.huimin.erpplat.dao.impl;

import cn.huimin.erpplat.dao.SqliteDao;
import cn.huimin.erpplat.utils.LogUtil;
import cn.huimin.erpplat.utils.SQLiteUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guochun on 2016/7/8.
 */
public class SqliteDaoImpl implements SqliteDao {


    @Override
    public void insertDataDownSynError(String beanId, String methodName, String params) {
        String sqlPattern = "INSERT INTO data_down_syn_error (bean_id, method_name, params) values ({0}, {1},{2})";
        String sql = MessageFormat.format(sqlPattern, beanId, methodName, params);
        SQLiteUtils.execute(sql);
    }

    @Override
    public List<Map<String, Object>> queryDataDownSynErrRecord() {
        List<Map<String, Object>>  result = new ArrayList<>();
        Connection conn = null;
        String sql = "select id, bean_id, method_name, params, flag  from  data_down_syn_error  where flag = 0";
        try{
            conn = SQLiteUtils.getConn();
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            LogUtil.debug("execute sql [" + sql + "]");
            while(rs.next()){
                Map<String, Object> column = new HashMap<String, Object>();
                column.put("id", rs.getInt("id"));
                column.put("method_name", rs.getString("method_name"));
                column.put("params", rs.getString("params"));
                column.put("flag", rs.getInt("flag"));
                result.add(column);
            }
            conn.commit();
        }catch(Exception e){
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    LogUtil.error("回滚事务失败", e1);
                }
            }
            LogUtil.error("数据查询失败[" + sql + "]", e);
        }finally{
            if(conn != null){
                SQLiteUtils.close(conn);
            }
        }
        return result;
    }

    @Override
    public void updateFlagById(int id) {
        String sqlPattern = "update data_down_syn_error set flag = -1 where id = {0}";
        String sql = MessageFormat.format(sqlPattern, id);
        SQLiteUtils.execute(sql);
    }
}

