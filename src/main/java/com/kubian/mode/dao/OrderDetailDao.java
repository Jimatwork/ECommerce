package com.kubian.mode.dao;

import com.kubian.mode.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单详情
 * @author HD
 * @Title: OrderDetail
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/8/30/03015:03
 */
@Transactional
@Repository
public interface OrderDetailDao extends CrudRepository<OrderDetail, Integer> {
    OrderDetail findById(Long id);

    List<OrderDetail> findByOrderid(Long orderid);
}
