package com.kubian.mode.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.Friend;

/**
 * 好友 ClassName: FriendDao
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月11日
 */
@Transactional
@Repository
public interface FriendDao extends CrudRepository<Friend, Integer> {
	// 根据用户id和好友id查询数据
	Friend findByAppUserIdAndFriendId(Long appUserId, Long friendId);

	// 根据用户id和好友分组id查询数据
	List<Friend> findByAppUserIdAndFriendGropIdAndIsPass(Long appUserId, Long friendGropId, Integer isPass);

	Friend findById(Long id);
}
