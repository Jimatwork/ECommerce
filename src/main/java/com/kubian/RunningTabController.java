package com.kubian;

import com.kubian.mode.RunningTab;
import com.kubian.mode.dao.RunningTabDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易明细操作
 * @author HD
 * @Title: RunningTabController
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/7/00714:55
 */
@RestController
public class RunningTabController {
    @Autowired
    private RunningTabDao runningTabDao;

    /**
     * 根据商铺id分页获取交易明细
     * @param bid
     * @return
     */
    @RequestMapping(value = "/getRunningTabByBid")
    public Object getRunningTabByBid(Long bid , Integer page , Integer size , Integer state){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.DESC,"createDate");
            PageRequest pageRequest = new PageRequest(page , size , sort);
            Page<RunningTab> runningTabs = null;

            if (!MyTools.isEmpty(bid)) {
                // 商铺id不为空根据商铺id查询

                if (!MyTools.isEmpty(state)) {
                    //state 不为空 根据状态获取
                    runningTabs = runningTabDao.findByBidAndState(bid , state , pageRequest);
                } else {
                    runningTabs = runningTabDao.findByBid(bid ,pageRequest );
                }
            } else {
                // 商铺id为空查询所有

                if (!MyTools.isEmpty(state)) {
                    //state 不为空 根据状态获取
                    runningTabs = runningTabDao.findByState(state , pageRequest);
                } else {
                    runningTabs = runningTabDao.findAll(pageRequest);
                }
            }
            if (!MyTools.isEmpty(returnMsg)) {
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(runningTabs.getTotalElements());
                returnMsg.setMsg("获取成功！");
                returnMsg.setList(runningTabs.getContent());
            } else {
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(0);
                returnMsg.setMsg("没有数据！");
                returnMsg.setList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }

        return returnMsg;
    }
}
