package com.kubian.mode.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kubian.mode.Recommend;

/**
 * 推荐表操作 ClassName: RecommendDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月22日
 */
@Transactional
@Repository
public interface RecommendDao extends CrudRepository<Recommend, Integer> {
	Recommend findById(Long id);
	
	List<Recommend> findByAppUserId(Long appUserId);
}
