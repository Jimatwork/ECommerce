package com.kubian.mode.dao;

import com.kubian.mode.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ShoppingCartDao extends CrudRepository<ShoppingCart, Integer> {

    ShoppingCart findById(Long id);

    ShoppingCart findByUserIdAndPidAndSid(Long userId, Long pid, Long sid);

    @Modifying
    @Transactional
    @Query("delete from ShoppingCart where id in (?1)")
    void deleteBatch(List<Long> ids);

    List<ShoppingCart> findByUserId(Long userId);
}
