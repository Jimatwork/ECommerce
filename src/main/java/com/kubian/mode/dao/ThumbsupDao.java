package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Thumbsup;

/**
 * 点赞
 * ClassName: ThumbsupDao 
 * @Description: TODO
 * @author HD
 * @date 2018年5月2日
 */
@Transactional
@Repository
public interface ThumbsupDao extends CrudRepository<Thumbsup, Integer> {
	// 根据用户id和内容id获取数据
	List<Thumbsup> findByAppUserIdAndColumnContentId(Long appUserId, Long columnContentId);
	// 根据用户id和评论id获取数据
	Thumbsup findByAppUserIdAndCommentId(Long appUserId, Long commentId);
}
