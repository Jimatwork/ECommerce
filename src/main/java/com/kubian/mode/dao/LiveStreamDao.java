package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.LiveStream;

/**
 * 直播频道 ClassName: LiveStreamDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月4日
 */
@Transactional
@Repository
public interface LiveStreamDao extends CrudRepository<LiveStream, Integer> {
	LiveStream findById(Long id);

	List<LiveStream> findByAppUserId(Long appUserId);
}
