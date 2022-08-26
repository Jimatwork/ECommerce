package com.kubian.mode.dao;

import com.kubian.mode.ReceivingAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ReceivingAddressDao extends CrudRepository<ReceivingAddress, Integer> {

    ReceivingAddress findById(Long id);

    List<ReceivingAddress> findByAppuserId(Long appUserId);

    Page<ReceivingAddress> findAll(Pageable pageable);

    @Query("select s from ReceivingAddress as s where appuserId=:appuserId and isDefault=1")
    ReceivingAddress findDefaultAddress(@Param("appuserId") long appuserId);
}
