package com.kubian.mode.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Collect;

/**
 * 用户收藏内容 ClassName: CollectDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@Transactional
@Repository
public interface CollectDao extends CrudRepository<Collect, Integer> {
	// 根据用户id和内容id获取数据
	Collect findByAppUserIdAndColumnContentId(Long appUserId, Long columnContentId);
}
