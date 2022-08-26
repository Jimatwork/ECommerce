package com.kubian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bytedeco.javacpp.avcodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.AppUser;
import com.kubian.mode.Attention;
import com.kubian.mode.Collect;
import com.kubian.mode.ColumnContent;
import com.kubian.mode.Columns;
import com.kubian.mode.Thumbsup;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.AttentionDao;
import com.kubian.mode.dao.CollectDao;
import com.kubian.mode.dao.ColumnContentDao;
import com.kubian.mode.dao.ColumnsDao;
import com.kubian.mode.dao.ThumbsupDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.AudioConvert;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;
import com.kubian.mode.util.ToolUserInFo;

/**
 * 关注和收藏 ClassName: AttentionCollectController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@RestController
public class AttentionCollectController {
	@Autowired
	private AttentionDao attentionDao;
	@Autowired
	private CollectDao collectDao;
	@Autowired
	private ColumnContentDao columnContentDao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private ColumnsDao columnsDao;
	@Autowired
	private UserDao userDao;
	@Value("${web.img.path}")
	private String webImgPath;
	@Autowired
	private ThumbsupDao thumbsupDao;
	private ReturnMsg returnMsg = new ReturnMsg();

	/**
	 * 添加关注 参数关注的用户followerId和被关注的用户(app用户)beFollowedId 被关注人id(后台用户)
	 * beFollowedUserId;
	 * 
	 * @Description: TODO
	 * @param @param
	 *            attention
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月28日
	 */
	@RequestMapping(value = "/addAttention")
	public ReturnMsg addAttention(@ModelAttribute Attention attention, Integer tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		AppUser appUser = new AppUser();
		if (!MyTools.isEmpty(tag) && tag == 1) {
			// 添加关注
			try {

				Attention attention2 = attentionDao.findByFollowerIdAndBeFollowedIdAndJudge(attention.getFollowerId(),
						attention.getBeFollowedId(), attention.getJudge());
				if (MyTools.isEmpty(attention2)) {

					long gf = attention.getFollowerId();
					long gbf = attention.getBeFollowedId();
					if (attention.getJudge() == 0 && gf == gbf) {
						returnMsg.setList(null);
						returnMsg.setMsg("添加失败，不能关注自己！");
						returnMsg.setSuccess(false);
						returnMsg.setTotalSize(0);
						return returnMsg;
					}

					// 用户关注的值加1
					appUser = appUserDao.findById(attention.getFollowerId());
					if (!MyTools.isEmpty(appUser)) {
						appUser.setAttention(appUser.getAttention() + 1);
						appUserDao.save(appUser);
					}
					// 关注的是app用户才能给关注的用户的粉丝值加1
					if (attention.getJudge() == 0) {
						appUser = appUserDao.findById(attention.getBeFollowedId());
						if (!MyTools.isEmpty(appUser)) {
							appUser.setFans(appUser.getFans() + 1);
							appUserDao.save(appUser);
						}
					}
					attentionDao.save(attention);
					returnMsg.setList(null);
					returnMsg.setMsg("true");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(appUser.getAttention());
				} else {
					returnMsg.setList(null);
					returnMsg.setMsg("添加失败，已关注！");
					returnMsg.setSuccess(false);
					returnMsg.setTotalSize(0);
				}

			} catch (Exception e) {
				returnMsg.setMsg("添加失败！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				e.printStackTrace();

			}
		}
		if (!MyTools.isEmpty(tag) && tag == 0) {
			try {
				Attention attention2 = attentionDao.findByFollowerIdAndBeFollowedIdAndJudge(attention.getFollowerId(),
						attention.getBeFollowedId(), attention.getJudge());
				if (!MyTools.isEmpty(attention2)) {

					// 用户关注的值减1
					appUser = appUserDao.findById(attention.getFollowerId());
					if (!MyTools.isEmpty(appUser)) {
						if (appUser.getAttention() > 0) {
							appUser.setAttention(appUser.getAttention() - 1);
						}
						appUserDao.save(appUser);
					}
					// 取消关注的是app用户才能给关注的用户的粉丝值加1
					if (attention.getJudge() == 0) {
						appUser = appUserDao.findById(attention.getBeFollowedId());
						if (!MyTools.isEmpty(appUser)) {
							if (appUser.getFans() > 0) {
								appUser.setFans(appUser.getFans() - 1);
							}

							appUserDao.save(appUser);
						}
					}
					attentionDao.delete(attention2);
					returnMsg.setList(null);
					returnMsg.setMsg("false");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(appUser.getAttention());
				}
			} catch (Exception e) {
				returnMsg.setMsg("取消失败！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		}

		return returnMsg;
	}

	/**
	 * 判断用户有没有关注这个人
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年5月2日
	 */
	@RequestMapping(value = "/getAttention")
	public ReturnMsg getAttention(@RequestParam("followerId") Long followerId,
			@RequestParam("beFollowedId") Long beFollowedId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Attention> attentions = attentionDao.findByFollowerIdAndBeFollowedId(followerId, beFollowedId);
			if (!attentions.isEmpty()) {
				returnMsg.setList(attentions);
				returnMsg.setMsg("获取成功，已关注！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("未关注！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("获取失败！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id获取自己关注的用户 和 获取关注自己的用户
	 * 
	 * @Description: TODO
	 * @param @param
	 *            followerId
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月28日
	 */
	@RequestMapping(value = "/getColContentByAttention")
	public ReturnMsg getColContentByAttention(@RequestParam("fId") Long fId, Integer tag, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<AppUser> appUsers = null;
			List<User> users = null;
			List<ToolUserInFo> toolUserInFos = new ArrayList<ToolUserInFo>();
			ToolUserInFo toolUserInFo = null;
			if (tag == 1) {
				// 获取的是用户所关注的用户信息
				appUsers = appUserDao.getColContentByFollowerId(fId, page * size, size);
				users = userDao.getColContentByFollowerId(fId, page * size, size);
				if (!appUsers.isEmpty()) {
					for (AppUser appUser : appUsers) {
						toolUserInFo = new ToolUserInFo();
						toolUserInFo.setUserName(appUser.getUserName());
						toolUserInFo.setId(appUser.getId());
						toolUserInFo.setUserImg(appUser.getUserImg());
						toolUserInFo.setJudge(0);
						toolUserInFos.add(toolUserInFo);
					}
				}
				if (!users.isEmpty()) {
					for (User user : users) {
						toolUserInFo = new ToolUserInFo();
						toolUserInFo.setUserName(user.getNickName());
						toolUserInFo.setId(user.getId());
						toolUserInFo.setUserImg(user.getImg());
						toolUserInFo.setJudge(1);
						toolUserInFos.add(toolUserInFo);
					}
				}
				// map.put("userName", userNames);
				// map.put("id", uIds);
				// returnList.add(map);
			} else {
				appUsers = appUserDao.getColContentByBeFollowerId(fId, page * size, size);
				if (!appUsers.isEmpty()) {
					for (AppUser appUser : appUsers) {
						List<Attention> attentions = attentionDao.findByFollowerIdAndBeFollowedId(fId, appUser.getId());
						toolUserInFo = new ToolUserInFo();
						if (!attentions.isEmpty()) {
							// 有数据已关注此粉丝
							toolUserInFo.setFlag(1);
						} else {
							// 没数据没关注此粉丝
							toolUserInFo.setFlag(0);
						}

						toolUserInFo.setUserName(appUser.getUserName());
						toolUserInFo.setId(appUser.getId());
						toolUserInFo.setUserImg(appUser.getUserImg());
						toolUserInFo.setJudge(0);
						toolUserInFos.add(toolUserInFo);
					}
				}
			}

			returnMsg.setList(toolUserInFos);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(toolUserInFos.size());
		} catch (Exception e) {
			returnMsg.setMsg("获取失败！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id获取关注的用户的动态
	 * 
	 * @Description: TODO
	 * @param @param
	 *            page
	 * @param @param
	 *            limit
	 * @param @param
	 *            appUserId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月11日
	 */
	@RequestMapping(value = "/getMyAttentionState")
	public Object getMyAttentionState(Integer page, Integer limit, Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 获取关注的app用户的发布内容
			List<ColumnContent> colCon1 = columnContentDao.getConByAttention1(appUserId, 0, page * limit, limit);
			Integer a = columnContentDao.getConByAttentions(appUserId, 0);
			for (ColumnContent columnContent : colCon1) {
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
				}
				// 判断当前用户有没有点赞此条文章
				List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
						columnContent.getId());
				if (thumbsup.size() > 0) {
					// 存在数据点过赞
					columnContent.setGive(1);
				} else {
					// 不存在数据 没点赞
					columnContent.setGive(0);
				}
				// 获取栏目信息
				Columns column = columnsDao.findById(columnContent.getColId());
				String cName = "";
				if ("0".equals(columnContent.getState()) || "1".equals(columnContent.getState())) {
					cName = "头条";
				} else if ("2".equals(columnContent.getState()) || "3".equals(columnContent.getState())) {
					cName = "发现";
				}
				if (!MyTools.isEmpty(column)) {
					columnContent.setColName(cName + " - " + column.getColName());
				} else {
					columnContent.setColName(cName);
				}
			}
			// 获取关注的后台用户的发布内容
			List<ColumnContent> colCon2 = columnContentDao.getConByAttention3(appUserId, 1, page * limit, limit);
			Integer b = columnContentDao.getConByAttentions2(appUserId, 1);
			for (ColumnContent columnContent : colCon2) {
				User user = userDao.findById(columnContent.getUserId());
				if (!MyTools.isEmpty(user)) {
					columnContent.setUserName(user.getNickName());
					columnContent.setUserImg(user.getImg());
				}
				// 判断当前用户有没有点赞此条文章
				List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
						columnContent.getId());
				if (thumbsup.size() > 0) {
					// 存在数据点过赞
					columnContent.setGive(1);
				} else {
					// 不存在数据 没点赞
					columnContent.setGive(0);
				}
				// 获取栏目信息
				Columns column = columnsDao.findById(columnContent.getColId());
				String cName = "";
				if ("0".equals(columnContent.getState()) || "1".equals(columnContent.getState())) {
					cName = "头条";
				} else if ("2".equals(columnContent.getState()) || "3".equals(columnContent.getState())) {
					cName = "发现";
				}
				if (!MyTools.isEmpty(column)) {
					columnContent.setColName(cName + " - " + column.getColName());
				} else {
					columnContent.setColName(cName);
				}
			}
		
			returnMsg.setList(colCon2);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(a + b);
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("获取失败！");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 添加收藏 参数为用户id appUserId和 内容id columnContentId
	 * 
	 * @Description: TODO
	 * @param @param
	 *            coll�ct
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月28日
	 */
	@RequestMapping(value = "/addCollect")
	public ReturnMsg addCollect(@ModelAttribute Collect collect, Integer tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (tag == 1) {
			// 收藏
			try {
				Collect collect2 = collectDao.findByAppUserIdAndColumnContentId(collect.getAppUserId(),
						collect.getColumnContentId());
				if (MyTools.isEmpty(collect2)) {
					collectDao.save(collect);
					returnMsg.setList(null);
					returnMsg.setMsg("true");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(0);
				} else {
					returnMsg.setList(null);
					returnMsg.setMsg("添加失败，已收藏！");
					returnMsg.setSuccess(false);
					returnMsg.setTotalSize(0);
				}

			} catch (Exception e) {
				returnMsg.setMsg("添加失败！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		} else {
			try {
				Collect collect2 = collectDao.findByAppUserIdAndColumnContentId(collect.getAppUserId(),
						collect.getColumnContentId());
				if (!MyTools.isEmpty(collect2)) {
					collectDao.delete(collect2);
					returnMsg.setList(null);
					returnMsg.setMsg("false");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(0);
				}
			} catch (Exception e) {
				returnMsg.setMsg("取消失败！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		}

		return returnMsg;
	}

	/**
	 * 判断用户有没有收藏此文章 参数为用户id appUserId和 内容id columnContentId
	 * 
	 * @Description: TODO
	 * @param @param
	 *            collect
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年5月9日
	 */
	@RequestMapping(value = "/isCollect")
	public ReturnMsg isCollect(@ModelAttribute Collect collect) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Collect collect2 = collectDao.findByAppUserIdAndColumnContentId(collect.getAppUserId(),
					collect.getColumnContentId());
			if (!MyTools.isEmpty(collect2)) {
				returnMsg.setMsg("获取成功，已收藏！");
				returnMsg.setSuccess(true);
			} else {
				returnMsg.setMsg("未收藏！");
				returnMsg.setSuccess(false);
			}
		} catch (Exception e) {
			returnMsg.setMsg("获取失败！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据用户id获取收藏的内容信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月28日
	 */
	@RequestMapping(value = "/getColContentbyCollect")
	public ReturnMsg getColContentbyCollect(@RequestParam("appUserId") Long appUserId, Integer start, Integer limit) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<ColumnContent> columnContents = columnContentDao.getColContentbyCollect(appUserId, start * limit,
					limit);
			if (!MyTools.isEmpty(columnContents)) {
				for (ColumnContent columnContent : columnContents) {
					AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
					if (!MyTools.isEmpty(appuser)) {
						columnContent.setUserName(appuser.getUserName());
						columnContent.setUserImg(appuser.getUserImg());
					}
					User user = userDao.findById(columnContent.getUserId());
					if (!MyTools.isEmpty(user)) {
						columnContent.setUserName(user.getNickName());
						columnContent.setUserImg(user.getImg());
					}
					// 判断当前用户有没有点赞此条文章
					List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
							columnContent.getId());
					if (thumbsup.size() > 0) {
						// 存在数据点过赞
						columnContent.setGive(1);
					} else {
						// 不存在数据 没点赞
						columnContent.setGive(0);
					}
					// 获取栏目信息
					Columns column = columnsDao.findById(columnContent.getColId());
					String cName = "";
					if ("0".equals(columnContent.getState()) || "1".equals(columnContent.getState())) {
						cName = "头条";
					} else if ("2".equals(columnContent.getState()) || "3".equals(columnContent.getState())) {
						cName = "发现";
					}
					if (!MyTools.isEmpty(column)) {
						columnContent.setColName(cName + " - " + column.getColName());
					} else {
						columnContent.setColName(cName);
					}
				}
			}
			returnMsg.setList(columnContents);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(columnContents.size());
		} catch (Exception e) {
			returnMsg.setMsg("获取失败！");
			returnMsg.setSuccess(false);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 我的发布内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月28日
	 */
	@RequestMapping(value = "/getMyColContent")
	public ReturnMsg getMyColContent(@RequestParam("appUserId") Long appUserId, Integer start, Integer limit,
			Integer judge, Long colId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 排序
			// Sort sort = new Sort(Sort.Direction.DESC, "id");
			// 分页
			PageRequest pageable = new PageRequest(start, limit);
			if (judge == 0) {
				// app用户的发布内容
				List<ColumnContent> columnContents = null;
				if (MyTools.isEmpty(colId)) {
					columnContents = columnContentDao.findByAppUserId(appUserId, pageable);
				} else {
					columnContents = columnContentDao.findByAppUserIdAndColId(appUserId, colId, "2", pageable);
				}

				if (!MyTools.isEmpty(columnContents)) {
					for (ColumnContent columnContent : columnContents) {
						AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
						if (!MyTools.isEmpty(appuser)) {
							columnContent.setUserName(appuser.getUserName());
							columnContent.setUserImg(appuser.getUserImg());
						}
						User user = userDao.findById(columnContent.getUserId());
						if (!MyTools.isEmpty(user)) {
							columnContent.setUserName(user.getNickName());
							columnContent.setUserImg(user.getImg());
						}
						// 判断当前用户有没有点赞此条文章
						List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
								columnContent.getId());
						if (thumbsup.size() > 0) {
							// 存在数据点过赞
							columnContent.setGive(1);
						} else {
							// 不存在数据 没点赞
							columnContent.setGive(0);
						}
						// 获取栏目信息
						Columns column = columnsDao.findById(columnContent.getColId());
						String cName = "";
						if ("0".equals(columnContent.getState()) || "1".equals(columnContent.getState())) {
							cName = "头条";
						} else if ("2".equals(columnContent.getState()) || "3".equals(columnContent.getState())) {
							cName = "发现";
						}
						if (!MyTools.isEmpty(column)) {
							columnContent.setColName(cName + " - " + column.getColName());
						} else {
							columnContent.setColName(cName);
						}
					}

					returnMsg.setList(columnContents);
					returnMsg.setMsg("获取成功！");
					returnMsg.setSuccess(true);
					int count = 0;
					if (MyTools.isEmpty(colId)) {
						count = columnContentDao.findByAppUserId(appUserId).size();
					} else {
						count = columnContentDao.findByAppUserIdAndColId(appUserId, colId, "2");
					}

					returnMsg.setTotalSize(count);
				} else {
					returnMsg.setMsg("获取失败，没有对应的数据！");
					returnMsg.setSuccess(true);
					returnMsg.setList(null);
					returnMsg.setTotalSize(0);
				}
			} else if (judge == 1) {
				// 后台用户的发布
				List<ColumnContent> columnContents2 = columnContentDao.findByUserId(appUserId, pageable);
				if (!columnContents2.isEmpty()) {
					for (ColumnContent columnContent : columnContents2) {
						AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
						if (!MyTools.isEmpty(appuser)) {
							columnContent.setUserName(appuser.getUserName());
							columnContent.setUserImg(appuser.getUserImg());
						}
						User user = userDao.findById(columnContent.getUserId());
						if (!MyTools.isEmpty(user)) {
							columnContent.setUserName(user.getNickName());
							columnContent.setUserImg(user.getImg());
						}
						// 判断当前用户有没有点赞此条文章
						List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
								columnContent.getId());
						if (thumbsup.size() > 0) {
							// 存在数据点过赞
							columnContent.setGive(1);
						} else {
							// 不存在数据 没点赞
							columnContent.setGive(0);
						}
					}
					returnMsg.setList(columnContents2);
					returnMsg.setMsg("获取成功！");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(columnContents2.size());
				}

			}

		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	@RequestMapping(value = "/testMp3")
	public void testMp3() {
		AudioConvert.convert("/usr/local/webapps/KuBianImg/img/1527565113801/yueduaiqing.m4a",
				"/usr/local/webapps/KuBianImg/img/yueduaiqing.mp3", avcodec.AV_CODEC_ID_MP3, 8000, 16, 1);
	}

	/**
	 * 用户在pc端根据栏目获取自己发布的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @param
	 *            colId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/getPcMyFindCon")
	public Object getPcMyFindCon(Integer start, Integer limit, Long colId, HttpServletRequest request, Long userId,
			String search, String state) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			User user = userDao.findById(userId);
			if (!MyTools.isEmpty(user)) {
				AppUser appUser = appUserDao.findById(user.getAuId());
				// 排序
				// 分页
				PageRequest pageable = new PageRequest(start, limit);
				if (!MyTools.isEmpty(search)) {
					// 模糊查询
					List<ColumnContent> coluPage = columnContentDao.getpcDimSearch(user.getAuId(), "%" + search + "%",
							start * limit, limit, state);
					if (coluPage.size() > 0) {
						for (ColumnContent columnContent : coluPage) {
							if (MyTools.isEmpty(user.getNickName())) {
								// 用户昵称为空的时候
								columnContent.setUserName(appUser.getUserName());
							} else {
								columnContent.setUserName(user.getNickName());
							}
							if (MyTools.isEmpty(user.getImg())) {
								// 头像为空
								columnContent.setUserImg(appUser.getUserImg());
							} else {
								columnContent.setUserImg(user.getImg());
							}
						}
						returnMsg.setMsg("获取成功！");
						returnMsg.setSuccess(true);
						returnMsg.setList(coluPage);
						returnMsg.setTotalSize(
								columnContentDao.getpcDimSearch(user.getAuId(), "%" + search + "%", state));
						return returnMsg;
					} else {
						returnMsg.setMsg("没有这条数据！");
						returnMsg.setSuccess(true);
						returnMsg.setList(null);
						returnMsg.setTotalSize(0);
						return returnMsg;
					}

				}
				List<ColumnContent> columnContents = new ArrayList<ColumnContent>();
				if (!MyTools.isEmpty(colId)) {
					columnContents = columnContentDao.pcFindByAppUserIdAndColId(user.getAuId(), colId, state, pageable);
				} else {
					columnContents = columnContentDao.pcFindByAppUserIdAndColId(user.getAuId(), state, pageable);
				}

				for (ColumnContent columnContent : columnContents) {
					if (MyTools.isEmpty(user.getNickName())) {
						// 用户昵称为空的时候
						columnContent.setUserName(appUser.getUserName());
					} else {
						columnContent.setUserName(user.getNickName());
					}
					if (MyTools.isEmpty(user.getImg())) {
						// 头像为空
						columnContent.setUserImg(appUser.getUserImg());
					} else {
						columnContent.setUserImg(user.getImg());
					}

				}
				returnMsg.setList(columnContents);
				returnMsg.setSuccess(true);
				returnMsg.setMsg("获取成功！");
				if (!MyTools.isEmpty(colId)) {
					returnMsg.setTotalSize(columnContentDao.findByAppUserIdAndColId(user.getAuId(), colId, state));
				} else {
					returnMsg.setTotalSize(columnContentDao.findByAppUserIdAndColId(user.getAuId(), state));
				}

			} else {
				returnMsg.setMsg("帐号异常！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
			}

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
	 * pc端登录的app用户根据标题模糊搜索自己发布的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            userId
	 * @param @param
	 *            con
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/pcDimSearch")
	public Object pcDimSearch(Long userId, String con, Integer start, Integer limit) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 排序
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			// 分页
			PageRequest pageable = new PageRequest(start, limit, sort);
			User user = userDao.findById(userId);
			if (!MyTools.isEmpty(user)) {
				List<ColumnContent> coluPage = columnContentDao.getpcDimSearch(user.getAuId(), "%" + con + "%",
						start * limit, limit, "2");
				returnMsg.setMsg("获取成功！");
				returnMsg.setSuccess(true);
				returnMsg.setList(coluPage);
				returnMsg.setTotalSize(columnContentDao.getpcDimSearch(user.getAuId(), "%" + con + "%", "2"));
			} else {
				returnMsg.setMsg("帐号异常！");
				returnMsg.setSuccess(false);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
			}
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}

		return returnMsg;
	}
}
