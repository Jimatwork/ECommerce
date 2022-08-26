package com.kubian;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.AppUser;
import com.kubian.mode.Friend;
import com.kubian.mode.FriendGrop;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.FriendDao;
import com.kubian.mode.dao.FriendGropDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

/**
 * 好友操作 ClassName: FriendController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年5月11日
 */
@RestController
public class FriendController {
	@Autowired
	private FriendDao friendDao;
	@Autowired
	private FriendGropDao friendGropDao;
	@Autowired
	private AppUserDao appUserDao;
	private ReturnMsg returnMsg = new ReturnMsg();

	/**
	 * 添加好友分组
	 * 
	 * @Description: TODO
	 * @param @param
	 *            friendGrop
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/addFriendGrop")
	public Object addFriendGrop(@ModelAttribute FriendGrop friendGrop) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			FriendGrop friendGrop2 = friendGropDao.findByUserIdAndGropName(friendGrop.getUserId(),
					friendGrop.getGropName());
			if (!MyTools.isEmpty(friendGrop2)) {
				// 存在此分组不能添加
				returnMsg.setList(null);
				returnMsg.setMsg("已存在的分组，不能添加！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
				return returnMsg;
			}
			friendGropDao.save(friendGrop);
			returnMsg.setList(null);
			returnMsg.setMsg("添加成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 删除好友分组 参数userId gropName
	 * 
	 * @Description: TODO
	 * @param @param
	 *            friendGrop
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/delFriendGrop")
	public Object delFriendGrop(@ModelAttribute FriendGrop friendGrop) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			FriendGrop friendGrop2 = friendGropDao.findByUserIdAndGropName(friendGrop.getUserId(),
					friendGrop.getGropName());
			if (MyTools.isEmpty(friendGrop2)) {
				// 数据为空 不能删除
				returnMsg.setList(null);
				returnMsg.setMsg("要删除的分组不存在！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
				return returnMsg;
			}
			friendGropDao.delete(friendGrop2);
			returnMsg.setList(null);
			returnMsg.setMsg("删除成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 修改好友分组
	 * 
	 * @Description: TODO
	 * @param @param
	 *            friendGrop
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/updateFriendGrop")
	public Object updateFriendGrop(@ModelAttribute FriendGrop friendGrop) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			FriendGrop friendGrop2 = friendGropDao.findById(friendGrop.getId());
			if (!MyTools.isEmpty(friendGrop2)) {
				// 把不同的数据替换到friendGrop2里
				MyTools.updateNotNullFieldForPatient(friendGrop2, friendGrop);
				friendGropDao.save(friendGrop2);
				returnMsg.setList(null);
				returnMsg.setMsg("修改成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id获取用户的好友分组
	 * 
	 * @Description: TODO
	 * @param @param
	 *            userId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/getFriendGrop")
	public Object getFriendGrop(@RequestParam("userId") Long userId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<FriendGrop> friendGrops = friendGropDao.findByUserId(userId);
			returnMsg.setList(friendGrops);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(friendGrops.size());
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}

		return returnMsg;
	}

	/**
	 * 申请添加好友 参数 用户idappUserId 好友id friendId 发起请求的描述 description 分组id
	 * friendGropId
	 * 
	 * @Description: TODO
	 * @param @param
	 *            friend
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/addFriend")
	public Object addFriend(@ModelAttribute Friend friend) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Friend frienf2 = friendDao.findByAppUserIdAndFriendId(friend.getAppUserId(), friend.getFriendId());
			if (!MyTools.isEmpty(frienf2)) {
				// 存在数据
				if (frienf2.getIsPass() == 1) {
					returnMsg.setMsg("已发起好友请求，等待对方通过！");
					returnMsg.setSuccess(false);
					returnMsg.setList(null);
					returnMsg.setTotalSize(0);
					return returnMsg;
				}
				if (frienf2.getIsPass() == 2) {
					returnMsg.setMsg("对方已是你好友！");
					returnMsg.setSuccess(false);
					returnMsg.setList(null);
					returnMsg.setTotalSize(0);
					return returnMsg;
				}
				if (frienf2.getIsPass() == 3) {
					// 对方已拒绝添加好友，重新申请添加好友
					frienf2.setIsPass(1);
				}
			}
			friendDao.save(friend);
			returnMsg.setList(null);
			returnMsg.setMsg("申请成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id和好友分组id获取好友信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @param
	 *            friendGropId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月15日
	 */
	@RequestMapping(value = "/getFriend")
	public Object getFriend(@RequestParam("appUserId") Long appUserId,
			@RequestParam("friendGropId") Long friendGropId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// List<Friend> friends =
			// friendDao.findByAppUserIdAndFriendGropIdAndIsPass(appUserId,
			// friendGropId, 1);
			List<AppUser> appusers = appUserDao.getAppUserByFriend(appUserId, friendGropId, 2);
			returnMsg.setList(appusers);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(appusers.size());
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 获取验证消息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月16日
	 */
	@RequestMapping(value = "/getAddFriend")
	public Object getAddFriend(@RequestParam("appUserId") Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<AppUser> appUsers = appUserDao.getAppUserByIsPass(appUserId);
			if (!appUsers.isEmpty()) {
				for (AppUser appUser : appUsers) {
					appUser.setPhonePwd(null);
				}
			}

			returnMsg.setList(appUsers);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(appUsers.size());
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 同意或拒绝添加好友
	 * 
	 * @Description: TODO
	 * @param @param
	 *            friendId
	 * @param @param
	 *            isPass
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月16日
	 */
	@RequestMapping(value = "/updateFriendByIsPass")
	public Object updateFriendByIsPass(@Param("id") Long id, @Param("isPass") Integer isPass) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Friend> friends = new ArrayList<Friend>();
			Friend friend = friendDao.findById(id);
			if (!MyTools.isEmpty(friend)) {
				friend.setIsPass(isPass);
				friendDao.save(friend);
				friends.add(friend);
				returnMsg.setList(friends);
				returnMsg.setMsg("操作成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("没找到这条数据！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
			}

		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 删除好友
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月24日
	 */
	@RequestMapping(value = "/delFriend")
	public Object delFriend(@Param("id") Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Friend friend = friendDao.findById(id);
			if (!MyTools.isEmpty(friend)) {
				// 数据不为空
				friendDao.delete(friend);
				returnMsg.setList(null);
				returnMsg.setMsg("删除成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("没找到这条数据！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}
}
