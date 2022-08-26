package com.kubian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kubian.mode.AppUser;
import com.kubian.mode.ColumnContent;
import com.kubian.mode.FriendGrop;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.ColumnContentDao;
import com.kubian.mode.dao.FriendGropDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.ImageRemarkUtil;
import com.kubian.mode.util.ListRange;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.RedisClient;
import com.kubian.mode.util.RequestUtil;
import com.kubian.mode.util.ReturnMsg;
import com.kubian.mode.util.SendSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * App用户管理 ClassName:AppUserController
 *
 * @author WangW
 * @Description: TODO
 * @date 2018年4月19日
 */
@RestController
public class AppUserController {

	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private FriendGropDao friendGropDao;
	@Autowired
	private UserDao userDao;
	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;
	@Value("${web.img.path1}")
	private String webImgPath1;
	@Autowired
	private ColumnContentDao columnContentDao;

	/**
	 * APP用户登录
	 *
	 * @param user
	 * @param request
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/appLogin")
	@ResponseBody
	public Object appLogin(@ModelAttribute AppUser user, HttpServletRequest request) {
		ListRange listRange = new ListRange();
		List<AppUser> list = new ArrayList<AppUser>();
		AppUser appUser2 = appUserDao.findByPhone(user.getPhone());
		if (!MyTools.isEmpty(appUser2)) {
			AppUser appUser = null;
			if (MyTools.isEmpty(appUser2.getPrefix())) {
				appUser = appUserDao.findByPhoneAndPhonePwd(user.getPhone(), user.getPhonePwd());
			} else {
				appUser = appUserDao.findByPhoneAndPhonePwdAndPrefix(user.getPhone(), user.getPhonePwd(),
						user.getPrefix());
			}
			if (appUser != null) {
				request.getSession().setAttribute("loginUser", appUser);
				// appUser.setPhonePwd(null);
				list.add(appUser);
				listRange.setMessage("登录成功!");
				listRange.setSuccess(true);
				listRange.setList(list);
			} else {
				listRange.setMessage("用户名或密码错误!");
				listRange.setSuccess(false);
			}
		} else {
			list.add(null);
			listRange.setMessage("登录失败，帐号不存在!");
			listRange.setSuccess(false);
			listRange.setList(list);

		}

		return listRange;
	}

	/**
	 * APP微信用户登录
	 *
	 * @param user
	 * @param request
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/appWeixinLogin")
	@ResponseBody
	public Object weixinLogin(@ModelAttribute AppUser user, HttpServletRequest request) {
		ListRange listRange = new ListRange();
		List<AppUser> list = new ArrayList<AppUser>();
		AppUser appUser = appUserDao.findByOpenId(user.getOpenId());
		if (appUser == null) {
			appUserDao.save(user);
		}
		// if (MyTools.isEmpty(appUser.getPhone())) {
		// list.add(appUser);
		// listRange.setMessage("请绑定手机号码!");
		// listRange.setSuccess(true);
		// listRange.setList(list);
		// }
		request.getSession().setAttribute("loginUser", appUser);
		// appUser.setPhonePwd(null);
		list.add(appUser);
		listRange.setMessage("登录成功!");
		listRange.setSuccess(true);
		listRange.setList(list);
		return listRange;
	}

	/**
	 * APP用户注销
	 *
	 * @param request
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/appCancel")
	@ResponseBody
	public Object appCancel(HttpServletRequest request) {
		ListRange listRange = new ListRange();
		if (request.getSession().getAttribute("loginUser") != null) {
			request.getSession().invalidate();
			listRange.setSuccess(true);
			listRange.setMessage("注销成功!");
		} else {
			listRange.setSuccess(false);
			listRange.setMessage("系统未检测到该用户登录!");
		}
		return listRange;
	}

	/**
	 * APP用户注册
	 *
	 * @param appUser
	 * @param verifycode
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/register")
	public Object register(@ModelAttribute AppUser appUser, @RequestParam("verifycode") String verifycode) {
		if (MyTools.isEmpty(appUser.getUserImg())) {
			appUser.setUserImg(webImgPath1 + "/yh.png");
			appUser.setUserName(appUser.getPhone());
		}
		Jedis jedis = RedisClient.getJedis();
		ListRange listRange = new ListRange();
		String prefix = appUser.getPrefix();
		if (!MyTools.isEmpty(prefix)) {
			if (prefix.indexOf("+") == -1) {
				prefix = "+" + prefix;
			}
		}
		appUser.setPrefix(prefix);
		try {
			String s = jedis.get(prefix + appUser.getPhone() + "send");
			// System.out.println("redis缓存的是："+s + appUser.getPrefix() +
			// appUser.getPhone()+ "send");
			if (s == null) {
				listRange.setMessage("验证码超时无效!");
				listRange.setSuccess(false);
			} else if (!s.equals(verifycode)) {
				listRange.setMessage("验证码错误!");
				listRange.setSuccess(false);
			} else if (MyTools.isEmpty(appUserDao.findByPhoneAndPrefix(appUser.getPhone(), prefix))
					&& s.equals(verifycode)) {
				listRange.setMessage("注册成功!");
				listRange.setSuccess(true);
				jedis.del(prefix + appUser.getPhone() + "reset");
				AppUser appUser1 = appUserDao.save(appUser);
				Long appId = appUser1.getId();
				FriendGrop friendGrop = new FriendGrop();
				friendGrop.setUserId(appId);
				friendGropDao.save(friendGrop);
			} else {
				listRange.setMessage("该用户已注册!");
				listRange.setSuccess(false);
			}
		} catch (Exception e) {
			listRange.setMessage("异常错误!");
			listRange.setSuccess(false);
			e.printStackTrace();
		} finally {
			RedisClient.returnResource(jedis);
		}
		return listRange;
	}

	/**
	 * 手机验证码发送
	 *
	 * @param appUser
	 * @param way
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/sendSms")
	@ResponseBody
	public Object sendSms(@ModelAttribute AppUser appUser, @RequestParam("way") String way) {
		String verifycode = MyTools.getCode();
		Jedis jedis = RedisClient.getJedis();
		ListRange listRange = new ListRange();
		try {
			if (appUser.getPhone().isEmpty() || way.isEmpty() || appUser.getPrefix().isEmpty()) {
				listRange.setMessage("参数错误!");
				listRange.setSuccess(false);
				return listRange;
			}
			if (MyTools.isMobile(appUser.getPhone())) {
				listRange.setMessage("手机号码格式错误!");
				listRange.setSuccess(false);
				return listRange;
			}
			// 如果为1则请求类型为注册
			AppUser user2 = appUserDao.findByPhone(appUser.getPhone());
			if (!MyTools.isEmpty(user2)) {
				if (MyTools.isEmpty(user2.getPrefix())) {
					listRange.setMessage("该手机号码已注册!");
					listRange.setSuccess(false);
					return listRange;
				}
			}
			AppUser user = appUserDao.findByPhoneAndPrefix(appUser.getPhone(), appUser.getPrefix());
			if (way.equals("1")) {
				if (!MyTools.isEmpty(user)) {
					listRange.setMessage("该手机号码已注册!");
					listRange.setSuccess(false);
					return listRange;
				}
				SendSmsHelper(verifycode, appUser, listRange, way, jedis);

			}
			// 如果为2则请求类型为忘记密码
			else if (way.equals("2")) {
				if (MyTools.isEmpty(user)) {
					listRange.setMessage("该用户不存在!");
					listRange.setSuccess(false);
					return listRange;
				}
				SendSmsHelper(verifycode, appUser, listRange, way, jedis);
			}
			// 如果为3则请求类型为更改绑定手机
			else if (way.equals("3")) {
				if (!MyTools.isEmpty(user)) {
					listRange.setMessage("请使用未注册的手机号码更换绑定!");
					listRange.setSuccess(false);
					return listRange;
				}
				SendSmsHelper(verifycode, appUser, listRange, way, jedis);
			}
			if (way.equals("1")) {
				jedis.set(appUser.getPrefix() + appUser.getPhone() + "send", verifycode);
				jedis.expire(appUser.getPrefix() + appUser.getPhone() + "send", 180);
				System.out.println(appUser.getPrefix() + appUser.getPhone() + "send");
			} else if (way.equals("2")) {
				jedis.set(appUser.getPrefix() + appUser.getPhone() + "reset", verifycode);
				jedis.expire(appUser.getPrefix() + appUser.getPhone() + "reset", 180);
			} else {
				jedis.set(appUser.getPrefix() + appUser.getPhone() + "change", verifycode);
				jedis.expire(appUser.getPrefix() + appUser.getPhone() + "change", 180);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisClient.returnResource(jedis);
		}
		return listRange;
	}

	/**
	 * 忘记密码
	 *
	 * @param appUser
	 * @param verifycode
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping("/forgotPassword")
	@ResponseBody
	public Object forgotPassword(@ModelAttribute AppUser appUser, String verifycode) {
		Jedis jedis = RedisClient.getJedis();
		ListRange listRange = new ListRange();
		try {
			String s = jedis.get(appUser.getPrefix() + appUser.getPhone() + "reset");
			if (s == null) {
				listRange.setMessage("验证码超时无效!");
				listRange.setSuccess(false);
				return listRange;
			}
			if (!s.equals(verifycode)) {
				listRange.setMessage("验证码错误!");
				listRange.setSuccess(false);
				return listRange;
			}
			AppUser user = appUserDao.findByPhoneAndPrefix(appUser.getPhone(), appUser.getPrefix());
			if (!MyTools.isEmpty(user)) {
				user.setPrefix(appUser.getPrefix());
				user.setPhonePwd(appUser.getPhonePwd());
				appUserDao.save(user);
				// 判断是不是会员用户
				User user2 = userDao.findByAuId(user.getId());
				if (!MyTools.isEmpty(user2)) {
					user2.setUserPwd(appUser.getPhonePwd());
					userDao.save(user2);
				}
				listRange.setSuccess(true);
				listRange.setMessage("密码重置成功!");
				jedis.del(appUser.getPrefix() + appUser.getPhone() + "reset");
				return listRange;
			} else {
				listRange.setSuccess(false);
				listRange.setMessage("该用户暂未注册!");
			}
		} catch (Exception e) {
			listRange.setSuccess(false);
			listRange.setMessage("异常错误!");
			e.printStackTrace();
		} finally {
			RedisClient.returnResource(jedis);
		}
		return listRange;
	}

	/**
	 * 用户密码修改
	 *
	 * @param newpassword
	 * @param oldpassword
	 * @param request
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping("/updateUserPwdBySession")
	@ResponseBody
	public Object updateUserPwdBySession(String newpassword, String oldpassword, HttpServletRequest request) {
		ListRange listRange = new ListRange();
		try {
			AppUser appUser = (AppUser) request.getSession().getAttribute("loginUser");
			if (appUser != null && appUser.getPhonePwd().equals(oldpassword)) {
				appUser.setPhonePwd(newpassword);
				appUserDao.save(appUser);
				// 判断是不是会员用户
				User user2 = userDao.findByAuId(appUser.getId());
				if (!MyTools.isEmpty(user2)) {
					user2.setUserPwd(appUser.getPhonePwd());
					userDao.save(user2);
				}
				listRange.setSuccess(true);
				listRange.setMessage("密码修改成功!");
				request.getSession().setAttribute("loginUser", appUser);
			} else {
				listRange.setSuccess(false);
				listRange.setMessage("修改失败!");
			}
		} catch (Exception e) {
			listRange.setSuccess(false);
			listRange.setMessage("异常错误!");
			e.printStackTrace();
		}
		return listRange;
	}

	/**
	 * 获取登录APP用户信息
	 *
	 * @param id
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping("/getInfoBySession")
	@ResponseBody
	public Object getInfoBySession(Long id) {
		ListRange listRange = new ListRange();
		try {
			AppUser appUser = appUserDao.findById(id);
			if (appUser != null) {
				appUser.setPhonePwd("********");
				List<AppUser> list = new ArrayList<AppUser>();
				list.add(appUser);
				listRange.setMessage("获取成功!");
				listRange.setSuccess(true);
				listRange.setList(list);
			} else {
				listRange.setSuccess(false);
				listRange.setMessage("未检测到该用户登录!");
			}
		} catch (Exception e) {
			listRange.setSuccess(false);
			listRange.setMessage("异常错误!");
			e.printStackTrace();
		}
		return listRange;
	}

	/**
	 * 用户绑定手机更改
	 *
	 * @param appUser
	 * @param verifycode
	 * @param oldPhone
	 * @param request
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/changeBinding")
	@ResponseBody
	public Object changeBinding(@ModelAttribute AppUser appUser, String oldPhone, String verifycode,
			HttpServletRequest request) {
		ListRange listRange = new ListRange();
		Jedis jedis = RedisClient.getJedis();
		try {
			String s = jedis.get(appUser.getPrefix() + appUser.getPhone() + "change");
			if (appUser.getPhone().equals(oldPhone)) {
				listRange.setMessage("换绑手机不能与原绑定手机相同");
				listRange.setSuccess(false);
				return listRange;
			}
			if (s == null) {
				listRange.setMessage("验证码超时无效!");
				listRange.setSuccess(false);
				return listRange;
			}
			if (!s.equals(verifycode)) {
				listRange.setMessage("验证码错误!");
				listRange.setSuccess(false);
				return listRange;
			}
			AppUser olduser = appUserDao.findByPhone(oldPhone);
			AppUser newuser = appUserDao.findByPhoneAndPrefix(appUser.getPhone(), appUser.getPrefix());
			if (newuser != null) {
				listRange.setMessage("该手机已经被注册过啦!");
				listRange.setSuccess(false);
				return listRange;
			}
			if (olduser != null) {
				olduser.setPhone(appUser.getPhone());
				olduser.setPrefix(appUser.getPrefix());
				appUserDao.save(olduser);
				request.getSession().setAttribute("loginUser", olduser);
				jedis.del(appUser.getPrefix() + appUser.getPhone() + "reset");
				listRange.setMessage("绑定手机修改成功!");
				listRange.setSuccess(true);
			}
		} catch (Exception e) {
			listRange.setSuccess(false);
			listRange.setMessage("异常错误!");
			e.printStackTrace();
		} finally {
			RedisClient.returnResource(jedis);
		}
		return listRange;
	}

	/**
	 * 用户信息修改
	 *
	 * @param appUser
	 * @param request
	 * @param multipartFile
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/updateAppUserBySession")
	@ResponseBody
	public Object updateAppUserBySession(AppUser appUser, HttpServletRequest request) {
		ListRange listRange = new ListRange();
		// AppUser appUser1 = (AppUser)
		// request.getSession().getAttribute("loginUser");
		// if (!appUser.getUserName().isEmpty()) {
		// appUser1.setUserName(appUser.getUserName());
		// }
		List<AppUser> list = new ArrayList<AppUser>();
		AppUser appUser1 = new AppUser();
		appUser1 = appUserDao.findById(appUser.getId());
		if (!MyTools.isEmpty(appUser1)) {
			try {
				if (appUser.getFans() == 0) {
					appUser.setFans(appUser1.getFans());
				}
				if (appUser.getAttention() == 0) {
					appUser.setAttention(appUser1.getAttention());
				}
				MyTools.updateNotNullFieldForPatient(appUser1, appUser);
				appUserDao.save(appUser1);

				request.getSession().setAttribute("loginUser", appUser1);
				list.add(appUser1);
				listRange.setList(list);
				listRange.setMessage("修改成功!");
				listRange.setSuccess(true);
			} catch (Exception e) {
				listRange.setMessage("修改失败!");
				listRange.setSuccess(false);
				e.printStackTrace();
			}
		} else {
			listRange.setMessage("修改失败!");
			listRange.setSuccess(false);
		}
		return listRange;
	}

	/**
	 * 用户水印图添加
	 *
	 * @param appUser
	 * @param bigImg
	 * @param middleImg
	 * @param smallImg
	 * @param defaultImg
	 * @return {@link Object}
	 * @author WangW
	 */
	@RequestMapping(value = "/uploadThumbnail")
	@ResponseBody
	public Object uploadThumbnail(AppUser appUser, MultipartFile bigImg, MultipartFile middleImg,
			MultipartFile smallImg, MultipartFile defaultImg) {
		ListRange listRange = new ListRange();
		AppUser appUsers = appUserDao.findById(appUser.getId());
		Boolean flag = false;
		if (MyTools.isImageFile(bigImg.getOriginalFilename())) {
			String BigfileName = MyTools.saveFiles(bigImg, uploadPath, webImgPath);
			appUsers.setSyImg1(BigfileName);
			flag = true;
		}
		if (MyTools.isImageFile(middleImg.getOriginalFilename())) {
			String BigfileName = MyTools.saveFiles(middleImg, uploadPath, webImgPath);
			appUsers.setSyImg2(BigfileName);
			flag = true;
		}
		if (MyTools.isImageFile(smallImg.getOriginalFilename())) {
			String BigfileName = MyTools.saveFiles(smallImg, uploadPath, webImgPath);
			appUsers.setSyImg3(BigfileName);
			flag = true;
		}
		if (MyTools.isImageFile(defaultImg.getOriginalFilename())) {
			String BigfileName = MyTools.saveFiles(defaultImg, uploadPath, webImgPath);
			appUsers.setSltImg(BigfileName);
			flag = true;
		}
		if (flag) {
			appUserDao.save(appUsers);
			listRange.setMessage("水印图添加成功!");
			listRange.setSuccess(true);
		} else {
			listRange.setMessage("水印图添加失败!");
			listRange.setSuccess(false);
		}
		return listRange;
	}

	public void SendSmsHelper(String verifycode, AppUser appUser, ListRange listRange, String way, Jedis jedis)
			throws IOException {
		String s = SendSMS.sendSms(verifycode, appUser.getPrefix() + appUser.getPhone());
		// System.out.println("================================="+appUser.getPrefix()
		// + appUser.getPhone()+verifycode);
		JSONObject json = JSONObject.parseObject(s);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(json);
		List listss = JSONObject.parseArray(jsonArray.toString(), Object.class);
		listRange.setList(listss);
		listRange.setTotalSize(listss.size());
		listRange.setMessage("发送成功!");
		listRange.setSuccess(true);
	}

	/**
	 * 根据id获取app用户详细信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月29日
	 */
	@RequestMapping(value = "/getFansAndAttention")
	public Object getFansAndAttention(@RequestParam("id") Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			AppUser appUser = appUserDao.findById(id);
			List<AppUser> list = new ArrayList<AppUser>();
			if (!MyTools.isEmpty(appUser)) {
				list.add(appUser);
			}
			returnMsg.setList(list);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(1);
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id修改用户密码
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            newPwd
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月11日
	 */
	@RequestMapping(value = "/updateAppUserPwd")
	public Object updateAppUserPwd(Long id, String newPwd) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			AppUser appUser = appUserDao.findById(id);
			if (!MyTools.isEmpty(appUser)) {
				appUser.setPhonePwd(newPwd);
				appUserDao.save(appUser);
			}
			User user = userDao.findByAuId(id);
			if (!MyTools.isEmpty(user)) {
				// 是允许pc登录的app用户
				user.setUserPwd(newPwd);
				userDao.save(user);
			}
			returnMsg.setList(null);
			returnMsg.setMsg("修改成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 添加水印
	 * 
	 * @Description: TODO
	 * @param @param
	 *            leftFile
	 * @param @param
	 *            rightFile
	 * @param @param
	 *            imgFile
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月12日
	 */
	@RequestMapping(value = "/addWatermark")
	public Object addWatermark(String leftFile, String rightFile, String imgFile, Long appUserId, String userName) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			AppUser appUser = appUserDao.findById(appUserId);
			if (!MyTools.isEmpty(rightFile)) {
				appUser.setSyImg2(rightFile);
			}
			if (!MyTools.isEmpty(imgFile)) {
				appUser.setSltImg(imgFile);
			}
			if (!MyTools.isEmpty(userName)) {
				appUser.setUserName(userName);
			}
			appUserDao.save(appUser);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("设置成功！");
			// if (!MyTools.isEmpty(imgFile)) {
			// imgFile = imgFile.substring(imgFile.indexOf("KuBianImg/") + 10);
			// String imgFile2 = uploadPath + imgFile;
			// System.out.println(imgFile2);
			// if (!MyTools.isEmpty(leftFile) && MyTools.isEmpty(rightFile)) {
			// leftFile = leftFile.substring(leftFile.indexOf("KuBianImg/") +
			// 10);
			// String leftFile2 = uploadPath + leftFile;
			// // 只添加左上角水印
			// ImageRemarkUtil.markImageByIcon(leftFile, imgFile, imgFile);
			// System.out.println(webImgPath + imgFile);
			// appUser.setSyImg2(webImgPath + imgFile);
			// appUserDao.save(appUser);
			// returnMsg.setList(null);
			// returnMsg.setTotalSize(0);
			// returnMsg.setSuccess(true);
			// returnMsg.setMsg("设置成功！");
			// }
			// if (MyTools.isEmpty(leftFile) && !MyTools.isEmpty(rightFile)) {
			// rightFile = rightFile.substring(rightFile.indexOf("KuBianImg/") +
			// 10);
			// String rightFile2 = uploadPath + rightFile;
			// // 只添加有下角水印
			// ImageRemarkUtil.markImageByIcon1(rightFile, imgFile, imgFile);
			// System.out.println(webImgPath + imgFile);
			// appUser.setSyImg2(webImgPath + imgFile);
			// appUserDao.save(appUser);
			// returnMsg.setList(null);
			// returnMsg.setTotalSize(0);
			// returnMsg.setSuccess(true);
			// returnMsg.setMsg("设置成功！");
			// }
			// if (!MyTools.isEmpty(leftFile) && !MyTools.isEmpty(rightFile)) {
			// rightFile = rightFile.substring(rightFile.indexOf("KuBianImg/") +
			// 10);
			// String rightFile2 = uploadPath + rightFile;
			// leftFile = leftFile.substring(leftFile.indexOf("KuBianImg/") +
			// 10);
			// String leftFile2 = uploadPath + leftFile;
			// // 左上角和右下角水印都添加
			// ImageRemarkUtil.markImageByIcon1(rightFile2, imgFile2, imgFile2);
			//
			// ImageRemarkUtil.markImageByIcon(leftFile2, imgFile2, imgFile2);
			// System.out.println(webImgPath + imgFile);
			// appUser.setSyImg2(webImgPath + imgFile);
			// appUserDao.save(appUser);
			// returnMsg.setList(null);
			// returnMsg.setTotalSize(0);
			// returnMsg.setSuccess(true);
			// returnMsg.setMsg("设置成功！");
			// }
			// } else {
			// returnMsg.setList(null);
			// returnMsg.setTotalSize(0);
			// returnMsg.setSuccess(false);
			// returnMsg.setMsg("设置失败！");
			// }
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id删除用户 参数 id
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUser
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月20日
	 */
	@RequestMapping(value = "/delAppUserById")
	public Object delAppUserById(@ModelAttribute AppUser appUser) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			appUserDao.delete(appUser);
			List<ColumnContent> cons = columnContentDao.findByAppUserId(appUser.getId());
			if (cons != null && cons.size() > 0) {
				for (ColumnContent columnContent : cons) {
					columnContentDao.delete(columnContent);
				}
			}
			returnMsg.setList(null);
			returnMsg.setMsg("删除成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}

		return returnMsg;
	}

	/**
	 * 验证手机号码是否存在
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月26日
	 */
	@RequestMapping(value = "/isAppUser")
	public Object isAppUser(String phone, String prefix) {
		if (!MyTools.isEmpty(prefix) && prefix.indexOf("+") == -1) {
			prefix = "+" + prefix.trim();
		}
		System.out.println(prefix + phone);
		AppUser appUser = appUserDao.findByPhoneAndPrefix(phone, prefix);
		if (!MyTools.isEmpty(appUser)) {
			System.out.println("进来了");
			// 有数据已存在 返回0
			return appUser;
		}
		return null;
	}

	/**
	 * 把外部的账号信息存起来
	 * 
	 * @Description: TODO
	 * @param @param
	 *            phone 手机号码
	 * @param @param
	 *            phonePwd 密码
	 * @param @param
	 *            number 外部的网站的编号
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月27日
	 */
	@RequestMapping(value = "/saveAppUserInfo")
	public Object saveAppUserInfo(String phone, String prefix, String phonePwd, Long number) {
		AppUser appUser = new AppUser();
		appUser.setPhone(phone);
		appUser.setPhonePwd(phonePwd);
		appUser.setNumber(number);
		appUser.setUserName(phone);
		appUser.setUserImg(webImgPath1 + "/yh.png");
		appUser.setPrefix(prefix);
		AppUser appUser2 = appUserDao.findByPhoneAndPrefix(phone, prefix);
		if (MyTools.isEmpty(appUser2)) {
			appUserDao.save(appUser);
		} else {
			return 0;
		}
		return 1;
	}

	/**
	 * 微信网页授权
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月31日
	 */
	@RequestMapping(value = "/weixinOauth")
	public Object weixinOauth(String code) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx064cb833dc1853c4&secret=f28f6f85205818454e8aa175dd916fd6&code="
					+ code + "&grant_type=authorization_code";
			String openstr = RequestUtil.httpsRequest(url, "GET", null);
			JSONObject json = JSONObject.parseObject(openstr);
			String openId = json.getString("openid");
			String token = json.getString("access_token");
			System.out.println("openId=" + openId + "/ntoken" + token);
			String url1 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openId
					+ "&lang=zh_CN";
			String userinfo = RequestUtil.httpsRequest(url1, "GET", null);
			JSONObject json2 = JSONObject.parseObject(userinfo);
			String nickname = json2.getString("nickname");
			System.out.println(userinfo);
			System.out.println(nickname);
			AppUser appUser = appUserDao.findByOpenId(openId);
			List<AppUser> list = new ArrayList<AppUser>();
			if (appUser == null) {
				// 没有注册 注册
				AppUser appUser1 = new AppUser();
				appUser1.setOpenId(openId);
				appUser1.setUserName(nickname);
				appUser1.setUserImg(webImgPath1 + "/yh.png");

				AppUser appuser2 = appUserDao.save(appUser1);
				list.add(appuser2);
			} else {
				list.add(appUser);
			}
			returnMsg.setList(list);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			e.printStackTrace();
		}
		return returnMsg;
	}
}
