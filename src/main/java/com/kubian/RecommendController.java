package com.kubian;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.AppUser;
import com.kubian.mode.ColumnContent;
import com.kubian.mode.Recommend;
import com.kubian.mode.RecommendCon;
import com.kubian.mode.Thumbsup;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.ColumnContentDao;
import com.kubian.mode.dao.RecommendConDao;
import com.kubian.mode.dao.RecommendDao;
import com.kubian.mode.dao.ThumbsupDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

/**
 * 推荐操作 ClassName: RecommendController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月22日
 */
@RestController
public class RecommendController {
	@Autowired
	private RecommendConDao recommendConDao;
	@Autowired
	private RecommendDao recommendDao;
	@Autowired
	private ColumnContentDao columnContentDao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private ThumbsupDao thumbsupDao;
	@Value("${recommend.size}")
	private String rsize;

	/**
	 * 添加推荐表
	 * 
	 * @Description: TODO
	 * @param @param
	 *            recommend
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/addRecommend")
	public Object addRecommend(@ModelAttribute Recommend recommend) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (!MyTools.isEmpty(recommend.getId())) {
			// id不为空 是修改
			Recommend recommend2 = recommendDao.findById(recommend.getId());
			try {
				MyTools.updateNotNullFieldForPatient(recommend2, recommend);
				recommendDao.save(recommend2);
				returnMsg.setList(null);
				returnMsg.setMsg("修改成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} catch (Exception e) {
				returnMsg.setList(null);
				returnMsg.setMsg("异常错误！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		} else {
			// id为空是添加
			try {
				recommendDao.save(recommend);
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
		}
		return returnMsg;
	}

	/**
	 * 根据用户id获取对应的推荐列表
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/getRecommend")
	public Object getRecommend(Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Recommend> recommends = recommendDao.findByAppUserId(appUserId);
			returnMsg.setList(recommends);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(recommends.size());
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
	 * 根据推荐表的id删除推荐列表
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/delRecommend")
	public Object delRecommend(Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Recommend recommend = recommendDao.findById(id);
			if (!MyTools.isEmpty(recommend)) {
				recommendDao.delete(recommend);
				returnMsg.setList(null);
				returnMsg.setMsg("删除成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("数据不存在！");
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
	 * 添加推荐详情
	 * 
	 * @Description: TODO
	 * @param @param
	 *            recommendCon
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/addRecommendCon")
	public Object addRecommendCon(String conId, Long rId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "createDate");
			String[] cid = conId.split(",");

			if (cid.length > 0) {
				for (int i = 0; i < cid.length; i++) {
					RecommendCon recommendCon = new RecommendCon();
					recommendCon.setConId(Long.valueOf(cid[i]));
					recommendCon.setrId(rId);
					recommendConDao.save(recommendCon);
					List<RecommendCon> rcons = recommendConDao.findByRId(rId, sort);
					if (rcons != null && rcons.size() > 0) {
						// 删除最先添加的一条
						if (rcons.size() > Integer.parseInt(rsize)) {
							recommendConDao.delete(rcons.get(rcons.size() - 1));
						}
					}
				}

			}

			// // 删除最先添加的一条 再添加
			// recommendConDao.delete(rcons.get(rcons.size() - 1));
			// recommendConDao.save(recommendCon);

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
	 * 根据id删除推荐详情的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/delRecommendCon")
	public Object delRecommendCon(Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			RecommendCon recommendCon = recommendConDao.findById(id);
			if (!MyTools.isEmpty(recommendCon)) {
				recommendConDao.delete(recommendCon);
				returnMsg.setList(null);
				returnMsg.setMsg("删除成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("数据不存在！");
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
	 * 根据推荐列表获取推荐详情
	 * 
	 * @Description: TODO
	 * @param @param
	 *            rId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月22日
	 */
	@RequestMapping(value = "/getRecommendCon")
	public Object getRecommendCon(Long rId, Long appUserId, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<ColumnContent> list = new ArrayList<ColumnContent>();
			AppUser appUser = appUserDao.findById(appUserId);
			Sort sort = new Sort(Sort.Direction.DESC, "pxDate");
			if (MyTools.isEmpty(page) || MyTools.isEmpty(size)) {
				List<RecommendCon> rcons = recommendConDao.findByRId(rId, sort);
				if (rcons != null && rcons.size() > 0) {
					for (RecommendCon recommendCon : rcons) {
						ColumnContent columnContent = columnContentDao.findById1(recommendCon.getConId());
						columnContent.setRcId(recommendCon.getId());
						columnContent.setSltImg(appUser.getSyImg2());
						columnContent.setUserName(appUser.getUserName());
						columnContent.setUserImg(appUser.getUserImg());
						List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
								columnContent.getId());
						if (thumbsup.size() > 0) {
							// 存在数据点过赞
							columnContent.setGive(1);
						} else {
							// 不存在数据 没点赞
							columnContent.setGive(0);
						}
						list.add(columnContent);
					}
				}
				returnMsg.setList(list);
				returnMsg.setMsg("获取成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(rcons.size());
				return returnMsg;
			}
			PageRequest pageable = new PageRequest(page, size, sort);
			Page<RecommendCon> recomendCons = recommendConDao.findByRId(rId, pageable);

			if (recomendCons.getContent() != null && recomendCons.getContent().size() > 0) {
				for (RecommendCon recommendCon : recomendCons) {
					ColumnContent columnContent = columnContentDao.findById1(recommendCon.getConId());
					columnContent.setRcId(recommendCon.getId());
					columnContent.setSltImg(appUser.getSyImg2());
					columnContent.setUserName(appUser.getUserName());
					columnContent.setUserImg(appUser.getUserImg());
					// columnContent.setConHtml(null);
					List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
							columnContent.getId());
					if (thumbsup.size() > 0) {
						// 存在数据点过赞
						columnContent.setGive(1);
					} else {
						// 不存在数据 没点赞
						columnContent.setGive(0);
					}
					list.add(columnContent);
				}
			}

			returnMsg.setList(list);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(recomendCons.getTotalElements());
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
	 * 获取我的发布内容(除开推荐详情里已经存在的内容)
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId
	 * @param @param
	 *            rId
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月25日
	 */
	@RequestMapping(value = "/getMyIssueCon")
	public Object getMyIssueCon(Long appUserId, Long rId, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Sort sort = new Sort(Sort.Direction.DESC, "createDate");

			PageRequest pageable = new PageRequest(page, size);
			// 获取我的发布的内容(状态是显示的内容)
			List<ColumnContent> coluPage = columnContentDao.findByAppUserIdAndState(appUserId, "6", "2", pageable);
			List<RecommendCon> list = recommendConDao.findByRId(rId, sort);
			List<ColumnContent> list2 = new ArrayList<ColumnContent>();
			AppUser appUser = appUserDao.findById(appUserId);
			if (coluPage.size() > 0) {
				for (ColumnContent columnContent : coluPage) {
					columnContent.setSltImg(appUser.getSyImg2());
					columnContent.setUserName(appUser.getUserName());
					columnContent.setUserImg(appUser.getUserImg());
					// columnContent.setConHtml(null);

					List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId,
							columnContent.getId());
					if (thumbsup.size() > 0) {
						// 存在数据点过赞
						columnContent.setGive(1);
					} else {
						// 不存在数据 没点赞
						columnContent.setGive(0);
					}
					list2.add(columnContent);
				}
				Iterator<ColumnContent> it = list2.iterator();
				while (it.hasNext()) {
					ColumnContent x = it.next();
					for (RecommendCon recommendCon : list) {
						long conId = recommendCon.getConId();
						long id = x.getId();
						if (conId == id) {
							it.remove();
						}
					}
				}
				// for (int i = 0; i < list2.size(); i++) {
				// if (list != null && list.size() > 0) {
				// for (RecommendCon recommendCon : list) {
				// long conId = recommendCon.getConId();
				// long id = list2.get(i).getId();
				// if (conId == id) {
				// list2.remove(i);
				// }
				// }
				// }
				// }
			}
			returnMsg.setList(list2);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			int count = columnContentDao.findByAppUserIdAndState(appUserId, "6", "2");
			returnMsg.setTotalSize(count);
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
	 * 置顶推荐详情的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            rmId 推荐详情表的id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月27日
	 */
	@RequestMapping(value = "/pxRecommend")
	public Object pxRecommend(Long rcId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			RecommendCon recommendCon = recommendConDao.findById(rcId);
			if (!MyTools.isEmpty(recommendCon)) {
				// if (recommendCon.getPxDate() != null) {
				// recommendCon.setPxDate(null); // 取消置顶
				// } else {
				recommendCon.setPxDate(new Date()); // 置顶
				// }
				recommendConDao.save(recommendCon);
				returnMsg.setList(null);
				returnMsg.setMsg("操作成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("数据不存在！");
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
}
