package cn.huimin.erpplat.schedule;

import cn.huimin.erpplat.service.IDataSynService;
import cn.huimin.erpplat.service.IDataSysErrorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin  on 16/7/9.
 */
@Service("dealSynErrorTask")
public class DealSynErrorTask {


    @Resource(name = "defaultDataSynErrorService")
    private IDataSysErrorService  dataSysErrorService;

    @Resource(name = "defaultDataSynService")
    private IDataSynService dataSynService;

    public void execute(){

        List<Map<String, Object>> errorRecords = dataSysErrorService.searchErrorLog();

        if(errorRecords == null){
            return;
        }

        for (Map<String, Object>  record : errorRecords){
            Integer id = (Integer)record.get("id");
            String beanId = record.get("bean_id").toString();
            String methodName = record.get("method_name").toString();
            String params = record.get("params").toString();
            //TODO 添加不同错误的处理
            dataSynService.getOutOrderInfo(Integer.parseInt(params));
            dataSysErrorService.deleteErrorLogById(id);
        }


    }
}
