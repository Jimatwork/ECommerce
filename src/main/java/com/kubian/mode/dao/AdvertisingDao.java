package com.kubian.mode.dao;

import com.kubian.mode.Advertising;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AdvertisingDao extends CrudRepository<Advertising, Integer> {

	// 获取所有的普通广告
	List<Advertising> findAll();

	// 获取所有的普通广告(分页)
	Page<Advertising> findAll(Pageable pageable);

	// 根据位置获取广告
	Page<Advertising> findBySite(String site, Pageable pageable);

	Advertising findById(Long id);
}
