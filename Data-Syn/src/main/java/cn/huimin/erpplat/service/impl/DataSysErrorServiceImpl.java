package cn.huimin.erpplat.service.impl;


import cn.huimin.erpplat.service.IDataSysErrorService;
import org.springframework.stereotype.Service;

/** 
 * @ClassName: DataSysErrorServiceImpl 
 * @Description: 处理数据同步错误的服务
 * @author  {ZhangSong} 
 * @date 2016年7月6日 下午4:53:20 
 *  
 */
@Service("defaultDataSynErrorService")
public class DataSysErrorServiceImpl implements IDataSysErrorService {

	@Override
	public void addErrorLog(String beanId, String method, Object... params) throws Exception{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void concumeErrorLog(String beanId, String method, Object... params) throws Exception{
		
//		Object bean = AppUtil.getBean(beanId);
//		Class<?> clazz = bean.getClass();
//		Class<?>[] types = new Class[params.length];
//		for(int i = 0; i < params.length; i++){
//			Object obj = params[i];
//			types[i] = obj.getClass();
//		}
//		Method invokeMethod = clazz.getMethod(method, types);
//		Object result = invokeMethod.invoke(bean, params);
//		if(Log4jUtil.CommonLog.isInfoEnabled()){
//			Log4jUtil.CommonLog.info("concumeErrorLog : [beanId:" + beanId + ",method:" + method + ",params:" + JsonUtil.toJson(params) + ",result:" + result + "]");
//		}
	}

}
