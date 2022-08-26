package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.MyIncome;
/**
 * 打赏
 * ClassName: MyIncomeDao 
 * @Description: TODO
 * @author HD
 * @date 2018年4月16日
 */
@Transactional
@Repository
public interface MyIncomeDao extends CrudRepository<MyIncome, Integer> {
	
	public List<MyIncome> findAll();
	// 分页获取所有被打赏人的列表数据
	public List<MyIncome> findAll(Pageable pageable);
	// 分页获取我的打赏的数据
	public List<MyIncome> findByDsUid(String dsUid,Pageable pageable);
	public List<MyIncome> findByDsUid(String dsUid);
}
