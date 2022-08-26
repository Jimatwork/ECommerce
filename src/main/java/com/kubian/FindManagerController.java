package com.kubian;

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
import com.kubian.mode.util.ListRange;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 发现管理 ClassName:FindManagerController
 *
 * @author WangW
 * @Description: TODO
 * @date 2018年4月12日
 */
@RestController
public class FindManagerController {

	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private ColumnContentDao columnContentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ThumbsupDao thumbsupDao;
	@Autowired
	private ColumnsDao columnsDao;
	@PersistenceContext
	private EntityManager em;

	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;
	private ReturnMsg returnMsg = new ReturnMsg();

	// /**
	// * 发现栏目
	// *
	// * @param page
	// * @param limit
	// * @param id
	// * @param search
	// * @param search_str
	// * @param state
	// * @return Object
	// * @throws
	// * @Description: TODO
	// * @author WangW
	// * @date 2018年4月12日
	// */
	// @RequestMapping(value = "/queryFindLisAltert")
	// @ResponseBody
	// public Object queryFindLisAltert(Integer page, Integer limit, Integer id,
	// String search, String search_str, String state) {
	// try {
	// // 查询内容
	// if (search != null && !"".equals(search)) {
	// ListRange<ColumnContent> list = new ListRange<ColumnContent>();
	// String sql = "from AppUser where userName like '%" + search_str + "%' and
	// state='0'";
	// Query query1 = em.createQuery(sql);
	// List<AppUser> aus = query1.getResultList();
	// String aussql = "";
	// if (!aus.isEmpty()) {
	// int i = 0;
	// for (AppUser au : aus) {
	// if (i == 0) {
	// aussql += "appUserId=" + au.getId();
	// } else {
	// aussql += " or appUserId=" + au.getId();
	// }
	// i++;
	// }
	// aussql += " or ";
	// }
	// String state1 = state != null ? " and state='1'" : "";
	// String sql1 = "from ColumnContent where " + aussql + " conTitle like '%"
	// + search_str + "%' " + state1
	// + " and (isPublic is null or isPublic!=0) order by id DESC";
	// System.out.println(sql1);
	// Query query2 = em.createQuery(sql1);
	// List<ColumnContent> cct = query2.getResultList();
	// list.setSuccess(true);
	// list.setList(cct);
	// list.setTotalSize((em.createQuery(sql)).getResultList().size());
	// return list;
	// }
	// ListRange<ColumnContent> list = new ListRange<ColumnContent>();
	// List<Columns> clo = (em.createQuery("from Columns where id=" +
	// id)).getResultList();
	// String sql = "from ColumnContent where (colId=" + id;
	// for (int i = 0; i < clo.size(); i++) {
	// sql += " or colId=" + clo.get(i).getId();
	// }
	// sql += ") and appUserId !=null and state=2 order by id DESC";
	// List<ColumnContent> clos =
	// (em.createQuery(sql).setFirstResult(page).setMaxResults(limit)).getResultList();
	// for (int i = 0; i < clos.size(); i++) {
	// AppUser u = appUserDao.findById(clos.get(i).getAppUserId());
	// if (u != null) {
	// clos.get(i).setUserName(u.getUserName());
	// }
	// List col1 = (em.createQuery("from Columns where id=" +
	// clos.get(i).getColId())).getResultList();
	// Columns col = (Columns) col1.get(0);
	// if (col != null) {
	// if (col.getId() != null) {
	// clos.get(i).setColId(col.getId());
	// }
	// }
	// }
	// list.setSuccess(true);
	// list.setList(clos);
	// list.setTotalSize((em.createQuery(sql)).getResultList().size());
	// return list;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	/**
	 * 获取所有信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            search_str
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
	 * @date 2018年4月24日
	 */
	@RequestMapping(value = "/queryFindLisAltert")
	public ReturnMsg queryFindLisAltert(Long id, String search_str, Integer start, Integer limit, String tag,
			Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);
		if (!"5".equals(tag)) {
			tag = "3";
		}
		// 分页操作
		PageRequest pageable = new PageRequest(start, limit);
		if (!MyTools.isEmpty(search_str)) {
			if (start == 0) {
				start = start + 1;
			}
			// 模糊查询
			List<ColumnContent> columnContents2 = columnContentDao.getFindColumnContent("%" + search_str + "%",
					(start - 1) * limit, limit, tag);
			if (!columnContents2.isEmpty()) {
				// 查询内容的作者名
				for (ColumnContent columnContent : columnContents2) {
					columnContent.setGive(0);
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
						columnContent.setColName("发现 - " + columns.getColName());
					} else {
						columnContent.setColName("发现");
					}
				}
				
				returnMsg.setList(columnContents2);
				int count = columnContentDao.getFindColumnContentAll("%" + search_str + "%", tag);
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
		List<ColumnContent> columnContents = null;

		if (!MyTools.isEmpty(id)) {
			columnContents = columnContentDao.getFindManagers(id, tag, pageable);
		} else {
			columnContents = columnContentDao.getFindManager("2", tag, pageable);
		}
		if (!columnContents.isEmpty()) {
			// 查询内容的作者名
			for (ColumnContent columnContent : columnContents) {
				// System.out.println("内容id：" + columnContent.getId());
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
				}
				// System.out.println("1111111111111111111111111111111111111");
				User user = userDao.findById(columnContent.getUserId());
				if (!MyTools.isEmpty(user)) {
					columnContent.setUserName(user.getNickName());
					columnContent.setUserImg(user.getImg());
				}
				// System.out.println("22222222222222222222222222222222");
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
					columnContent.setColName("发现 - " + columns.getColName());
				} else {
					columnContent.setColName("发现");
				}
			}
		}
		
		returnMsg.setList(columnContents);
		int count = 0;
		if (!MyTools.isEmpty(id)) {
			count = columnContentDao.getFindManagers(id, tag);
		} else {
			count = columnContentDao.getFindManager("2", tag);
		}
		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		return returnMsg;
	}

	// /**
	// * 顶部展示 查询
	// *
	// * @param id
	// * @param types
	// * @return Object
	// * @throws
	// * @Description: TODO
	// * @author WangW
	// * @date 2018年4月12日
	// */
	// @RequestMapping(value = "/queryHotContent")
	// @ResponseBody
	// public Object queryHotContent(Long id, String types) {
	// List<ColumnContent> con = null;
	// try {
	// List<Columns> clo = (em.createQuery("from Columns where id=" +
	// id)).getResultList();
	// String sql = "from ColumnContent where (colId=" + id;
	// for (int i = 0; i < clo.size(); i++) {
	// sql += " or colId=" + clo.get(i).getId();
	// }
	// sql += ") and state=2 and hot=1 order by sort DESC,id DESC";
	// if (types.equals("2")) {
	// sql = "from ColumnContent where (colId=" + id;
	// for (int i = 0; i < clo.size(); i++) {
	// sql += " or colId=" + clo.get(i).getId();
	// }
	// sql += ") and state=1 and hot=1 order by id DESC";
	// }
	// if (types.equals("3")) {
	// sql = "from ColumnContent where (colId=" + id;
	// for (int i = 0; i < clo.size(); i++) {
	// sql += " or colId=" + clo.get(i).getId();
	// }
	// sql += ") and (state=1 or state=2) and hot=1 order by playCount DESC";
	// }
	// con = (em.createQuery(sql)).getResultList();
	// for (int i = 0; i < con.size(); i++) {
	// AppUser u = appUserDao
	// .findById(con.get(i).getAppUserId());
	// if (u != null) {
	// con.get(i).setUserName(u.getUserName());
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return con;
	// }
	/**
	 * 获取顶部和滚动头条的展示内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id 栏目的id
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/queryHotContent")
	public ReturnMsg queryHotContent(@RequestParam("topId") Long id, String tag, String hot) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (!"5".equals("tag")) {
			tag = "3";
		}
		try {
			List<ColumnContent> columnContents = columnContentDao.getFindTopContent(id, hot);
			// 查询内容的作者名
			for (ColumnContent columnContent : columnContents) {
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
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
	 * 推送
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月19日
	 */
	@RequestMapping(value = "/pushFindContent")
	public ReturnMsg pushFindContent(@ModelAttribute ColumnContent columnContent1) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent columnContent = columnContentDao.findById(columnContent1.getId());
			if (!MyTools.isEmpty(columnContent)) {
				// if (!MyTools.isEmpty(columnContent1.getPraiseCount()) &&
				// columnContent1.getPraiseCount() == 0) {
				// columnContent1.setPraiseCount(columnContent.getPraiseCount());
				// }
				// // if (!MyTools.isEmpty(coliColumnContent.gethTop()) &&
				// // coliColumnContent.gethTop() == 0) {
				// // coliColumnContent.sethTop(coliColumnContent2.gethTop());
				// // }
				// if (!MyTools.isEmpty(columnContent1.getDislikeCount()) &&
				// columnContent1.getDislikeCount() == 0) {
				// columnContent1.setDislikeCount(columnContent.getDislikeCount());
				// }
				// if (!MyTools.isEmpty(columnContent1.getPlayCount()) &&
				// columnContent1.getPlayCount() == 0) {
				// columnContent1.setPlayCount(columnContent.getPlayCount());
				// }
				// if (!MyTools.isEmpty(columnContent1.getExaCount()) &&
				// columnContent1.getExaCount() == 0) {
				// columnContent1.setExaCount(columnContent.getExaCount());
				// }
				// // if (!MyTools.isEmpty(coliColumnContent.getTop()) &&
				// // coliColumnContent.getTop() == 0) {
				// // coliColumnContent.setTop(coliColumnContent2.getTop());
				// // }
				// if ("1".equals(columnContent1.getState())) {
				// columnContent1.setState(columnContent.getState());
				// }
				// if ("0".equals(columnContent1.getHot())) {
				// columnContent1.setHot(columnContent.getHot());
				// }
				// if ("0".equals(columnContent1.getFollow())) {
				// columnContent1.setFollow(columnContent.getFollow());
				// }
				// ColumnContent columnContent3 = new ColumnContent();
				// if ("2".equals(columnContent.getState())) {
				// columnContent1.setState("1");
				// } else if ("3".equals(columnContent.getState())) {
				// columnContent1.setState("0");
				// }
				//
				// try {
				// columnContent1.setCreateDate(columnContent.getCreateDate());
				// MyTools.updateNotNullFieldForPatient(columnContent3,
				// columnContent);
				// MyTools.updateNotNullFieldForPatient(columnContent3,
				// columnContent1);
				//// columnContent3.setState(columnContent1.getState());
				//
				// if (!MyTools.isEmpty(myUploads)) {
				// File certificate = new File(uploadPath + "/img");
				// if (!certificate.exists()) {
				// certificate.mkdirs();
				// }
				// if (myUploads.getSize() > 0) {// 有文件上传
				// // 文件名称
				// String format =
				// MyTools.getFileType(myUploads.getOriginalFilename());
				// long time = System.currentTimeMillis();
				// File files = new File(certificate + File.separator + time +
				// format);
				// try {
				// FileUtils.copyInputStreamToFile(myUploads.getInputStream(),
				// files);
				// columnContent3.setConImg(webImgPath + "img/" +
				// files.getName());
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// }
				// } catch (Exception e) {
				//
				// e.printStackTrace();
				// }
				columnContent.setIsPush(columnContent1.getIsPush()); // 设为推送的数据所属的栏目id
				columnContent.setBackupConTitle(columnContent1.getBackupConTitle());// 推送的数据的标题
				columnContentDao.save(columnContent);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
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
	 * 内容推送1
	 *
	 * @param id
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/getColumnByLevelNull")
	@ResponseBody
	public Object getColumnByLevelNull(Integer id) {
		if (id == null) {
			List<Columns> cols = null;
			return cols;
		} else {
			List<Columns> cols = (em.createQuery("from Columns where cid=" + id)).getResultList();
			return cols;
		}
	}

	/**
	 * 内容推送2
	 *
	 * @param content
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/push")
	@ResponseBody
	public Object push(ColumnContent content, MultipartFile multipartFile) {
		ListRange listRange = new ListRange();
		try {
			// 获取文件名
			String filePath = MyTools.saveFiles(multipartFile, uploadPath, webImgPath);
			if (filePath.equals("图片保存出错")) {
				listRange.setSuccess(false);
				listRange.setMessage("图片保存出错");
				return listRange;
			}
			content.setConImg(filePath);
			ColumnContent con = (ColumnContent) (em.createQuery("from ColumnContent where colId=" + content.getColId()))
					.getResultList().get(0);
			if (!con.getColId().equals(content.getColId())) {
				con.setColId(content.getColId());
			}
			if (!con.getConTitle().equals(content.getConTitle())) {
				con.setConTitle(content.getConTitle());
			}
			if (!con.getConRemark().equals(content.getConRemark())) {
				con.setConRemark(content.getConRemark());
			}
			con.setState("2"); // 状态改为2
			columnContentDao.save(con);
			listRange.setMessage("推送成功！");
			listRange.setSuccess(true);
			return listRange;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取我的隐藏的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id 用户id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月1日
	 */
	@RequestMapping(value = "/getMyHideCon")
	public Object getMyHideCon(@RequestParam("id") Long id, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		List<ColumnContent> cons = null;
		try {
			PageRequest pageable = new PageRequest(page, size);
			cons = columnContentDao.findByAppUserIdAndState(id, "7", "3", pageable);
			for (ColumnContent columnContent : cons) {
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

			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
			return returnMsg;
		}
		returnMsg.setList(cons);
		returnMsg.setSuccess(true);
		returnMsg.setMsg("获取成功！");
		int count = columnContentDao.findByAppUserIdAndState(id, "7", "3");
		returnMsg.setTotalSize(count);
		return returnMsg;
	}

	/**
	 * 获取发现下面对应栏目的置顶的内容
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
	@RequestMapping(value = "/getFindConByColId")
	public Object getFindConByColId(Long colId, String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			if (!"5".equals(tag)) {
				tag = "3";
			}
			List<ColumnContent> columnContents = columnContentDao.getFindConByColId(colId, tag);
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
	 * 获取所有信息 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            search_str
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
	 * @date 2018年4月24日
	 */
	@RequestMapping(value = "/queryFindLisAltert1")
	public ReturnMsg queryFindLisAltert1(Long id, String search_str, Integer start, Integer limit, String tag,
			Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);
		if (!"5".equals("tag")) {
			tag = "0";
		}
		// 分页操作
		PageRequest pageable = new PageRequest(start, limit);
		if (!MyTools.isEmpty(search_str)) {
			if (start == 0) {
				start = start + 1;
			}
			// 模糊查询
			List<ColumnContent> columnContents2 = columnContentDao.getFindColumnContent("%" + search_str + "%",
					(start - 1) * limit, limit, Integer.parseInt(tag));
			if (!columnContents2.isEmpty()) {
				// 查询内容的作者名
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
						columnContent.setColName("发现 - " + columns.getColName());
					} else {
						columnContent.setColName("发现");
					}
				}
		
				returnMsg.setList(columnContents2);
				int count = columnContentDao.getFindColumnContentAll("%" + search_str + "%", Integer.parseInt(tag));
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

		List<ColumnContent> columnContents = null;

		if (!MyTools.isEmpty(id)) {
			columnContents = columnContentDao.getFindManagers(id, Integer.parseInt(tag), pageable);
		} else {
			columnContents = columnContentDao.getFindManager(Integer.parseInt(tag), pageable);
		}
		if (!columnContents.isEmpty()) {
			// 查询内容的作者名
			for (ColumnContent columnContent : columnContents) {
				// System.out.println("内容id：" + columnContent.getId());
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
				}
				// System.out.println("1111111111111111111111111111111111111");
				User user = userDao.findById(columnContent.getUserId());
				if (!MyTools.isEmpty(user)) {
					columnContent.setUserName(user.getNickName());
					columnContent.setUserImg(user.getImg());
				}
				// System.out.println("22222222222222222222222222222222");
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
					columnContent.setColName("各地 - " + columns.getColName());
				} else {
					columnContent.setColName("各地");
				}
			}
		}
		returnMsg.setList(columnContents);
		int count = 0;
		if (!MyTools.isEmpty(id)) {
			count = columnContentDao.getFindManagers(id, Integer.parseInt(tag));
		} else {
			count = columnContentDao.getFindManager(Integer.parseInt(tag));
		}

		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		return returnMsg;
	}
}
