package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Comment;

/**
 * 评论 ClassName: CommentDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月11日
 */
@Transactional
@Repository
public interface CommentDao extends CrudRepository<Comment, Integer> {
	// 根据内容id分页获取评论内容
	@Query("from Comment where (state = 0 or state =:tag) and conId = :conId order by id desc")
	public List<Comment> findByConId(@Param("conId") Long conId, @Param("tag") String pag, Pageable pageable);

	@Query("select count(1) from Comment where (state = 0 or state =:tag) and conId = :conId")
	public int findByConId(@Param("conId") Long conId, @Param("tag") String pag);

	// 根据内容id分页获取评论内容
	@Query("from Comment where state = 0 and (bih=1 or bih=:tag) and conId = :conId order by id desc")
	public List<Comment> findByConId(@Param("conId") Long conId, @Param("tag") Integer pag, Pageable pageable);

	@Query("select count(1) from Comment where state = 0 and (bih=1 or bih=:tag) and conId = :conId")
	public int findByConId(@Param("conId") Long conId, @Param("tag") Integer pag);

	// 根据内容id获取所有评论内容
	public List<Comment> findByConId(Long conId);

	public Comment findById(Long id);
}
