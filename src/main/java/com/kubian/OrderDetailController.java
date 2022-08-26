package com.kubian;

import com.kubian.mode.OrderDetail;
import com.kubian.mode.Product;
import com.kubian.mode.Specs;
import com.kubian.mode.dao.OrderDetailDao;
import com.kubian.mode.dao.ProductDao;
import com.kubian.mode.dao.SpecsDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单详情操作
 * @author HD
 * @Title: OrderDetailController
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/8/30/03015:41
 */
@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SpecsDao specsDao;
    /**
     * 根据订单id获取订单详情
     * @param orderid 订单id
     * @return
     */
    @RequestMapping(value = "/getOrderBetailByOederId")
    public Object getOrderBetailByOederId(Long orderid){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(orderid);
            if (!MyTools.isEmpty(orderDetails)) {
                for (OrderDetail orderDetail:orderDetails) {
                    if (orderDetail.getPid() != null) {
                        // 获取对应的商品信息
                        Product product = productDao.findById(orderDetail.getPid());
                        orderDetail.setProduct(product);
                    }
                    if (orderDetail.getSid() != null) {
                        // 获取对应的规格的信息
                        Specs specs = specsDao.findById(orderDetail.getSid());
                        orderDetail.setSpecs(specs);
                    }
                }
                returnMsg.setSuccess(true);
                returnMsg.setMsg("操作成功！");
                returnMsg.setList(orderDetails);
                returnMsg.setTotalSize(orderDetails.size());
            } else {
                returnMsg.setSuccess(true);
                returnMsg.setMsg("没有数据！");
                returnMsg.setList(null);
                returnMsg.setTotalSize(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
            returnMsg.setList(null);
            returnMsg.setTotalSize(0);
        }
        return returnMsg;
    }

    /**
     * 添加修改订单详情
     * @param orderDetail
     * @return
     */
    @RequestMapping(value = "/updateOrderBetail")
    public Object updateOrderBetail(OrderDetail orderDetail , String shop_name , String shop_specs){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (MyTools.isEmpty(orderDetail.getId())) {
                // id 为空是添加
                orderDetailDao.save(orderDetail);
                returnMsg.setMsg("添加成功！");
                returnMsg.setSuccess(true);
            } else {
                // id不为空是修改
                OrderDetail orderDetail2 = orderDetailDao.findById(orderDetail.getId());
                if (!MyTools.isEmpty(orderDetail2)) {
                    try {
                        MyTools.updateNotNullFieldForPatient(orderDetail2 , orderDetail);
                        // 获取对应的商品信息
                        Product product = productDao.findById(orderDetail2.getPid());
                        if (!MyTools.isEmpty(product) && !MyTools.isEmpty(shop_name)) {
                            if (!shop_name.equals(product.getName())) {
                                // 输入的商品名称和原商品名称不一致 修改原商品名称
                                product.setName(shop_name);
                                productDao.save(product);
                            }
                        }
                        // 获取对应的规格的信息
                        Specs specs = specsDao.findById(orderDetail2.getSid());
                        if (!MyTools.isEmpty(specs) && !MyTools.isEmpty(shop_specs)) {
                            if (!shop_specs.equals(specs.getSpecsName())) {
                                // 输入的商品名称和原商品名称不一致 修改原商品名称
                                specs.setSpecsName(shop_specs);
                                specsDao.save(specs);
                            }
                        }
                        orderDetailDao.save(orderDetail2);
                        returnMsg.setMsg("修改成功！");
                        returnMsg.setSuccess(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        returnMsg.setMsg("修改失败！");
                        returnMsg.setSuccess(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 根据id删除对应的订单详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/delOrderBetail")
    public Object delOrderBetail(Long id){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            OrderDetail orderDetail = orderDetailDao.findById(id);
            if (!MyTools.isEmpty(orderDetail)) {
                orderDetailDao.delete(orderDetail);
                returnMsg.setSuccess(true);
                returnMsg.setMsg("删除成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }
}
