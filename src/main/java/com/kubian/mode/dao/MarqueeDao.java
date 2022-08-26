package com.kubian.mode.dao;

import com.kubian.mode.Marquee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MarqueeDao extends CrudRepository<Marquee, Integer> {

    //查询所有APP滚动字幕
    List<Marquee> findAll();
}
