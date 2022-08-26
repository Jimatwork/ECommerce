package com.kubian;

import com.alibaba.fastjson.JSONObject;
import com.kubian.mode.AppUser;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理 ClassName:UserManagerController
 * 
 * @Description: TODO
 * @author WangW
 * @date 2018年4月11日
 */
@RestController
public class UserManagerController {

	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private UserDao userDao;

	private static ReturnMsg returnMsg = new ReturnMsg();

	/**
	 * 查询所有的APPuser
	 *
	 * @param page
	 * @param limit
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getAppUserList")
	@ResponseBody
	public Object getAppUserList(Integer page, Integer limit, String search) {
		PageRequest pageable = new PageRequest(page, limit);
		ReturnMsg returnMsg = new ReturnMsg();
		if (!MyTools.isEmpty(search)) {
			List<AppUser> us = appUserDao.findAll("%" + search + "%", pageable);
			returnMsg.setList(us);
			returnMsg.setMsg("获取成功！");
			returnMsg.setTotalSize(appUserDao.findAll("%" + search + "%").size());
			returnMsg.setSuccess(true);
			return returnMsg;
		}
		Page<AppUser> us = appUserDao.findAll(pageable);
		returnMsg.setList(us.getContent());
		returnMsg.setMsg("获取成功！");
		returnMsg.setTotalSize(us.getTotalElements());
		returnMsg.setSuccess(true);
		return returnMsg;
	}

	/**
	 * 打开或关闭app用户的pc端登录权限
	 *
	 * @param id
	 * @param isPc
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/setPcLogin")
	@ResponseBody
	public ReturnMsg setPcLogin(Long id, Integer isPc) {
		ReturnMsg returnMsg = new ReturnMsg();
		AppUser au = appUserDao.findById(id);
		au.setIsPc(isPc);
		if (isPc == 1) {
			User us = new User();
			if (!MyTools.isEmpty(au.getPhone())) {
				us.setUserName(au.getPhone());
			}
			if (!MyTools.isEmpty(au.getOpenId())) {
				us.setUserName(au.getOpenId());
			}
			us.setUserPwd(au.getPhonePwd());
			us.setAuId(au.getId());
			us.setNickName(au.getUserName());
			us.setImg(au.getUserImg());
			us.setUserRole(2);
			userDao.save(us);
			returnMsg.setSuccess(true);
		} else {
			userDao.delete(userDao.findByAuId(id));
			returnMsg.setSuccess(true);
		}
		return returnMsg;
	}

	/**
	 * 获取启动文件
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月13日
	 */
	@RequestMapping(value = "/getStartImg")
	public Object getStartImg() {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			User user = userDao.findByUserNameAndUserRole("admin", 1);
			if (!MyTools.isEmpty(user)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("startPath", user.getStartPath());
				map.put("showTime", user.getShowTime());
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "异常错误！";
		}
		return "";
	}
}
