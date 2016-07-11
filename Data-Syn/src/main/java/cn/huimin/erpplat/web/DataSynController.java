package cn.huimin.erpplat.web;

import cn.huimin.erpplat.constants.TaskSignalConstant;
import cn.huimin.erpplat.constants.TaskStatusConstant;
import cn.huimin.erpplat.utils.LogUtil;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by guochun on 2016/7/11.
 */
@Controller
public class DataSynController {

    @RequestMapping("/")
    public String index(){
        return "index.vm";
    }


    @RequestMapping("/taskStart")
    public String taskStart(@RequestParam(value = "pwd", required = true)String pwd,
                            HttpServletRequest request ){
        LogUtil.debug("pppppppppppwwwwwwwwwwddddddddd:" + pwd );
        if("1234qwer".equals(pwd)){
            TaskSignalConstant.DATA_SYN_ERROR_SIGNAL = TaskStatusConstant.DATA_SYN_ERROR_OPEN;
            TaskSignalConstant.DATA_SYN_SIGNAL = TaskStatusConstant.DATA_SYN_OPEN;
            request.setAttribute("msg", "开启成功");
        }else {
            request.setAttribute("msg", "密码错误");
        }
        return "index.vm";
    }

    @RequestMapping("/taskStop")
    public String taskStop(@RequestParam(value = "pwd", required = true)String pwd,
                           HttpServletRequest request){
        LogUtil.debug("pppppppppppwwwwwwwwwwddddddddd:" + pwd );
        if("1234qwer".equals(pwd)){
            TaskSignalConstant.DATA_SYN_ERROR_SIGNAL = TaskStatusConstant.DATA_SYN_ERROR_CLOSING;
            TaskSignalConstant.DATA_SYN_SIGNAL = TaskStatusConstant.DATA_SYN_CLOSING;
            try {
                Thread.sleep(10000l);
            }catch (Exception e){}
            while (TaskSignalConstant.DATA_SYN_ERROR_SIGNAL != TaskStatusConstant.DATA_SYN_ERROR_CLOSED ||
                    TaskSignalConstant.DATA_SYN_SIGNAL != TaskStatusConstant.DATA_SYN_CLOSED) {
                LogUtil.info("任务关闭中。。。。");
                try {
                    Thread.sleep(5000l);
                } catch (Exception e) {
                }
            }
            request.setAttribute("msg", "关闭成功");
        }else {
            request.setAttribute("msg", "密码错误");
        }
        return "index.vm";
    }

}
