package com.kubian.mode.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Follow;

/**
 * 关注
 * ClassName: FollowDao 
 * @Description: TODO
 * @author HD
 * @date 2018年4月12日
 */
@Transactional
@Repository
public interface FollowDao extends CrudRepository<Follow, Integer> {
	// 根据用户id和文章id获取信息
	public Follow findByUserIdAndConId(Long userId,Long ConId);
}
