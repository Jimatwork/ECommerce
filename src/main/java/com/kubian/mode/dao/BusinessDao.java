package com.kubian.mode.dao;

import com.kubian.mode.Business;
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
public interface BusinessDao extends CrudRepository<Business, Integer> {

    List<Business> findAll();

    @Query(" from Business where name like :name ORDER BY sort desc ")
    List<Business> findByNameLike(Pageable pageable, @Param("name") String name);

    @Query("select count(name) from Business where name like :name")
    Integer findByNameCount(@Param("name") String name);

    Business findById(Long id);

    @Query(" from Business where name like :name and status=:status")
    List<Business> findByNameLike(@Param("name") String name, @Param("status") Integer status);

    @Query(" from Business where name like :name")
    List<Business> findByNameLike(@Param("name") String name);

    @Query(" from Business where name like :name and status=:status and bid=:bid ORDER BY sort desc")
    List<Business> findByNameLike(Pageable pageable, @Param("name") String name, @Param("status") Integer status, @Param("bid") Long bid);

    @Query("select count(name) from Business where name like :name and status=:status and bid=:bid")
    Integer findByNameCount(@Param("name") String name, @Param("status") Integer status, @Param("bid") Long bid);

    @Query("select  s from Business as s where name like :name and status=:status")
    Page<Business> findByNameLikes(Pageable pageable, @Param("name") String name, @Param("status") Integer status);
}
