package cn.huimin.erpplat.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IDataSysErrorService 
 * @Description: 处理数据同步错误的服务
 * @author  {ZhangSong} 
 * @date 2016年7月6日 下午4:53:24 
 *  
 */
public interface IDataSysErrorService {
	/**
	 * 
	 * @Title: addErrorLog 
	 * @Description: 添加错误记录
	 * @param beanId
	 * @param method
	 * @param params
	 */
	void addErrorLog(String beanId, String method, String params);
	/**
	 * 
	 * @Title: concumeErrorLog 
	 * @Description: 消费错误记录
	 */
	List<Map<String, Object>> searchErrorLog();


	void deleteErrorLogById(int id);
}
