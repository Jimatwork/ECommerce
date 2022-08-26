package com.kubian.mode.dao;

import com.kubian.mode.ShopClassify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品一级分类
 * @author HD
 * @Title: ShopClassifyDao
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/10/16/01615:41
 */
@Repository
@Transactional
public interface ShopClassifyDao extends CrudRepository<ShopClassify , Integer> {
    Page<ShopClassify> findAll(Pageable pageable);

    List<ShopClassify> findAll();

    Page<ShopClassify> findByState (Integer state , Pageable pageable);

    ShopClassify findById(Long id);
}
