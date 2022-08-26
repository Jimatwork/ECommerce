package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.BackUpColumnContent;

/**
 * 内容备份 ClassName: BackUpColumnContentDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月4日
 */
@Transactional
@Repository
public interface BackUpColumnContentDao extends CrudRepository<BackUpColumnContent, Integer> {

	// 根据app用户id查询备份的内容
	Page<BackUpColumnContent> findByAppUserId(Long appUserId, Pageable pageable);

	// 根据用户id查询备份的内容
	Page<BackUpColumnContent> findByUserId(Long userId, Pageable pageable);

	List<BackUpColumnContent> findAll();
}
