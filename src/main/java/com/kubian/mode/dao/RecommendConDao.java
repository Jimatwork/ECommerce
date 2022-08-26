package com.kubian.mode.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kubian.mode.RecommendCon;

/**
 * 推荐表详情操作 ClassName: RecommendConDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月22日
 */
@Transactional
@Repository
public interface RecommendConDao extends CrudRepository<RecommendCon, Integer> {
	RecommendCon findById(Long id);

	Page<RecommendCon> findByRId(Long rId , Pageable pageable);
	
	List<RecommendCon> findByRId(Long rId , Sort sort);
	
	List<RecommendCon> findByConId(Long conId);
}
