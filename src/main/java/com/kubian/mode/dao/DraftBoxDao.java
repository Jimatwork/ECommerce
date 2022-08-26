package com.kubian.mode.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.DraftBox;

/**
 * 草稿箱
 * ClassName: DraftBoxDao 
 * @Description: TODO
 * @author HD
 * @date 2018年4月12日
 */
@Transactional
@Repository
public interface DraftBoxDao extends CrudRepository<DraftBox, Integer> {
	// 根据id获取草稿箱内容
	public DraftBox findById (Long id);
}
