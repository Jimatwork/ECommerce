package com.kubian;

import com.alibaba.fastjson.JSON;
import com.kubian.mode.ReceivingAddress;
import com.kubian.mode.dao.ReceivingAddressDao;
import com.kubian.mode.util.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:ReceivingAddressController
 *
 * @author WangW
 * @Description: 商品管理
 * @date 2018年8月28日
 */
@RestController
public class ReceivingAddressController {

    @Autowired
    private ReceivingAddressDao receivingAddressDao;

    private static final Logger log = LoggerFactory.getLogger(ReceivingAddressController.class);

    /**
     * (增加/修改)收货地址
     *
     * @param receivingAddress
     * @return Object
     */
    @RequestMapping(value = "/saveAddress")
    @ResponseBody
    public Object saveAddress(ReceivingAddress receivingAddress) {
        try {
            System.out.println(JSON.toJSONString(receivingAddress));
            //添加
            if (receivingAddress.getIsDefault() == 1) {
                List<ReceivingAddress> receivingAddressList = receivingAddressDao.findByAppuserId(receivingAddress.getAppuserId());
                for (ReceivingAddress address : receivingAddressList) {
                    address.setIsDefault(2);
                }
                receivingAddressDao.save(receivingAddressList);
            }
            receivingAddressDao.save(receivingAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/delAddress")
    @ResponseBody
    public Object delAddress(Long id) {
        try {
            receivingAddressDao.delete(receivingAddressDao.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 根据用户ID查询地址列表
     *
     * @param appuserId
     * @return Object
     */
    @RequestMapping(value = "/getAddress")
    @ResponseBody
    public Object getAddress(Long appuserId) {
        List<ReceivingAddress> receivingAddress = receivingAddressDao.findByAppuserId(appuserId);
        if (receivingAddress.size() > 0) {
            return ResultVO.custom(0, "操作成功", receivingAddress, receivingAddress.size());
        } else {
            return ResultVO.custom(1, "无数据", receivingAddress, receivingAddress.size());
        }
    }

    /**
     * 设置默认地址
     *
     * @param appuserId
     * @return Object
     */
    /*
    @RequestMapping(value = "/setDefaultAddress")
    @ResponseBody
    public Object setDefaultAddress(Long appuserId, Long addressid) {
        List<ReceivingAddress> receivingAddressList = receivingAddressDao.findByAppuserId(appuserId);
        for (ReceivingAddress receivingAddress : receivingAddressList) {
            receivingAddress.setDefault(false);
        }
        receivingAddressDao.save(receivingAddressList);
        ReceivingAddress receivingAddress = receivingAddressDao.findById(addressid);
        receivingAddress.setDefault(true);
        receivingAddressDao.save(receivingAddress);
        return receivingAddressDao.findByAppuserId(appuserId);
    }
    */
}
