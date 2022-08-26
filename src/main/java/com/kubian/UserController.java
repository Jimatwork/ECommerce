package com.kubian;

import com.alibaba.fastjson.JSONObject;
import com.kubian.mode.ServerPath;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.ServerPathDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 系统管理 ClassName:UserController
 * 
 * @Description: TODO
 * @author WangW
 * @date 2018年4月10日
 */
@RestController
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final long serialVersionUID = 1L;
	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;

	@Autowired
	private UserDao userDao;
	@Autowired
	private ServerPathDao serverPathDao;
	@Autowired
	private AppUserDao appUserDao;
	private ListRange<User> listRange = new ListRange<User>();

	/**
	 * 会员微信登录
	 * 
	 * @Description: TODO
	 * @param @param
	 *            user
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月21日
	 */
	@RequestMapping(value = "/weixLogin")
	public Object weixLogin(String userNName) {
		ReturnMsg returnMsg = new ReturnMsg();
		List<User> users = new ArrayList<User>();
		try {
			User user = userDao.findByUserName(userNName);
			if (!MyTools.isEmpty(user)) {
				users.add(user);
				listRange.setMessage("登录成功!");
				listRange.setSuccess(true);
				listRange.setList(users);
				listRange.setTotalSize(0);
			} else {
				listRange.setMessage("不是会员用户!");
				listRange.setSuccess(false);
				listRange.setList(null);
				listRange.setTotalSize(0);
			}
		} catch (Exception e) {
			listRange.setMessage("异常错误!");
			listRange.setSuccess(false);
			listRange.setList(null);
			listRange.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 登录
	 *
	 * @param user
	 * @param request
	 * @return String
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public Object login(User user, HttpServletRequest request) {
		ListRange<User> listRange = new ListRange<User>();
		try {
			String userName = user.getUserName();
			String userPwd = user.getUserPwd();
			List<User> users = new ArrayList<User>();
			if (userName.equals("") || userName == null || userPwd.equals("") || userPwd == null) {
				listRange.setMessage("用户名或者密码为空");
				listRange.setSuccess(false);
			} else {
				User flag = new User();
				if ("admin".equals(user.getUserName())) {
					flag = userDao.findByUserNameAndUserPwdAndUserRole(user.getUserName(), user.getUserPwd(), 1);
				} else if ("portaladm".equals(user.getUserName())) {
					flag = userDao.findByUserNameAndUserPwdAndUserRole(user.getUserName(), user.getUserPwd(), 1);
				} else {
					flag = userDao.getUser(user.getUserName(), user.getUserPwd());
				}

				if (flag != null) {
					request.getSession().setAttribute("wuser", flag);
					users.add(flag);
					listRange.setMessage("登录成功!");
					listRange.setSuccess(true);
					listRange.setList(users);
					listRange.setTotalSize(0);
				} else {
					listRange.setMessage("用户名或密码错误!");
					listRange.setSuccess(false);
					listRange.setList(null);
					listRange.setTotalSize(0);
				}
			}
		} catch (Exception e) {
			listRange.setMessage("异常错误!");
			listRange.setSuccess(false);
			listRange.setList(null);
			listRange.setTotalSize(0);
			e.printStackTrace();
		}
		return listRange;
	}

	/**
	 * 查询用户信息
	 *
	 * @param request
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/queryLogin")
	@ResponseBody
	public Object queryLogin(Long userId, HttpServletRequest request) {
		ReturnMsg returnMsg = new ReturnMsg();
		// User user = (User) request.getSession().getAttribute("wuser");
		User user = null;
		try {
			user = userDao.findById(userId);
			List<User> list = new ArrayList<User>();
			if (!MyTools.isEmpty(user)) {
				list.add(user);
				returnMsg.setList(list);
				returnMsg.setMsg("获取成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(1);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("获取失败！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(1);
			}
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			e.printStackTrace();
		}
		// this.outJsonString(user.getId()+","+user.getUserPwd());
		// return user.getId() + "," + user.getUserPwd();
		return user;
	}

	/**
	 * 修改密码
	 *
	 * @param user
	 * @param request
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/modUser")
	@ResponseBody
	public Object modUser(User user, HttpServletRequest request) {
		ListRange<User> listRange = new ListRange<User>();
		try {
			User users = userDao.findById(user.getId());
			users.setUserPwd(user.getUserPwd());
			userDao.save(users);
			if (users.getUserPwd().equals(user.getUserPwd())) {
				listRange.setSuccess(true);
				listRange.setMessage("密码修改成功!");
				listRange.setList(null);
				listRange.setTotalSize(0);
			}
			// 删除session
			request.getSession().invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRange;
	}

	/**
	 * 注销
	 *
	 * @param request
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/loginout")
	@ResponseBody
	public Object loginout(HttpServletRequest request) {
		ListRange<User> listRange = new ListRange<User>();
		request.getSession().invalidate();
		listRange.setMessage("退出成功!");
		listRange.setSuccess(true);
		listRange.setList(null);
		listRange.setTotalSize(0);
		return listRange;
	}

	/**
	 * 设置服务器请求地址
	 *
	 * @param server_path
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/setServerPath")
	public Object setServerPath(Long id, String serverPath, String fcServerPath) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ServerPath serverPath3 = new ServerPath();
			serverPath3.setFcServerPath(fcServerPath);
			serverPath3.setId(id);
			serverPath3.setServerPath(serverPath);
			if (!MyTools.isEmpty(id)) {
				// id不为空是修改
				ServerPath serverPath2 = serverPathDao.findById(id);
				if (!MyTools.isEmpty(serverPath2)) {
					try {
						MyTools.updateNotNullFieldForPatient(serverPath2, serverPath3);
						serverPathDao.save(serverPath2);
						returnMsg.setSuccess(true);
						returnMsg.setList(null);
						returnMsg.setTotalSize(0);
						returnMsg.setMsg("修改成功！");
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				} else {
					returnMsg.setSuccess(false);
					returnMsg.setList(null);
					returnMsg.setTotalSize(0);
					returnMsg.setMsg("数据不存在！");
				}
			} else {
				serverPathDao.save(serverPath3);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("添加成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;

	}

	/**
	 * 判断访问的ip的中国的还是外国的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            request
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月13日
	 */
	@RequestMapping(value = "/getServerPath")
	public Object getServerPath(HttpServletRequest request) {
		// if (!MyTools.isEmpty(tag)) {
		// ServerPath serverPath = serverPathDao.getServerPath();
		// return serverPath;
		// }
		String flag = "";
		String ip = RequestUtil.getIpAddr(request);
		if (ip.indexOf(",") != -1) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		System.out.println(ip);
		// 获取服务器地址
		// ServerPath serverPath = serverPathDao.getServerPath();
		// ServerPath serverPath2 = new ServerPath();
		// if (!MyTools.isEmpty(serverPath)) {
		Object obj = HttpRequestUtil.sendPost("http://ip.taobao.com/service/getIpInfo.php", "ip=" + ip); // 获取请求的用户的地址

		System.out.println("-------------" + obj.toString());
		JSONObject jsn = JSONObject.parseObject(obj.toString()); // 转成json对象
		String nation = ((JSONObject) jsn.get("data")).get("country").toString(); // 获取对象里的值(所属国家)
		// serverPath2.setId(serverPath.getId());
		System.out.println(ip + "--" + nation);
		if ("中国".equals(nation) || "XX".equals(nation)) {
			flag = "cn";
			// serverPath2.setServerPath(serverPath.getServerPath()); // 国内的域名
		} else {
			// serverPath2.setServerPath(serverPath.getFcServerPath()); // 美国的域名
		}
		// }

		return flag;
	}

	/**
	 * 修改用户信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            user
	 * @param @param
	 *            imgFile
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月11日
	 */
	@RequestMapping(value = "/updateUserInfo")
	public Object updateUserInfo(Long id, String nickName, Integer showTime, Integer flag,
			@RequestParam("imgFile") MultipartFile imgFile, @RequestParam("qdFile") MultipartFile pdFile,
			@RequestParam("sFile") MultipartFile sFile, @RequestParam("usersFile") MultipartFile usersFile) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			User user2 = userDao.findById(id);
			if (!MyTools.isEmpty(imgFile) && imgFile.getSize() > 0) {
				String imgPath = MyTools.saveFiles(imgFile, uploadPath, webImgPath);
				user2.setImg(imgPath);
			}
			if (!MyTools.isEmpty(pdFile) && pdFile.getSize() > 0) {
				String imgPath = MyTools.saveFiles(pdFile, uploadPath, webImgPath);
				user2.setStartPath(imgPath);
			}
			if (!MyTools.isEmpty(sFile) && sFile.getSize() > 0) {
				String imgPath = MyTools.saveFiles(sFile, uploadPath, webImgPath);
				user2.setWatermark(imgPath);
			}
			if (!MyTools.isEmpty(usersFile) && usersFile.getSize() > 0) {
				String imgPath = MyTools.saveFiles(usersFile, uploadPath, webImgPath);
				user2.setUserWatermark(imgPath);
				appUserDao.updateAppUserSyImg1(imgPath);
			}
			if (!MyTools.isEmpty(nickName)) {
				user2.setNickName(nickName);
			}
			if (!MyTools.isEmpty(showTime)) {
				user2.setShowTime(showTime);
			}
			if (!MyTools.isEmpty(flag)) {
				user2.setFlag(flag);
			}
			userDao.save(user2);
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("修改成功！");
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 设置ios是否允许微信登录
	 * 
	 * @Description: TODO
	 * @param @param
	 *            flag
	 * @param @return
	 * @return boolean
	 * @throws @author
	 *             HD
	 * @date 2018年6月14日
	 */
	@RequestMapping(value = "/setIos")
	public Object setIos(Integer flag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {

			User user = userDao.findByUserNameAndUserRole("admin", 1);
			user.setFlag(flag);
			userDao.save(user);
			returnMsg.setMsg("设置成功！");
			returnMsg.setSuccess(true);
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);

			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 判断是否允许ios显示登录
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws @author
	 *             HD
	 * @date 2018年6月14日
	 */
	@RequestMapping(value = "/getIos")
	public Object getIos() {
		User user = userDao.findByUserNameAndUserRole("admin", 1);
		if (!MyTools.isEmpty(user)) {
			if (!MyTools.isEmpty(user.getFlag()) && user.getFlag() == 1) {
				// 显示ios微信登录功能
				return "true";
			}
		}
		return "false";
	}

}
