package cn.huimin.erpplat.service;

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
	public void addErrorLog(String beanId, String method, Object... params) throws Exception;
	/**
	 * 
	 * @Title: concumeErrorLog 
	 * @Description: 消费错误记录
	 * @param beanId
	 * @param method
	 * @param params
	 */
	public void concumeErrorLog(String beanId, String method, Object... params) throws Exception;
}
