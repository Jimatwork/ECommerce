package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Attention;

/**
 * 用户关注用户 ClassName: AttentionDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@Transactional
@Repository
public interface AttentionDao extends CrudRepository<Attention, Integer> {
	// 根据关注人id和被关注人id获取数据
	List<Attention> findByFollowerIdAndBeFollowedId(Long followerId, Long beFollowedId);

	// 根据关注人id和被关注人id和状态judge获取数据
	Attention findByFollowerIdAndBeFollowedIdAndJudge(Long followerId, Long beFollowedId, Integer judge);

	// 获取关注我的人的信息
	List<Attention> findByBeFollowedIdAndJudge(Long beFollowedId, Integer judge);
}
