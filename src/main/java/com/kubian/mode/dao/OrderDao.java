package com.kubian.mode.dao;

import com.kubian.mode.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单
 */
@Transactional
@Repository
public interface OrderDao extends CrudRepository<Order, Integer> {

    Order findById(Long id);

    List<Order> findByUserIdAndState(Long userId, Integer state);

    List<Order> findByUserId(Long userId);

    List<Order> findByBid(Long bid);

    Page<Order> findByBid(Long bid, Pageable pageable);

    Page<Order> findByBidAndState(Long bid, Integer state, Pageable pageable);

    @Query("from Order where bid=:bid and orderNumber like :orderNumber")
    List<Order> getOrderByOrderNumber(@Param("orderNumber") String orderNumber, @Param("bid") Long bid, Pageable pageable);

    Page<Order> findByBidAndOrderNumber(Long bid, String orderNumber, Pageable pageable);

    Page<Order> findByUserIdAndState(Long userId, Integer state, Pageable pageable);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByState(Integer state, Pageable pageable);

    Page<Order> findByOrderNumber(String orderNumber, Pageable pageable);

    List<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByOrderNumberAndState(String orderNumber, Integer state, Pageable pageable);
}
