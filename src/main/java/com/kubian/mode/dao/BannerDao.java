package com.kubian.mode.dao;

import com.kubian.mode.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface BannerDao extends CrudRepository<Banner, Integer> {

    //获取所有的banner
    List<Banner> findAll();

    //获取所有的banner(分页)
    Page<Banner> findAll(Pageable pageable);
    
    Banner findById(Long id);
}
