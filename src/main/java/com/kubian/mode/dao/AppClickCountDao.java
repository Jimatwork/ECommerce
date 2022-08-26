package com.kubian.mode.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.AppClickCount;

/**
 * 浏览次数
 * ClassName: AppClickCountDao 
 * @Description: TODO
 * @author HD
 * @date 2018年4月13日
 */
@Transactional
@Repository
public interface AppClickCountDao extends CrudRepository<AppClickCount, Integer> {

}
