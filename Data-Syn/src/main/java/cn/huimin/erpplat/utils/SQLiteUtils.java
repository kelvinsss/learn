package cn.huimin.erpplat.utils;

import org.apache.commons.logging.Log;
import org.light.complet.model.sql.TSQL;
import org.light.data.base.SQLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/** 
 * @ClassName: SQLiteUtils 
 * @Description: SQLite的工具类,暂时只支持记录日志
 * @author  {ZhangSong} 
 * @date 2016年5月29日 上午11:52:54 
 *  
 */
public class SQLiteUtils {
	
	private static Logger log = LoggerFactory.getLogger(SQLiteUtils.class);
	/**
	 * 初始化的数据库链接
	 */
	private static final ConcurrentLinkedQueue<Connection> connections = new ConcurrentLinkedQueue<Connection>();
	/**
	 * 数据库文件位置
	 */
	private static String DB_URL = null;
	static{
		URL resource = SQLiteUtils.class.getClassLoader().getResource("dd.db");
		DB_URL = resource.getPath();
	}
	
	/**
	 * 初始化链接数量
	 */
	private static final int INIT = 5;
	
	/**
	 * 
	 * @Title: initConnection 
	 * @Description: 初始化链接
	 */
	public static void initConnection(){
		try{
			if(log.isDebugEnabled()){
				log.debug("init sqlite conn, size:" + INIT);
			}
			for(int i = 0 ; i < INIT; i++){
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_URL);
				conn.setAutoCommit(false);
				connections.add(conn);
			}
		}catch(Exception e){
			log.error("初始化链接", e);
		}
	}
	/**
	 * 
	 * @Title: getConn 
	 * @Description: 获取数据库链接
	 * @return
	 * @return Connection 返回类型
	 */
	public static Connection getConn(){
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_URL);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 
	 * @Title: destory 
	 * @Description: 关闭数据库链接
	 * @param conn 
	 */
	public static void close(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public static void execute(String sql){
		Connection conn = null;
		try{
			conn = SQLiteUtils.getConn();
			LogUtil.debug("execute sql [" + sql + "]");
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.execute();
			conn.commit();
		}catch(Exception e){
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LogUtil.error("回滚事务失败", e1);
				}
			}
			LogUtil.error("写log到db[" + sql + "]", e);
		}finally{
			if(conn != null){
				SQLiteUtils.close(conn);
			}
		}
	}
}
