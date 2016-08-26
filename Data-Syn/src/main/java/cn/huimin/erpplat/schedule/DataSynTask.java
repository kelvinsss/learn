package cn.huimin.erpplat.schedule;

import cn.huimin.erpplat.constants.TaskSignalConstant;
import cn.huimin.erpplat.constants.TaskStatusConstant;
import cn.huimin.erpplat.service.IDataSynService;
import cn.huimin.erpplat.service.IDataSysErrorService;
import cn.huimin.erpplat.utils.LogUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by guochun on 2016/7/8.
 */
@Component("dataSynTask")
public class DataSynTask {

    @Resource(name = "defaultDataSynService")
    private IDataSynService dataSynService;

    @Resource(name = "defaultDataSynErrorService")
    private IDataSysErrorService dataSysErrorService;


    public void execute(){

        LogUtil.debug("dataSynTask 开始执行....");
        if(TaskSignalConstant.DATA_SYN_SIGNAL == TaskStatusConstant.DATA_SYN_OPEN){
            //TODO 查待处理订单
//            try {
//                dataSynService.getOutOrderInfo(111);
//            }catch (Exception e){
//                LogUtil.error("数据下行发生异常", e);
//                // 下行失败数据写入sqllite
//                dataSysErrorService.addErrorLog("defaultDataSynService", "getOutOrderInfo", "params");
//            }

        }else {
            TaskSignalConstant.DATA_SYN_SIGNAL = TaskStatusConstant.DATA_SYN_CLOSED;
            LogUtil.info("dataSynTask 停止执行....");

        }
    }

}
