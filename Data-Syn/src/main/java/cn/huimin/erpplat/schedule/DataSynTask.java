package cn.huimin.erpplat.schedule;

import cn.huimin.erpplat.service.IDataSynService;
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


    public void execute(){

        //TODO 查待处理订单
        try {
            dataSynService.getOutOrderInfo(111);
        }catch (Exception e){
            LogUtil.error("数据下行发生异常", e);
            //TODO 写入sqllite
        }

    }

}
