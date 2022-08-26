package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.FriendGrop;

/**
 * 好友分组 ClassName: FriendGropDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月11日
 */
@Transactional
@Repository
public interface FriendGropDao extends CrudRepository<FriendGrop, Integer> {
	// 根据用户id和分组名称获取数据
	FriendGrop findByUserIdAndGropName(Long userId, String gropName);

	// 根据id获取数据
	FriendGrop findById(Long id);

	// 根据用户id获取数据
	List<FriendGrop> findByUserId(Long userId);
}
