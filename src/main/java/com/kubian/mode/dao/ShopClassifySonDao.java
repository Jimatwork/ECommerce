package com.kubian.mode.dao;

import com.kubian.mode.ShopClassifySon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品二级分类
 * @author HD
 * @Title: ShopClassifySonDao
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/10/16/01615:48
 */
@Transactional
@Repository
public interface ShopClassifySonDao extends CrudRepository<ShopClassifySon , Integer> {
    Page<ShopClassifySon> findBySId(Long sId , Pageable pageable);

    Page<ShopClassifySon> findBySIdAndState(Long sId , Integer state , Pageable pageable);

    List<ShopClassifySon> findBySId(Long sId);

    List<ShopClassifySon> findBySIdAndState(Long sId , Integer state);

    ShopClassifySon findById(Long id);

}
