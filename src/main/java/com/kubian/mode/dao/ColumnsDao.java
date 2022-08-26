package com.kubian.mode.dao;

import com.kubian.mode.Columns;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 栏目操作 ClassName: ColumnsDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月12日
 */
@Transactional
@Repository
public interface ColumnsDao extends CrudRepository<Columns, Integer> {
	// 根据id获取栏目
	public Columns findById(Long id);

	/**
	 * 获取全部栏目
	 */
	public List<Columns> findAll(Sort sort);

	public Page<Columns> findAll(Pageable pageable);

	// 根据栏目colLink 获取信息
	public List<Columns> findByColLink(String colLink);
}
