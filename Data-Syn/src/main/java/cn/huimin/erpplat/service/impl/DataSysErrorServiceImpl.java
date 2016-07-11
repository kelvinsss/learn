package cn.huimin.erpplat.service.impl;


import cn.huimin.erpplat.dao.SqliteDao;
import cn.huimin.erpplat.service.IDataSysErrorService;
import cn.huimin.erpplat.utils.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: DataSysErrorServiceImpl 
 * @Description: 处理数据同步错误的服务
 * @author  {ZhangSong} 
 * @date 2016年7月6日 下午4:53:20 
 *  
 */
@Service("defaultDataSynErrorService")
public class DataSysErrorServiceImpl implements IDataSysErrorService {

	@Resource(name = "defaultSqliteDao")
	private SqliteDao sqliteDao;

	@Override
	public void addErrorLog(String beanId, String method, String params){
		try {
			sqliteDao.insertDataDownSynError(beanId, method, params);
		}catch (Exception e){
			LogUtil.error("记录下行失败数据发生异常!", e);
		}
	}

	@Override
	public List<Map<String, Object>> searchErrorLog(){
		return sqliteDao.queryDataDownSynErrRecord();
	}

	@Override
	public void deleteErrorLogById(int id) {
		try {
			sqliteDao.updateFlagById(id);
		}catch (Exception e){
			LogUtil.error("deleteErrorLogById 发生异常！", e);
		}
	}

}
