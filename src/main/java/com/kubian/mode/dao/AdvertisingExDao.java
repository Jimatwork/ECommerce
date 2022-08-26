package com.kubian.mode.dao;

import com.kubian.mode.AdvertisingEx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AdvertisingExDao extends CrudRepository<AdvertisingEx, Integer> {

	// 分页查询所有的浏览区广告
//	@Query("from AdvertisingEx")
	Page<AdvertisingEx> findAll(Pageable pageable);

	AdvertisingEx findById(Long id);

	Page<AdvertisingEx> findByColid(Long colid, Pageable pageable);
}
