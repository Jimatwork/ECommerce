package com.kubian;

import com.kubian.mode.*;
import com.kubian.mode.dao.*;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单操作
 *
 * @author HD
 * @Title: OrderController
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/8/30/03015:14
 */
@RestController
public class OrderController {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SpecsDao specsDao;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private AppUserDao appUserDao;

    /**
     * 分页获取商铺的订单
     *
     * @param bid         商铺id
     * @param page
     * @param size
     * @param state       状态 0:未付款 1:待发货 2:待收货 3:待评价
     * @param orderNumber 订单编号
     * @return
     */
    @RequestMapping(value = "/getOrderByBid")
    public Object getOrderByBid(Long bid, Integer state, Integer page, Integer size, String orderNumber) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            PageRequest pageable = new PageRequest(page, size, sort);
            if (!MyTools.isEmpty(orderNumber)) {
                // 订单编号不为空 根据订单编号查询
                Page<Order> order = orderDao.findByBidAndOrderNumber(bid, orderNumber, pageable);
                if (!MyTools.isEmpty(order)) {
                    // 查询对应订单的详情
                    for (Order order12 : order.getContent()) {
                        List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order12.getId());
                        if (null != orderDetails) {
                            for (OrderDetail orderDatai2 : orderDetails) {
                                if (!MyTools.isEmpty(orderDatai2.getPid())) {
                                    // 获取对应的商品信息
                                    Product product = productDao.findById(orderDatai2.getPid());
                                    orderDatai2.setProduct(product);
                                }
                                if (!MyTools.isEmpty(orderDatai2.getSid())) {
                                    // 获取对应的规格的信息
                                    Specs specs = specsDao.findById(orderDatai2.getSid());
                                    orderDatai2.setSpecs(specs);
                                }
                            }
                            order12.setOrderDetailList(orderDetails);
                        }
                        // 查询对应的商铺的信息
                        Business business = businessDao.findById(order12.getBid());
                        if (!MyTools.isEmpty(business)) order12.setBusiness(business);
                    }

                    returnMsg.setList(order.getContent());
                    returnMsg.setMsg("获取成功！");
                    returnMsg.setSuccess(true);
                    returnMsg.setTotalSize(order.getTotalElements());
                    return returnMsg;
                }
            }
            Page<Order> orders = null;
            if (state == null || state == -1) {
                // state等于-1 根据状态获取对应订单信息
                orders = orderDao.findByBid(bid, pageable);

            } else {
                orders = orderDao.findByBidAndState(bid, state, pageable);

            }

            if (null != orders) {
                for (Order order : orders.getContent()) {
                    // 获取订单的详情信息
                    List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order.getId());
                    if (null != orderDetails) {
                        for (OrderDetail orderDatai2 : orderDetails) {
                            if (!MyTools.isEmpty(orderDatai2.getPid())) {
                                // 获取对应的商品信息
                                Product product = productDao.findById(orderDatai2.getPid());
                                orderDatai2.setProduct(product);
                            }
                            if (!MyTools.isEmpty(orderDatai2.getSid())) {
                                // 获取对应的规格的信息
                                Specs specs = specsDao.findById(orderDatai2.getSid());
                                orderDatai2.setSpecs(specs);
                            }
                        }
                        order.setOrderDetailList(orderDetails);

                    }
                }
                returnMsg.setList(orders.getContent());
                returnMsg.setSuccess(true);
                returnMsg.setMsg("获取成功！");
                returnMsg.setTotalSize(orders.getTotalElements());
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setList(null);
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
            returnMsg.setTotalSize(0l);
        }
        return returnMsg;
    }

    /**
     * 根据订单编号和商铺id查询订单
     *
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/getOrderByOrderNumber")
    public Object getOrderByOrderNumber(String orderNumber, Long bid, Integer page, Integer size) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            PageRequest pageable = new PageRequest(page, size, sort);
            Page<Order> order = orderDao.findByBidAndOrderNumber(bid, orderNumber, pageable);
            List<Order> list = new ArrayList<>();
            if (!MyTools.isEmpty(order)) {
                // 查询对应订单的详情
                for (Order order12 : order.getContent()) {
                    List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order12.getId());
                    if (null != orderDetails) {
                        for ( OrderDetail orderDetail:orderDetails) {
                            if (!MyTools.isEmpty(orderDetail.getPid())) {
                                // 获取对应的商品信息
                                Product product = productDao.findById(orderDetail.getPid());
                                orderDetail.setProduct(product);
                            }
                            if (!MyTools.isEmpty(orderDetail.getSid())) {
                                // 获取对应的规格的信息
                                Specs specs = specsDao.findById(orderDetail.getSid());
                                orderDetail.setSpecs(specs);
                            }
                        }
                        order12.setOrderDetailList(orderDetails);
                    }
                    // 查询对应的商铺的信息
                    Business business = businessDao.findById(order12.getBid());
                    if (!MyTools.isEmpty(business)) {
                        order12.setBusiness(business);
                    }
                }
                returnMsg.setList(order.getContent());
                returnMsg.setMsg("获取成功！");
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(order.getTotalElements());
            } else {
                returnMsg.setList(null);
                returnMsg.setMsg("没有这条数据！");
                returnMsg.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setList(null);
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 添加修改订单
     *
     * @param order
     * @return
     */
    @RequestMapping(value = "/addOrder")
    public Object addOrder(Order order) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (!MyTools.isEmpty(order.getId())) {
                // id不为空是修改
                Order order1 = orderDao.findById(order.getId());
                if (!MyTools.isEmpty(order1)) {
                    try {
                        MyTools.updateNotNullFieldForPatient(order1, order);
                        orderDao.save(order1);
                        returnMsg.setSuccess(true);
                        returnMsg.setMsg("修改成功！");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // id为空是添加
                orderDao.save(order);
                returnMsg.setSuccess(true);
                returnMsg.setMsg("添加成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 根据id删除对应的订单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteOrder")
    public Object deleteOrder(Long id) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Order order = orderDao.findById(id);
            if (!MyTools.isEmpty(order)) {
                orderDao.delete(order);
                // 查询对应订单的详情
                List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order.getId());
                if (!MyTools.isEmpty(orderDetails)) {
                    // 删除对应的订单详情
                    for (OrderDetail orderDetail : orderDetails) {
                        orderDetailDao.delete(orderDetail);
                    }
                }
                returnMsg.setMsg("删除成功！");
                returnMsg.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 根据用户id和状态分页获取订单
     *
     * @param userId
     * @param state
     * @return
     */
    @RequestMapping(value = "/getOrderByUserId")
    public Object getOrderByUserId(Long userId, Integer state, Integer page, Integer size) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            PageRequest pageRequest = new PageRequest(page, size, sort);
            Page<Order> orders = null;
            if (state == null || state == -1) {
                // state等于-1 根据用户id和订单状态获取订单信息
                orders = orderDao.findByUserId(userId, pageRequest);

            } else {
                orders = orderDao.findByUserIdAndState(userId, state, pageRequest);
            }
            if (null != orders && !MyTools.isEmpty(orders.getContent())) {
                for (Order order : orders.getContent()) {
                    // 查询对应订单的详情
                    List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order.getId());
                    if (null != orderDetails) {
                        for (OrderDetail orderDatai2 : orderDetails) {
                            if (!MyTools.isEmpty(orderDatai2.getPid())) {
                                // 获取对应的商品信息
                                Product product = productDao.findById(orderDatai2.getPid());
                                orderDatai2.setProduct(product);
                            }
                            if (!MyTools.isEmpty(orderDatai2.getSid())) {
                                // 获取对应的规格的信息
                                Specs specs = specsDao.findById(orderDatai2.getSid());
                                orderDatai2.setSpecs(specs);
                            }
                        }
                        order.setOrderDetailList(orderDetails);
                    }
                    // 查询对应的商铺的信息
                    Business business = businessDao.findById(order.getBid());
                    if (!MyTools.isEmpty(business)) {
                        order.setBusiness(business);
                    }
                }
                returnMsg.setSuccess(true);
                returnMsg.setMsg("获取成功！");
                returnMsg.setTotalSize(orders.getTotalElements());
                returnMsg.setList(orders.getContent());
            } else {
                returnMsg.setSuccess(true);
                returnMsg.setMsg("没有数据！");
                returnMsg.setTotalSize(0);
                returnMsg.setList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 根据状态分页获取所有的订单
     *
     * @param page
     * @param limit
     * @param state       状态
     * @param orderNumber 订单编号
     * @return
     */
    @RequestMapping(value = "/getOrderAll")
    public Object getOrderAll(Integer page, Integer limit, Integer state, String orderNumber) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (state != null) {
                state = state == -1 ? null : state;
            }
            if (orderNumber != null) {
                orderNumber = orderNumber == "" ? null : orderNumber;
            }
            System.out.println(state+"____"+orderNumber);
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            PageRequest pageRequest = new PageRequest(page - 1, limit, sort);
            Page<Order> orders = null;
            if (orderNumber == null && state == null) {
                orders = orderDao.findAll(pageRequest);
            }
            if (state != null) {
                orders = orderDao.findByState(state, pageRequest);
            }
            if (orderNumber != null) {
                orders = orderDao.findByOrderNumber(orderNumber, pageRequest);
            }
            if (orderNumber != null && state != null) {
                orders = orderDao.findByOrderNumberAndState(orderNumber, state, pageRequest);
            }
            if (!MyTools.isEmpty(orders)) {
                for (Order order : orders.getContent()) {
                    order.setBusiness(businessDao.findById(order.getBid()));
                    order.setAppUser(appUserDao.findById(order.getUserId()));
                    // 查询对应订单的详情
//                    List<OrderDetail> orderDetails = orderDetailDao.findByOrderid(order.getId());
//                    if (null != orderDetails) {
//                        order.setOrderDetailList(orderDetails);
//                    }
                }
                returnMsg.setList(orders.getContent());
                returnMsg.setTotalSize(orders.getTotalElements());
                returnMsg.setMsg("获取成功！");
                returnMsg.setSuccess(true);
            } else {
                returnMsg.setMsg("没有数据！");
                returnMsg.setSuccess(true);
                returnMsg.setList(null);
                returnMsg.setTotalSize(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }
}
