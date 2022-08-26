package com.kubian;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.AppUser;
import com.kubian.mode.ColumnContent;
import com.kubian.mode.Columns;
import com.kubian.mode.Thumbsup;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.ColumnContentDao;
import com.kubian.mode.dao.ColumnsDao;
import com.kubian.mode.dao.ThumbsupDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

/**
 * 视频 ClassName: HotColumnContentController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月19日
 */
@RestController
public class HotColumnContentController {
	@Autowired
	private ColumnContentDao columnContentDao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ThumbsupDao thumbsupDao;
	@Autowired
	private ColumnsDao columnsDao;
	private ReturnMsg returnMsg = new ReturnMsg();

	/**
	 * 分页获取视频的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            start
	 * @param @param
	 *            limit
	 * @param @param
	 *            tag
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月19日
	 */
	@RequestMapping(value = "/getHotColumnContent")
	public ReturnMsg getHotColumnContent(@RequestParam("id") Long id, String search_str, Integer start, Integer limit,
			String tag, Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);
		if (!"5".equals(tag)) {
			tag = "7";
		}
		// 分页操作
		PageRequest pageable = new PageRequest(start, limit);
		if (!MyTools.isEmpty(search_str)) {
			List<ColumnContent> columnContents2 = columnContentDao.getCon("%" + search_str + "%", start * limit, limit,
					tag);

			if (!columnContents2.isEmpty()) {
				for (ColumnContent columnContent : columnContents2) {
					// 查询内容的作者名
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
					if (!MyTools.isEmpty(appUserId)) {
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
					// 所在的栏目
					if (!MyTools.isEmpty(columns)) {
						columnContent.setColName("视频 - " + columns.getColName());
					} else {
						columnContent.setColName("视频");
					}

				}
				
				returnMsg.setList(columnContents2);
				int count = columnContentDao.getCon("%" + search_str + "%", tag);
				returnMsg.setTotalSize(count);
				returnMsg.setSuccess(true);
				return returnMsg;
			} else {
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setSuccess(true);
				returnMsg.setMsg("没有数据！");
				return returnMsg;
			}
		}

		List<ColumnContent> columnContents = columnContentDao.getHotContent(id, tag, pageable);
		if (!columnContents.isEmpty()) {
			// 查询内容的作者名
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
				// 所在的栏目
				if (!MyTools.isEmpty(columns)) {
					columnContent.setColName("视频 - " + columns.getColName());
				} else {
					columnContent.setColName("视频");
				}
			}
		} else {
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("没有数据！");
			return returnMsg;
		}
		returnMsg.setList(columnContents);
		int count = columnContentDao.getHotContent(id, tag);
		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		return returnMsg;
	}

	/**
	 * 获取视频栏目的滚动内容和轮播内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            tag
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月19日
	 */
	@RequestMapping(value = "/getTopHotContent")
	public ReturnMsg getTopHotContent(@RequestParam("topId") Long id, String tag, String hot) {
		ReturnMsg returnMsg = new ReturnMsg();
		String tag1 = "";
		String tag2 = "";
		if (!"5".equals(tag)) {
			tag1 = "0";
			tag2 = "3";
		}
		try {
			List<ColumnContent> columnContents = columnContentDao.getTopHotContent(id, hot);
			// 查询内容的作者名
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
			}
			returnMsg.setList(columnContents);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(columnContents.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 获取视频下面对应栏目的置顶的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            colId
	 * @param @param
	 *            tag
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/getHotConByColId")
	public Object getHotConByColId(Long colId, String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			String tag1 = "";
			String tag2 = "";
			if (!"5".equals(tag)) {
				tag1 = "0";
				tag2 = "3";
			}
			List<ColumnContent> columnContents = columnContentDao.getHotConByColId(colId);
			if (columnContents != null && columnContents.size() > 0) {
				// 查询内容的作者名
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
				}
			}
			returnMsg.setList(columnContents);
			returnMsg.setMsg("获取成功");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(columnContents.size());
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
	 * 分页获取视频的内容 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            start
	 * @param @param
	 *            limit
	 * @param @param
	 *            tag
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月19日
	 */
	@RequestMapping(value = "/getHotColumnContent1")
	public ReturnMsg getHotColumnContent1(@RequestParam("id") Long id, Integer start, Integer limit, String tag,
			Long appUserId, String search_str) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);

		if (!"5".equals(tag)) {
			tag = "0";
		}
		// 分页操作
		PageRequest pageable = new PageRequest(start, limit);

		if (!MyTools.isEmpty(search_str)) {
			List<ColumnContent> columnContents2 = columnContentDao.getCon1("%" + search_str + "%", start * limit, limit,
					Integer.parseInt(tag));

			if (!columnContents2.isEmpty()) {
				for (ColumnContent columnContent : columnContents2) {
					// 查询内容的作者名
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
					if (!MyTools.isEmpty(appUserId)) {
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
					// 所在的栏目
					if (!MyTools.isEmpty(columns)) {
						columnContent.setColName("视频 - " + columns.getColName());
					} else {
						columnContent.setColName("视频");
					}

				}

				returnMsg.setList(columnContents2);
				int count = columnContentDao.getCon1("%" + search_str + "%", Integer.parseInt(tag));
				returnMsg.setTotalSize(count);
				returnMsg.setSuccess(true);
				return returnMsg;
			} else {
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setSuccess(true);
				returnMsg.setMsg("没有数据！");
				return returnMsg;
			}
		}
		List<ColumnContent> columnContents = columnContentDao.getHotContent1(id, Integer.parseInt(tag), pageable);
		if (!columnContents.isEmpty()) {
			// 查询内容的作者名
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
				// 所在的栏目
				if (!MyTools.isEmpty(columns)) {
					columnContent.setColName("视频 - " + columns.getColName());
				} else {
					columnContent.setColName("视频");
				}
			}
		} else {
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("没有数据！");
			return returnMsg;
		}
	
		returnMsg.setList(columnContents);
		int count = columnContentDao.getHotContent1(id, Integer.parseInt(tag));
		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		return returnMsg;
	}
}
