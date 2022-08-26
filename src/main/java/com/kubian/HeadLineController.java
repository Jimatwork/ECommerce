package com.kubian;

import cn.jpush.api.push.PushResult;
import com.kubian.mode.*;
import com.kubian.mode.dao.*;
import com.kubian.mode.util.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 头条 ClassName: HeadLineController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年4月10日
 */
@RestController
public class HeadLineController {
	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;
	@Value("${web.img.path1}")
	private String webImgPath1;
	@Autowired
	private ColumnContentDao columnContentDao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private ColumnsDao columnsDao;
	@Autowired
	private FollowDao followDao;
	@Autowired
	private DraftBoxDao draftBoxDao;
	@Autowired
	private AppClickCountDao appClickCountDao;
	@Autowired
	private MyIncomeDao myIncomeDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ThumbsupDao thumbsupDao;
	@Autowired
	private BackUpColumnContentDao backUpColumnContentDao;
	@Autowired
	private AttentionDao attentionDao;
	@Autowired
	private RecommendConDao recommendConDao;
	private ReturnMsg returnMsg = new ReturnMsg();

	/**
	 * 修改数据库数据接口
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月23日
	 */
	@RequestMapping(value = "/utilCon")
	public Object utilCon() {
		List<ColumnContent> list = columnContentDao.findAll();
		String s = null;
		for (ColumnContent bcc : list) {
			String stes = null;
			try {
				s = bcc.getConHtml();
				for (int i = 0; i < 100; i++) {
					s = s.replaceAll("font-size:" + i + "=", "");
				}
				s = s.replaceAll("font-size:=", "");

				String news = "";
				String[] str = s.split("font-size:");
				for (int i = 0; i < str.length; i++) {
					stes = str[i];
					if (i > 0) {
						String strs = stes.substring(0, stes.indexOf('"'));
						if (strs.indexOf(";") != -1) {
							String f = stes.substring(0, stes.indexOf(";"));
							if (f.indexOf("p") != -1) {
								String a1 = stes.substring(0, stes.indexOf("p"));
								double dd = Double.parseDouble(a1);
								double cs = dd / 16;
								String newStr = "font-size:" + cs + "em";
								StringBuilder sb1 = new StringBuilder(stes);
								sb1.replace(0, stes.indexOf(";"), newStr);
								stes = sb1.toString();
							} else {
								StringBuilder sb1 = new StringBuilder(stes);
								sb1.insert(0, "font-size:");
								stes = sb1.toString();
							}
						} else {
							String f = stes.substring(0, stes.indexOf('"'));
							System.out.println(stes);
							if (f.indexOf("p") != -1) {
								String a1 = stes.substring(0, stes.indexOf("p"));
								double dd = Double.parseDouble(a1);
								double cs = dd / 16;
								String newStr = "font-size:" + cs + "em;";
								StringBuilder sb1 = new StringBuilder(stes);
								sb1.replace(0, stes.indexOf('"'), newStr);
								stes = sb1.toString();
							} else {
								StringBuilder sb1 = new StringBuilder(stes);
								sb1.insert(0, "font-size:");
								stes = sb1.toString();
							}
						}

					}
					news += stes;
				}
				bcc.setConHtml(news);
				columnContentDao.save(bcc);
			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("id" + bcc.getId());
				continue;

			}
		}

		return "";
	}

	/**
	 * 根据内容id获取内容详情
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月27日
	 */
	@RequestMapping(value = "/getColContentById")
	public ReturnMsg getColContentById(@RequestParam("id") Long id, Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<ColumnContent> list = new ArrayList<ColumnContent>();
			ColumnContent columnContent = columnContentDao.findById(id);
			int playCount = columnContent.getPlayCount();
			if (!MyTools.isEmpty(columnContent)) {
				// 查询内容的作者名
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setSltImg(appuser.getSyImg2());
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
				}
				User user = userDao.findById(columnContent.getUserId());
				if (!MyTools.isEmpty(user)) {
					columnContent.setUserName(user.getNickName());
					columnContent.setUserImg(user.getImg());
				}
				if (!MyTools.isEmpty(columnContent)) {
					int num = 0;
					num = new Random().nextInt(10) + 1;
					columnContent.setPlayCount(columnContent.getPlayCount() + num);
					columnContentDao.save(columnContent);

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
				list.add(columnContent);
				returnMsg.setList(list);
				returnMsg.setMsg("获取成功");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(playCount + 1);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("获取失败，没有这条数据！");
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
	 * 随机取5条数据
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月22日
	 */
	@RequestMapping(value = "/getRandom")
	public Object getRandom() {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			int count = columnContentDao.findAll1();
			int num = count / 5;
			Random random = new Random();
			int start = random.nextInt(num);

			List<ColumnContent> con = columnContentDao.findAll1(start * 5, 5);
			for (ColumnContent columnContent : con) {
				// 查询内容的作者名
				AppUser appuser = appUserDao.findById(columnContent.getAppUserId());
				if (!MyTools.isEmpty(appuser)) {
					columnContent.setSltImg(appuser.getSltImg());
					columnContent.setUserName(appuser.getUserName());
					columnContent.setUserImg(appuser.getUserImg());
				}
				User user = userDao.findById(columnContent.getUserId());
				if (!MyTools.isEmpty(user)) {
					columnContent.setUserName(user.getNickName());
					columnContent.setUserImg(user.getImg());
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

			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setContent(con);
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
	 * 根据传过来的栏目id分页获取数据
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/getHeadLines")
	public ReturnMsg getHeadLines(Long id, String search_str, Integer start, Integer limit, String tag,
			Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);
		if (!"5".equals(tag)) {
			tag = "0";
		}
		// 分页操作
		PageRequest pageable = new PageRequest(start, limit);
		if (!MyTools.isEmpty(search_str)) {

			// 模糊查询
			List<ColumnContent> columnContents2 = columnContentDao
					.getColumnContentByAppUserIdOrConTitle("%" + search_str + "%", start * limit, limit, tag);
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
						columnContent.setColName("头条 - " + columns.getColName());
					} else {
						columnContent.setColName("头条");
					}

				}

				returnMsg.setList(columnContents2);
				int count = columnContentDao.getColumnContentAll("%" + search_str + "%", tag);
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
		// if ("5".equals(tag)) {
		if (!MyTools.isEmpty(id)) {
			columnContents = columnContentDao.findByColId1(id, tag, pageable);
		} else {
			columnContents = columnContentDao.getConByState1("0", "1", pageable);
		}
		// } else {
		// if (!MyTools.isEmpty(id)) {
		// columnContents = columnContentDao.findByColId(id, tag, pageable);
		// } else {
		// columnContents = columnContentDao.getConByState("0", "1", pageable);
		// }
		// }

		List<Long> appUserIds = new ArrayList<Long>();
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
					columnContent.setColName("头条 - " + columns.getColName());
				} else {
					columnContent.setColName("头条");
				}
			}
		}
		returnMsg.setList(columnContents);
		int count = 0;
		if (!MyTools.isEmpty(id)) {
			count = columnContentDao.findByColId(id, tag);
		} else {
			count = columnContentDao.getConByState("0", "1");
		}

		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		returnMsg.setMsg(null);
		return returnMsg;
	}

	/**
	 * 文件上传操作
	 * 
	 * @Description: TODO
	 * @param @param
	 *            myUploads
	 * @param @return
	 * @return String
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/uploadFile")
	public Object uploadFile(@RequestParam("file") MultipartFile[] myUploads) {
		List<String> list = new ArrayList<String>();
		String path = "";
		if (!MyTools.isEmpty(myUploads)) {
			File certificate = new File(uploadPath + "/headLineImg");
			if (!certificate.exists()) {
				certificate.mkdirs();
			}
			for (int i = 0; i < myUploads.length; i++) {
				if (myUploads[i].getSize() > 0) {// 有文件上传
					// 文件名称
					String format = MyTools.getFileType(myUploads[i].getOriginalFilename());
					long time = System.currentTimeMillis();
					File files = new File(certificate + File.separator + time + format);
					try {
						FileUtils.copyInputStreamToFile(myUploads[i].getInputStream(), files);
						path = webImgPath + "headLineImg/" + files.getName();
						list.add(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("返回值：" + list.toString());
		return list;
	}

	/**
	 * 添加或修改文章
	 * 
	 * @Description: TODO
	 * @param @param
	 *            coliColumnContent
	 * @param @param
	 *            drugImgs
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/addHeadLineContent")
	public ReturnMsg addHeadLineContent(@ModelAttribute ColumnContent coliColumnContent, HttpSession session, HttpServletRequest request,
			String param) {
		ReturnMsg returnMsg = new ReturnMsg();
		try{
			request.setCharacterEncoding("utf-8");  
			   StringBuilder sb = new StringBuilder();
			   BufferedReader reader = request.getReader();
			   char[] buff = new char[1024];
			   int len;
			    while((len = reader.read(buff)) != -1) {
			       sb.append(buff,0, len);
			       }
			    String content =sb.toString();
			    System.out.println(content);
			if (!MyTools.isEmpty(coliColumnContent.getConImg())) {
				// 获取图片文件的名称
				String suffix1 = coliColumnContent.getConImg().substring(
						coliColumnContent.getConImg().lastIndexOf("/") + 1,
						coliColumnContent.getConImg().lastIndexOf("."));
				if (!"slider".equals(suffix1)) {
					// 按比例缩小图片给移动端使用
					String path = coliColumnContent.getConImg();
					// 获取当前图片的部分路径
					if (path.indexOf("KuBianImg/") != -1) {
						String imgPath = path.substring(path.indexOf("KuBianImg/") + 10, path.lastIndexOf("/"));
						String imgPath1 = uploadPath + imgPath;
						String path1 = path.substring(path.indexOf("KuBianImg/") + 10);
						path = uploadPath + path1;
						String fname = System.currentTimeMillis() + ".jpg";
						File file = new File(path);
						if (file.exists()) {
							ImgUtil.resizeImg(file, imgPath1 + "/" + fname, 200, 200);
							String prImg = webImgPath + imgPath + "/" + fname;
							coliColumnContent.setConImg1(prImg);
						} else {
							coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
						}
						// 按比例缩小

					} else {
						coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
					}

				}

			}
			if (coliColumnContent.getId() != null) {

				// id不为空 是修改
				ColumnContent coliColumnContent2 = columnContentDao.findById(coliColumnContent.getId());
				if (!MyTools.isEmpty(coliColumnContent2)) {
					if (!MyTools.isEmpty(coliColumnContent.getPraiseCount())
							&& coliColumnContent.getPraiseCount() == 0) {
						coliColumnContent.setPraiseCount(coliColumnContent2.getPraiseCount());
					}
					// if (!MyTools.isEmpty(coliColumnContent.gethTop()) &&
					// coliColumnContent.gethTop() == 0) {
					// coliColumnContent.sethTop(coliColumnContent2.gethTop());
					// }
					if (!MyTools.isEmpty(coliColumnContent.getDislikeCount())
							&& coliColumnContent.getDislikeCount() == 0) {
						coliColumnContent.setDislikeCount(coliColumnContent2.getDislikeCount());
					}
					if (!MyTools.isEmpty(coliColumnContent.getPlayCount()) && coliColumnContent.getPlayCount() == 0) {
						coliColumnContent.setPlayCount(coliColumnContent2.getPlayCount());
					}
					if (!MyTools.isEmpty(coliColumnContent.getExaCount()) && coliColumnContent.getExaCount() == 0) {
						coliColumnContent.setExaCount(coliColumnContent2.getExaCount());
					}
					// if (!MyTools.isEmpty(coliColumnContent.getTop()) &&
					// coliColumnContent.getTop() == 0) {
					// coliColumnContent.setTop(coliColumnContent2.getTop());
					// }
					if ("1".equals(coliColumnContent.getState())) {
						coliColumnContent.setState(coliColumnContent2.getState());
					}
					if ("0".equals(coliColumnContent.getHot())) {
						coliColumnContent.setHot(coliColumnContent2.getHot());
					}
					if ("0".equals(coliColumnContent.getFollow())) {
						coliColumnContent.setFollow(coliColumnContent2.getFollow());
					}
					MyTools.updateNotNullFieldForPatient(coliColumnContent2, coliColumnContent);
					coliColumnContent2.setMp3Name(null);
					columnContentDao.save(coliColumnContent2);

					returnMsg.setSuccess(true);
					returnMsg.setList(null);
					returnMsg.setTotalSize(0);
					returnMsg.setMsg("修改成功！");
				}
			} else {
				String suffix = "";
				// id为空 是添加
				// User user = (User) session.getAttribute("wuser");
				// if (!MyTools.isEmpty(user)) {
				// coliColumnContent.setUserId(user.getId());
				// }
				if (MyTools.isEmpty(coliColumnContent.getPraiseCount())) {
					coliColumnContent.setPraiseCount(0);
				}
				// if (MyTools.isEmpty(coliColumnContent.gethTop())) {
				// coliColumnContent.sethTop(0);
				// }
				if (MyTools.isEmpty(coliColumnContent.getDislikeCount())) {
					coliColumnContent.setDislikeCount(0);
				}
				if (MyTools.isEmpty(coliColumnContent.getPlayCount())) {
					coliColumnContent.setPlayCount(0);
				}
				if (MyTools.isEmpty(coliColumnContent.getExaCount())) {
					coliColumnContent.setExaCount(0);
				}
				// if (MyTools.isEmpty(coliColumnContent.getTop())) {
				// coliColumnContent.setTop(0);
				// }
				if (MyTools.isEmpty(coliColumnContent.getState())) {
					coliColumnContent.setState("1");
				}
				if (MyTools.isEmpty(coliColumnContent.getHot())) {
					coliColumnContent.setHot("0");
				}
				if (MyTools.isEmpty(coliColumnContent.getFollow())) {
					coliColumnContent.setFollow("0");
				}
				if (!MyTools.isEmpty(coliColumnContent.getConImg())) {
					// coliColumnContent.setConImg1(coliColumnContent.getConImg());
					// 获取图片文件的名称
					suffix = coliColumnContent.getConImg().substring(coliColumnContent.getConImg().lastIndexOf("/") + 1,
							coliColumnContent.getConImg().lastIndexOf("."));
				}
				if (MyTools.isEmpty(coliColumnContent.getConImg()) || "slider".equals(suffix)) {
					if (!MyTools.isEmpty(coliColumnContent.getAppUserId())) {
						AppUser appuser = appUserDao.findById(coliColumnContent.getAppUserId());
						if (!MyTools.isEmpty(appuser.getSltImg())) {
							coliColumnContent.setConImg(appuser.getSltImg());
							// 获取图片文件的名称
							String suffix1 = coliColumnContent.getConImg().substring(
									coliColumnContent.getConImg().lastIndexOf("/") + 1,
									coliColumnContent.getConImg().lastIndexOf("."));
							if (!"slider".equals(suffix1)) {
								// 按比例缩小图片给移动端使用
								String path = coliColumnContent.getConImg();
								// 获取当前图片的部分路径
								if (path.indexOf("KuBianImg/") != -1) {
									String imgPath = path.substring(path.indexOf("KuBianImg/") + 10,
											path.lastIndexOf("/"));
									String imgPath1 = uploadPath + imgPath;
									String path1 = path.substring(path.indexOf("KuBianImg/") + 10);
									path = uploadPath + path1;
									String fname = System.currentTimeMillis() + ".jpg";
									File file = new File(path);
									if (file.exists()) {
										ImgUtil.resizeImg(file, imgPath1 + "/" + fname, 200, 200);
										String prImg = webImgPath + imgPath + "/" + fname;
										coliColumnContent.setConImg1(prImg);
									} else {
										coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
									}
									// 按比例缩小

								} else {
									coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
								}

							}
						} else {
							coliColumnContent.setConImg(webImgPath1 + "/slider.png");
							coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
						}
					} else {
						coliColumnContent.setConImg(webImgPath1 + "/slider.png");
						coliColumnContent.setConImg1(webImgPath1 + "/slider.png");
					}

				}
				if (coliColumnContent.getConHtml() != null) {
					System.out.println("conHtml长度=" + coliColumnContent.getConHtml().length());
				} else {
					System.out.println("conHtml长度=111");
				}

				columnContentDao.save(coliColumnContent);
				String alias = "";
				// 获取关注我(app用户)的人的id
				if (!MyTools.isEmpty(coliColumnContent.getAppUserId())) {
					List<Attention> attentions = attentionDao
							.findByBeFollowedIdAndJudge(coliColumnContent.getAppUserId(), 0);

					if (attentions != null && attentions.size() > 0) {
						// 有关注我的人
						for (Attention attention : attentions) {
							alias += attention.getFollowerId() + ",";
						}

					}
				}
				// 获取关注我(后台管理用户)的人的id
				if (!MyTools.isEmpty(coliColumnContent.getUserId())) {
					List<Attention> attentions = attentionDao.findByBeFollowedIdAndJudge(coliColumnContent.getUserId(),
							1);

					if (attentions != null && attentions.size() > 0) {
						// 有关注我的人
						for (Attention attention : attentions) {
							alias += attention.getFollowerId() + ",";
						}

					}
				}
				if (!MyTools.isEmpty(alias)) {
					alias = alias.substring(0, alias.lastIndexOf(","));
				}
				System.out.println("alias:" + alias);
				if (!MyTools.isEmpty(alias)) {
					// 推送给关注我的人
					try {
						PushResult result = JpushUtil.sendMsg(alias, coliColumnContent.getConHtml());
					} catch (Exception e) {
						System.out.println("推送异常！");
						e.printStackTrace();
					}
					// System.out.println(result.msg_id);
				}
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("添加成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

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
	@RequestMapping(value = "/getTopContent")
	public ReturnMsg getTopContent(@RequestParam("topId") Long id, String tag, String hot) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (!"5".equals(tag)) {
			tag = "0";
		}
		try {
			List<ColumnContent> columnContents = new ArrayList<ColumnContent>();
			if ("5".equals(tag)) {
				columnContents = columnContentDao.findByColIdAndHot1(id, hot, "1");
			} else {
				columnContents = columnContentDao.findByColIdAndHot(id, hot, "1");
			}

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
			returnMsg.setMsg("获取成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 删除 添加顶部滚动和滚动头条展示的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id 对应的内容的id
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/delTopContent")
	public ReturnMsg delTopContent(@RequestParam("topId") Long id, Integer tag, String flag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			if (tag != null && tag == 5) {
				ColumnContent columnContent = columnContentDao.findById(id);
				if ("2".equals(columnContent.getHot())) {
					// 是滚动头条展示内容 修改为不是滚动头条展示
					columnContent.setHot("0");
					columnContent.setHotDate(null);
				} else if ("0".equals(columnContent.getHot())) {
					// 不是滚动头条展示内容 修改为滚动头条展示
					columnContent.setHot("2");
					columnContent.setHotDate(new Date());
					List<ColumnContent> columnContents = null;
					if ("1".equals(flag)) {
						// 是视频的操作
						columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "2", "6");
					} else if ("2".equals(flag)) {
						// 是发现栏目的操作
						columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "2", "2");
					} else {
						// 是头条栏目的操作
						columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "2", "1");
					}
					if (columnContents.size() > 4) {
						// 最先设置的滚动头条
						ColumnContent columnContent2 = columnContents.get(columnContents.size() - 1);
						if (!MyTools.isEmpty(columnContent2)) {
							columnContent2.setHot("0");
							columnContent2.setHotDate(null);
							columnContentDao.save(columnContent2);
						}
					}

				}
				columnContentDao.save(columnContent);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
				return returnMsg;
			}
			ColumnContent columnContent = columnContentDao.findById(id);
			if ("1".equals(columnContent.getHot())) {
				// 是顶部轮播展示内容 修改为不是顶部展示
				columnContent.setHot("0");
				columnContent.setHotDate(null);
			} else if ("0".equals(columnContent.getHot())) {
				// 不是顶部轮播展示内容 修改为顶部展示
				columnContent.setHot("1");
				columnContent.setHotDate(new Date());
				List<ColumnContent> columnContents = null;
				if ("1".equals(flag)) {
					// 是视频的操作
					columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "1", "6");
				} else if ("2".equals(flag)) {
					// 是发现栏目的操作
					columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "1", "2");
				} else {
					// 是头条栏目的操作
					columnContents = columnContentDao.findByColIdAndHot(columnContent.getColId(), "1", "1");
				}
				if (columnContents.size() > 4) {
					// 最先设置的顶部轮播展示内容
					ColumnContent columnContent2 = columnContents.get(columnContents.size() - 1);
					if (!MyTools.isEmpty(columnContent2)) {
						columnContent2.setHot("0");
						columnContent2.setHotDate(null);
						columnContentDao.save(columnContent2);
					}
				}
			}

			columnContentDao.save(columnContent);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.setSuccess(false);
			returnMsg.setMsg("操作失败！");
		}
		return returnMsg;
	}

	/**
	 * 根据内容id分页查询评论
	 * 
	 * @Description: TODO
	 * @param @param
	 *            coid 内容id
	 * @param @param
	 *            start
	 * @param @param
	 *            limit
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getComments")
	public ReturnMsg getComments(@RequestParam("conId") Long conId, Integer start, Integer limit, Long appUserId,
			String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		ListRange<Comment> cms = new ListRange<Comment>();
		// 分页操作
		try {
			if (!"5".equals(tag)) {
				tag = "1";
			}
			PageRequest pageable = new PageRequest(start, limit);
			List<Comment> comments = commentDao.findByConId(conId, tag, pageable);
			if (appUserId != null) {
				for (Comment comment : comments) {
					Thumbsup thumbsup = thumbsupDao.findByAppUserIdAndCommentId(appUserId, comment.getId());
					if (!MyTools.isEmpty(thumbsup)) {
						// 存在数据点过赞
						comment.setGive(1);
					} else {
						comment.setGive(0);
					}
				}
			}

			returnMsg.setList(comments);
			// comments = commentDao.findByConId(conId);
			int count = commentDao.findByConId(conId, tag);
			returnMsg.setTotalSize(count);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("获取成功！");
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 添加评论
	 * 
	 * @Description: TODO
	 * @param @param
	 *            comment
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/addComments")
	public ReturnMsg addComments(@ModelAttribute Comment comment, HttpServletRequest request) {
		ReturnMsg returnMsg = new ReturnMsg();
		ListRange<Comment> cms = new ListRange<Comment>();
		try {
			String ip = RequestUtil.getIpAddr(request);
			comment.setIp(ip);
			if (MyTools.isEmpty(comment.getUsername())) {
				comment.setUsername("游客");
			}
			commentDao.save(comment);
			// 根据内容id给对应的内容评论数加1
			ColumnContent columnContent = columnContentDao.findById(comment.getConId());
			columnContent.setExaCount(columnContent.getExaCount() + 1);
			columnContentDao.save(columnContent);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 给评论点赞
	 * 
	 * @Description: TODO
	 * @param @param
	 *            appUserId 当前登录用户id
	 * @param @param
	 *            commentId 评论的id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月11日
	 */
	@RequestMapping(value = "/giveComment")
	public Object giveComment(@RequestParam("appUserId") Long appUserId, @RequestParam("id") Long commentId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			if (MyTools.isEmpty(appUserId)) {
				Comment comment = commentDao.findById(commentId);
				comment.setPraiseCount(comment.getPraiseCount() + 1);
				Thumbsup thumbsup = new Thumbsup();
				thumbsup.setAppUserId(appUserId);
				thumbsup.setCommentId(commentId);
				thumbsupDao.save(thumbsup);
				returnMsg.setMsg("true");

				commentDao.save(comment);
				returnMsg.setList(null);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(comment.getPraiseCount());
				return returnMsg;
			}
			Comment comment = commentDao.findById(commentId);
			Thumbsup thumbsup = thumbsupDao.findByAppUserIdAndCommentId(appUserId, commentId);
			if (!MyTools.isEmpty(thumbsup)) {
				// 存在数据 已点赞 取消点赞
				comment.setPraiseCount(comment.getPraiseCount() - 1);
				thumbsupDao.delete(thumbsup);
				returnMsg.setMsg("false");
			} else {
				// 不存在数据 点赞
				comment.setPraiseCount(comment.getPraiseCount() + 1);
				thumbsup = new Thumbsup();
				thumbsup.setAppUserId(appUserId);
				thumbsup.setCommentId(commentId);
				thumbsupDao.save(thumbsup);
				returnMsg.setMsg("true");
			}
			commentDao.save(comment);
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(comment.getPraiseCount());
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据 id 设置评论内容显示还是屏蔽 0 显示 1屏蔽 参数为 id state
	 * 
	 * @Description: TODO
	 * @param @param
	 *            comment
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/setCommentState")
	public ReturnMsg setCommentState(@ModelAttribute Comment comment) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Comment comment2 = commentDao.findById(comment.getId());
			comment2.setState(comment.getState());
			commentDao.save(comment2);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据id删除内容 参数为id
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/delColumnContentById")
	public ReturnMsg delColumnContentById(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent columnContent2 = columnContentDao.findById(columnContent.getId());
			if (!MyTools.isEmpty(columnContent2)) {
				BackUpColumnContent backUpColumnContent = new BackUpColumnContent();
				if (!MyTools.isEmpty(columnContent2.getRedact())) {
					backUpColumnContent.setRedact(columnContent2.getRedact());
				}
				if (!MyTools.isEmpty(columnContent2.getAppUserId())) {
					backUpColumnContent.setAppUserId(columnContent2.getAppUserId());
				}
				if (!MyTools.isEmpty(columnContent2.getBackgroundMusic())) {
					backUpColumnContent.setBackgroundMusic(columnContent2.getBackgroundMusic());
				}
				if (!MyTools.isEmpty(columnContent2.getConHtml())) {
					backUpColumnContent.setConHtml(columnContent2.getConHtml());
				}
				if (!MyTools.isEmpty(columnContent2.getColName())) {
					backUpColumnContent.setColName(columnContent2.getColName());
				}
				if (!MyTools.isEmpty(columnContent2.getConImg())) {
					backUpColumnContent.setConImg(columnContent2.getConImg());
				}
				if (!MyTools.isEmpty(columnContent2.getConImg1())) {
					backUpColumnContent.setConImg1(columnContent2.getConImg1());
				}
				if (!MyTools.isEmpty(columnContent2.getConLivePath())) {
					backUpColumnContent.setConLivePath(columnContent2.getConLivePath());
				}
				if (!MyTools.isEmpty(columnContent2.getConRemark())) {
					backUpColumnContent.setConRemark(columnContent2.getConRemark());
				}
				if (!MyTools.isEmpty(columnContent2.getConTitle())) {
					backUpColumnContent.setConTitle(columnContent2.getConTitle());
				}
				if (!MyTools.isEmpty(columnContent2.getConVideoPath())) {
					backUpColumnContent.setConVideoPath(columnContent2.getConVideoPath());
				}
				if (!MyTools.isEmpty(columnContent2.getCreateDate())) {
					backUpColumnContent.setCreateDate(columnContent2.getCreateDate());
				}
				if (!MyTools.isEmpty(columnContent2.getDislikeCount())) {
					backUpColumnContent.setDislikeCount(columnContent2.getDislikeCount());
				}
				if (!MyTools.isEmpty(columnContent2.getExaCount())) {
					backUpColumnContent.setExaCount(columnContent2.getExaCount());
				}
				if (!MyTools.isEmpty(columnContent2.getFollow())) {
					backUpColumnContent.setFollow(columnContent2.getFollow());
				}
				if (!MyTools.isEmpty(columnContent2.getHot())) {
					backUpColumnContent.setHot(columnContent2.getHot());
				}
				if (!MyTools.isEmpty(columnContent2.getImgUrl())) {
					backUpColumnContent.setImgUrl(columnContent2.getImgUrl());
				}
				if (!MyTools.isEmpty(columnContent2.getIsFurther())) {
					backUpColumnContent.setIsFurther(columnContent2.getIsFurther());
				}
				if (!MyTools.isEmpty(columnContent2.getLinkUrl())) {
					backUpColumnContent.setLinkUrl(columnContent2.getLinkUrl());
				}
				if (!MyTools.isEmpty(columnContent2.getPlayCount())) {
					backUpColumnContent.setPlayCount(columnContent2.getPlayCount());
				}
				if (!MyTools.isEmpty(columnContent2.getPraiseCount())) {
					backUpColumnContent.setPraiseCount(columnContent2.getPraiseCount());
				}
				if (!MyTools.isEmpty(columnContent2.getSort())) {
					backUpColumnContent.setSort(columnContent2.getSort());
				}
				if (!MyTools.isEmpty(columnContent2.getState())) {
					backUpColumnContent.setState(columnContent2.getState());
				}
				if (!MyTools.isEmpty(columnContent2.getUserAdImg())) {
					backUpColumnContent.setUserAdImg(columnContent2.getUserAdImg());
				}
				if (!MyTools.isEmpty(columnContent2.getUserId())) {
					backUpColumnContent.setUserId(columnContent2.getUserId());
				}
				if (!MyTools.isEmpty(columnContent2.getMp3Name())) {
					backUpColumnContent.setMp3Name(columnContent2.getMp3Name());
				}
				if (!MyTools.isEmpty(columnContent2.getTop())) {
					backUpColumnContent.setTop(columnContent2.getTop());
				}
				if (!MyTools.isEmpty(columnContent2.gethTop())) {
					backUpColumnContent.sethTop(columnContent2.gethTop());
				}
				if (!MyTools.isEmpty(columnContent2.gethHot())) {
					backUpColumnContent.sethHot(columnContent2.gethHot());
				}
				backUpColumnContentDao.save(backUpColumnContent);
			}
			columnContentDao.delete(columnContent);
			// 判断推荐详情表里有没有这条数据 有就删除
			List<RecommendCon> rcons = recommendConDao.findByConId(columnContent.getId());
			if (rcons != null && rcons.size() > 0) {
				for (RecommendCon recommendCon : rcons) {
					recommendConDao.delete(recommendCon);
				}
			}
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据id设置内容为隐藏或显示 参数为 id
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/hideContent")
	public ReturnMsg hideContent(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent coliColumnContent2 = columnContentDao.findById(columnContent.getId());
			if (!MyTools.isEmpty(coliColumnContent2)) {
				if ("1".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("0");
				} else if ("0".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("1");
				} else if ("2".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("3");
				} else if ("3".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("2");
				} else if ("6".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("7");
				} else if ("7".equals(coliColumnContent2.getState())) {
					coliColumnContent2.setState("6");
				}
				columnContentDao.save(coliColumnContent2);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}

		return returnMsg;
	}

	/**
	 * 根据id排序 参数 id sort
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/sortContent")
	public ReturnMsg sortContent(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		ColumnContent coliColumnContent2 = columnContentDao.findById(columnContent.getId());
		if (!MyTools.isEmpty(coliColumnContent2)) {
			try {
				MyTools.updateNotNullFieldForPatient(coliColumnContent2, columnContent);
				columnContentDao.save(coliColumnContent2);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			} catch (Exception e) {
				returnMsg.setSuccess(false);
				returnMsg.setMsg("异常错误！");
				e.printStackTrace();
			}
		}
		return returnMsg;
	}

	/**
	 * 设置文章延伸阅读 参数 id
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/setFurther")
	public ReturnMsg setFurther(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent coliColumnContent2 = columnContentDao.findById(columnContent.getId());
			coliColumnContent2.setIsFurther(new Date().getTime());
			columnContentDao.save(coliColumnContent2);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 获取全部栏目
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/getAllColumns")
	public ReturnMsg getAllColumns(Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		Sort sort = new Sort(Sort.Direction.ASC, "sort");
		try {
			System.out.println("---111" + page + size + "---111");
			if (!MyTools.isEmpty(page) && !MyTools.isEmpty(size)) {
				System.out.println("---" + page + size + "---");
				PageRequest pageable = new PageRequest(page, size, sort);
				Page<Columns> pages = columnsDao.findAll(pageable);
				if (!MyTools.isEmpty(pages)) {
					returnMsg.setList(pages.getContent());
					returnMsg.setSuccess(true);
					returnMsg.setMsg("获取成功！");
					returnMsg.setTotalSize(pages.getTotalElements());
				} else {
					returnMsg.setList(null);
					returnMsg.setSuccess(false);
					returnMsg.setMsg("获取失败，数据是空的！");
					returnMsg.setTotalSize(0);
				}
				return returnMsg;
			}
			List<Columns> columns = columnsDao.findAll(sort);
			returnMsg.setList(columns);
			returnMsg.setTotalSize(columns.size());
			returnMsg.setSuccess(true);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据栏目id删除栏目 参数 id
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columns
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/delColumns")
	public ReturnMsg delColumns(@ModelAttribute Columns columns) {
		ReturnMsg returnMsg = new ReturnMsg();
		ListRange<Columns> cs = new ListRange<Columns>();
		try {
			columnsDao.delete(columns);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 添加或修改栏目
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columns
	 * @param @param
	 *            uploadColmn
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/saveColumns")
	public ReturnMsg saveColumns(@ModelAttribute Columns columns, @RequestParam("myUpload") MultipartFile uploadColmn) {
		ReturnMsg returnMsg = new ReturnMsg();
		ListRange<Columns> cs = new ListRange<Columns>();
		if (!uploadColmn.isEmpty()) {
			if (uploadColmn.getSize() > 0) {
				// 有文件上传
				File file = new File(uploadPath + "/columnImg");
				if (!file.exists()) {
					file.mkdirs();
				}
				// 文件名称
				String fname = MyTools.getFileType(uploadColmn.getOriginalFilename());
				Long time = System.currentTimeMillis();
				File files = new File(file + File.separator + time + fname);
				try {
					FileUtils.copyInputStreamToFile(uploadColmn.getInputStream(), files);
					columns.setColIcon(webImgPath + "columnImg/" + files.getName());
				} catch (IOException e) {
					returnMsg.setSuccess(false);
					returnMsg.setMsg("异常错误！");
					e.printStackTrace();
				}
			}

		}
		if (columns.getId() != null) {
			// id不为空 是修改
			Columns columns2 = columnsDao.findById(columns.getId());
			try {
				MyTools.updateNotNullFieldForPatient(columns2, columns);
				columnsDao.save(columns2);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			} catch (Exception e) {
				returnMsg.setSuccess(false);
				returnMsg.setMsg("异常错误！");
				e.printStackTrace();
			}
		} else {
			// id为空 是添加
			columnsDao.save(columns);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		}

		return returnMsg;
	}

	/**
	 * 根据栏目的 colLink 获取栏目信息
	 * 
	 * @Description: TODO
	 * @param @param
	 *            colLink
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/getColumnByTypes")
	public ReturnMsg getColumnByTypes(@RequestParam("colLink") String colLink) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Columns> cloumns = columnsDao.findByColLink(colLink);
			returnMsg.setList(cloumns);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	// /**
	// * 取消关注 参数是 用户id userId 文章id conId
	// *
	// * @Description: TODO
	// * @param @param
	// * follow
	// * @param @return
	// * @return ListRange<?>
	// * @throws @author
	// * HD
	// * @date 2018年4月12日
	// */
	// @RequestMapping(value = "/closeFollow")
	// public ReturnMsg closeFollow(@ModelAttribute Follow follow) {
	// try {
	// Follow follow2 = followDao.findByUserIdAndConId(follow.getUserId(),
	// follow.getConId());
	// followDao.delete(follow2);
	// returnMsg.setSuccess(true);
	// returnMsg.setList(null);
	// returnMsg.setTotalSize(0);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return returnMsg;
	// }
	/**
	 * 添加 或 取消 关注 用户id userId 文章id conId 添加的时候还要 type参数
	 * 
	 * @Description: TODO
	 * @param @param
	 *            follow
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/addFollow")
	public ReturnMsg addFollow(@ModelAttribute Follow follow) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Follow follow2 = followDao.findByUserIdAndConId(follow.getUserId(), follow.getConId());
			if (MyTools.isEmpty(follow2)) {
				followDao.save(follow);
				returnMsg.setList(null);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			} else {
				followDao.delete(follow2);
				returnMsg.setList(null);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 查询一条数据
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            draftId
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/queryOne")
	public Object queryOne(@RequestParam("id") Long id, @RequestParam("draftId") String draftId) {

		if (!draftId.isEmpty()) {
			DraftBox draftBox = draftBoxDao.findById(id);
			return draftBox;
		}
		ColumnContent columnContent = columnContentDao.findById(id);
		return columnContent;
	}

	/**
	 * 给评论添加作者回复 参数 评论的id 作者的回复 replyContent
	 * 
	 * @Description: TODO
	 * @param @param
	 *            comment
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/addReply")
	public ReturnMsg addReply(@ModelAttribute Comment comment) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Comment comment2 = commentDao.findById(comment.getId());
			if (!MyTools.isEmpty(comment2)) {
				MyTools.updateNotNullFieldForPatient(comment2, comment);
			}
			commentDao.save(comment2);
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 浏览次数
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/appClickCount")
	public ReturnMsg appClickCount(@RequestParam("columnContentId") Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent columnContent = columnContentDao.findById(id);
			int num = 0;
			if ("1".equals(columnContent.getState()) || "2".equals(columnContent.getState())) {
				num = new Random().nextInt(10) + 1;
				columnContent.setPlayCount(columnContent.getPlayCount() + num);
				columnContentDao.save(columnContent);
			}

			// int num = new Random().nextInt(20 - 6) + 6;
			AppClickCount acc = new AppClickCount();
			acc.setColId(id);
			acc.setClickcount(num);
			appClickCountDao.save(acc);
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 点赞
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id 内容id
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/addPraiseCount")
	public ReturnMsg addPraiseCount(@RequestParam("id") Long id, @RequestParam("appUserId") Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			if (MyTools.isEmpty(appUserId)) {
				ColumnContent columnContent = columnContentDao.findById(id);
				columnContent.setPraiseCount(columnContent.getPraiseCount() + 1);
				Thumbsup thumbsup = new Thumbsup();
				thumbsup.setAppUserId(appUserId);
				thumbsup.setColumnContentId(id);
				thumbsupDao.save(thumbsup);
				returnMsg.setMsg("false");
				columnContentDao.save(columnContent);
				returnMsg.setList(null);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(columnContent.getPraiseCount());
				return returnMsg;
			}
			ColumnContent columnContent = columnContentDao.findById(id);
			List<Thumbsup> thumbsup2 = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId, id);
			if (thumbsup2.size() > 0) {
				// 存在数据 点过赞
				// 取消点赞
				if (columnContent.getPraiseCount() > 0) {
					columnContent.setPraiseCount(columnContent.getPraiseCount() - 1);
				}

				// Thumbsup thumbsup =
				// thumbsupDao.findByAppUserIdAndColumnContentId(appUserId, id);
				// if (!MyTools.isEmpty(thumbsup)) {
				thumbsupDao.delete(thumbsup2);
				// }
				returnMsg.setMsg("true");
			} else {
				// 点赞
				columnContent.setPraiseCount(columnContent.getPraiseCount() + 1);
				Thumbsup thumbsup = new Thumbsup();
				thumbsup.setAppUserId(appUserId);
				thumbsup.setColumnContentId(id);
				thumbsupDao.save(thumbsup);
				returnMsg.setMsg("false");
			}
			columnContentDao.save(columnContent);
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(columnContent.getPraiseCount());
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 判断用户是否点赞这篇文章
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年5月2日
	 */
	@RequestMapping(value = "/isLike")
	public ReturnMsg isLike(@RequestParam("columnContentId") Long id, @RequestParam("appUserId") Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Thumbsup> thumbsup = thumbsupDao.findByAppUserIdAndColumnContentId(appUserId, id);
			if (thumbsup.size() > 0) {
				returnMsg.setList(null);
				returnMsg.setMsg("获取成功！");
				returnMsg.setSuccess(true);
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
	 * 获取客户端ip
	 * 
	 * @Description: TODO
	 * @param @param
	 *            request
	 * @param @return
	 * @return String
	 * @throws @author
	 *             HD
	 * @date 2018年4月16日
	 */
	public String getRequestIp(HttpServletRequest request) {
		return RequestUtil.getIpAddr(request);
	}

	/**
	 * 获取所有的被打赏的数据
	 * 
	 * @Description: TODO
	 * @param @param
	 *            myIncome
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月16日
	 */
	@RequestMapping(value = "/getAllMyIncome")
	public ReturnMsg getAllMyIncome(@ModelAttribute MyIncome myIncome, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 排序
			Sort sort = new Sort(Sort.Direction.DESC, "dsUid");
			// 分页
			PageRequest pageable = new PageRequest(page, size, sort);
			List<MyIncome> myIncomes = myIncomeDao.findAll(pageable);
			returnMsg.setList(myIncomes);
			myIncomes = myIncomeDao.findAll();
			returnMsg.setTotalSize(myIncomes.size());
			returnMsg.setSuccess(true);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据dsUid获取为的打赏数据
	 * 
	 * @Description: TODO
	 * @param @param
	 *            dsUid
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月16日
	 */
	@RequestMapping(value = "/getMyIncomeByDsUid")
	public ReturnMsg getMyIncomeByDsUid(@RequestParam("dsUid") String dsUid, Integer page, Integer size) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 排序
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			// 分页
			PageRequest pageable = new PageRequest(page, size, sort);
			List<MyIncome> myIncomes = myIncomeDao.findByDsUid(dsUid, pageable);
			returnMsg.setList(myIncomes);
			myIncomes = myIncomeDao.findByDsUid(dsUid);
			returnMsg.setTotalSize(myIncomes.size());
			returnMsg.setSuccess(true);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 微信支付成功
	 * 
	 * @Description: TODO
	 * @param @param
	 *            myIncome
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月16日
	 */
	@RequestMapping(value = "/weixinAjaxPaySucc")
	public ReturnMsg weixinAjaxPaySucc(@ModelAttribute MyIncome myIncome) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent cc = columnContentDao.findById(Long.valueOf(myIncome.getDsConId()));
			if (cc.getAppUserId() != null && !"".equals(cc.getAppUserId())) {
				AppUser au = appUserDao.findById(cc.getAppUserId());
				myIncome.setDsUid(au.getId() + "");
				myIncome.setDsUname(au.getUserName());
			}
			myIncomeDao.save(myIncome);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (NumberFormatException e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 获取当前登录用户发布的内容列表
	 * 
	 * @Description: TODO
	 * @param @param
	 *            colid
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月16日
	 */
	@RequestMapping(value = "/getAppUserPublish")
	public ReturnMsg getAppUserPublish(@RequestParam("colId") Long colId, Integer page, Integer size,
			HttpSession session) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// 排序
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			// 分页
			PageRequest pageable = new PageRequest(page, size, sort);
			User us = (User) session.getAttribute("wuser");
			if (!MyTools.isEmpty(us)) {
				List<ColumnContent> columnContents = columnContentDao.findByAppUserIdAndColId(us.getAuId(), colId, "2",
						pageable);
				for (ColumnContent columnContent : columnContents) {
					AppUser u = appUserDao.findById(columnContent.getAppUserId());
					if (u != null) {
						columnContent.setUserName(u.getUserName());
						columnContent.setUserImg(u.getUserImg());
					}
					User user = userDao.findById(columnContent.getUserId());
					if (!MyTools.isEmpty(user)) {
						columnContent.setUserName(user.getNickName());
						columnContent.setUserImg(user.getImg());
					}
				}
				returnMsg.setList(columnContents);
				int count = columnContentDao.findByAppUserIdAndColId(us.getAuId(), colId, "2");
				returnMsg.setTotalSize(count);
				returnMsg.setSuccess(true);
				returnMsg.setMsg("操作成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}

		return returnMsg;
	}

	/**
	 * 增加TTS 文字到语音的转换
	 * 
	 * @Description: TODO
	 * @param @param
	 *            str 文字内容
	 * @param @param
	 *            conid 文章id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月11日
	 */
	@RequestMapping(value = "/getTtsMp3")
	public Object getTtsMp3(@RequestParam("str") String str, @RequestParam("conid") Long conid) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent con = columnContentDao.findById(conid);
			String mp3name = System.currentTimeMillis() + ".mp3";
			if (con.getMp3Name() == null) {
				String mp3url = uploadPath + "/mp3";
				File file = new File(mp3url);
				if (!file.exists()) {
					file.mkdirs();
				}
				TtsToMp3.toMp3(str, mp3url + File.separator + mp3name);
				String mp3Path = webImgPath + "/mp3/" + mp3name;
				con.setMp3Name(mp3Path);
				columnContentDao.save(con);
				returnMsg.setList(null);
				returnMsg.setMsg(mp3Path);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				mp3name = con.getMp3Name();
				returnMsg.setList(null);
				returnMsg.setMsg(mp3name);
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 模糊搜索所有内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            search_str
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月17日
	 */
	@RequestMapping(value = "/fuzzySearchCon")
	public Object fuzzySearchCon(@RequestParam("search_str") String search_str, Integer start, Integer limit,
			String tag, Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			String tag1 = "";
			String tag2 = "";
			String tag3 = "";
			if (!"5".equals(tag)) {
				tag1 = "0";
				tag2 = "3";
				tag3 = "7";
			}
			// 分页操作
			PageRequest pageable = new PageRequest(start, limit);
			if (!MyTools.isEmpty(search_str)) {

				// 模糊查询
				List<ColumnContent> columnContents2 = columnContentDao.fuzzySearchCon("%" + search_str + "%",
						start * limit, limit, tag1, tag2, tag3);
				// System.out.println(columnContents2.size() + "start:" + start
				// + "limit:" + limit);
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
						// 获取栏目信息
						Columns column = columnsDao.findById(columnContent.getColId());
						String cName = "";
						if ("0".equals(columnContent.getState()) || "1".equals(columnContent.getState())) {
							cName = "头条";
						} else if ("2".equals(columnContent.getState()) || "3".equals(columnContent.getState())) {
							cName = "发现";
						}
						if (!MyTools.isEmpty(column)) {
							columnContent.setColName(cName + "," + column.getColName());
						} else {
							columnContent.setColName(cName);
						}
					}
					returnMsg.setList(columnContents2);
					int count = columnContentDao.fuzzySearchCon("%" + search_str + "%", tag1, tag2, tag3);
					returnMsg.setTotalSize(count);
					returnMsg.setSuccess(true);
					return returnMsg;
				} else {
					returnMsg.setList(null);
					returnMsg.setMsg("获取失败，不存在这条数据！");
					returnMsg.setSuccess(true);
					returnMsg.setTotalSize(0);
				}
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据栏目id获取栏目详情
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年5月25日
	 */
	@RequestMapping(value = "/getColumnsById")
	public Object getColumnsById(@RequestParam("id") Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<Columns> list = new ArrayList<Columns>();
			Columns columns = columnsDao.findById(id);
			if (!MyTools.isEmpty(columns)) {
				list.add(columns);
				returnMsg.setList(list);
				returnMsg.setMsg("获取成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(1);
			} else {
				returnMsg.setList(list);
				returnMsg.setMsg("获取失败，不存在这条数据！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(1);
			}
		} catch (Exception e) {
			returnMsg.setList(null);
			returnMsg.setMsg("异常错误");
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 删除编辑器内容为空的接口
	 * 
	 * @Description: TODO
	 * @param @param
	 *            a
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月1日
	 */
	@RequestMapping(value = "/updateMysql")
	public Object updateMysql(String a) {
		ReturnMsg returnMsg = new ReturnMsg();
		ColumnContent columnContent = new ColumnContent();
		if (!MyTools.isEmpty(a)) {
			String[] aa = a.split(",");
			for (String string : aa) {
				System.out.println(aa.length + "------" + string);
				columnContent.setId(Long.valueOf(string));
				columnContentDao.delete(columnContent);
			}

			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("获取成功！");
		} else {
			returnMsg.setList(null);
			returnMsg.setSuccess(false);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("参数不能为空！");
		}

		return returnMsg;
	}

	/**
	 * 删除编辑器内容为空的接口
	 * 
	 * @Description: TODO
	 * @param @param
	 *            a
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月1日
	 */
	@RequestMapping(value = "/findConAll")
	public Object findConAll() {
		ReturnMsg returnMsg = new ReturnMsg();
		// PageRequest pageable = new PageRequest(0, 10);
		List<ColumnContent> columnContents = columnContentDao.findAll();
		returnMsg.setList(columnContents);
		returnMsg.setSuccess(true);
		returnMsg.setTotalSize(columnContents.size());
		returnMsg.setMsg("获取成功！");
		return returnMsg;
	}

	/**
	 * 置顶
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            top
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/setTopById")
	public Object setTopById(Long id, Integer top1, Long colId, String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent columnContent = columnContentDao.findById(id);
			if ("5".equals(tag)) {
				// 是视频下面的操作
				if (top1 == 1) {
					// 取消置顶
					columnContent.sethTop(null);
				} else if (top1 == 0) {
					// 置顶

					List<ColumnContent> columnContents = columnContentDao.findByTopAndColId(colId, "1", "2");
					if (columnContents.size() > 4) {
						ColumnContent columnContent2 = columnContents.get(columnContents.size() - 1);
						columnContent2.sethTop(null);
						columnContentDao.save(columnContent2);
					}

					columnContent.sethTop(new Date());
				}
				columnContentDao.save(columnContent);
				returnMsg.setList(null);
				returnMsg.setMsg("设置成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
				return returnMsg;
			}
			if (top1 == 1) {
				// 取消置顶
				columnContent.setTop(null);
			} else if (top1 == 0) {
				// 置顶
				String state = "";
				if ("1".equals(tag)) {
					// 是头条操作
					state = "1";
				} else if ("2".equals(tag)) {
					// 是发现下面的操作
					state = "2";
				}
				List<ColumnContent> columnContents = columnContentDao.findColIdAndState(colId, state);
				if (columnContents.size() > 4) {
					ColumnContent columnContent2 = columnContents.get(columnContents.size() - 1);
					columnContent2.setTop(null);
					columnContentDao.save(columnContent2);
					// returnMsg.setList(null);
					// returnMsg.setSuccess(false);
					// returnMsg.setTotalSize(0);
					// returnMsg.setMsg("只能置顶三条内容！");
					// return returnMsg;
				}

				columnContent.setTop(new Date());
			}
			columnContentDao.save(columnContent);
			returnMsg.setList(null);
			returnMsg.setMsg("设置成功！");
			returnMsg.setSuccess(true);
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
	 * 获取头条下面对应栏目的置顶的内容
	 * 
	 * @Description: TODO
	 * @param @param
	 *            colId
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/getHeadConByTop")
	public Object getHeadConByTop(Long colId, String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			if (!"5".equals(tag)) {
				tag = "0";
			}
			List<ColumnContent> columnContents = columnContentDao.getHeadConByColId(colId, tag);
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
	 * 修改有标题和摘要有问题的内容
	 * 
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws @author
	 *             HD
	 * @date 2018年6月5日
	 */
	@RequestMapping(value = "/setConTitleAndRemark")
	public void setConTitleAndRemark() {
		List<ColumnContent> columnContents = columnContentDao.setConTitleAndRemark();
		List<ColumnContent> columnContents2 = new ArrayList<ColumnContent>();
		columnContents2 = columnContentDao.setConTitleAndRemark2();
		if (!MyTools.isEmpty(columnContents)) {
			for (ColumnContent columnContent : columnContents) {
				columnContentDao.save(columnContent);
				String str = columnContent.getConRemark().trim();
				String[] atrarr = str.split("<");
				String str1 = "";
				if (atrarr.length > 1) {
					str1 = atrarr[0];
				}
				columnContent.setConRemark(str1);
				columnContentDao.save(columnContent);
			}
		}
		if (!MyTools.isEmpty(columnContents2)) {
			for (ColumnContent columnContent : columnContents2) {
				String str = columnContent.getConTitle().trim();
				String[] atrarr = str.split("<");
				String str1 = "";
				if (atrarr.length > 1) {
					str1 = atrarr[0];
				}
				columnContent.setConTitle(str1);
				columnContentDao.save(columnContent);
			}
		}

	}

	/**
	 * 根据传过来的栏目id分页获取数据 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月10日
	 */
	@RequestMapping(value = "/getHeadLines1")
	public ReturnMsg getHeadLines1(@RequestParam("id") Long id, String search_str, Integer start, Integer limit,
			String tag, Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		Columns columns = columnsDao.findById(id);
		if (!"5".equals(tag)) {
			tag = "0";
		}
		// 分页操作
		System.out.println("start:" + start);
		System.out.println("limit:" + limit);
		PageRequest pageable = new PageRequest(start, limit);

		if (!MyTools.isEmpty(search_str)) {
			if (start == 0) {
				start = start + 1;
			}
			// 模糊查询
			List<ColumnContent> columnContents2 = columnContentDao.getColumnContentByAppUserIdOrConTitle(
					"%" + search_str + "%", (start - 1) * limit, limit, Integer.parseInt(tag));
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
						columnContent.setColName("热门 - " + columns.getColName());
					} else {
						columnContent.setColName("热门");
					}

				}

				returnMsg.setList(columnContents2);
				int count = columnContentDao.getColumnContentAll("%" + search_str + "%", Integer.parseInt(tag));
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
		List<ColumnContent> columnContents = columnContentDao.findByColId(id, Integer.parseInt(tag), pageable);
		List<Long> appUserIds = new ArrayList<Long>();
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
					columnContent.setColName("热门 - " + columns.getColName());
				} else {
					columnContent.setColName("热门");
				}
			}
		}

		returnMsg.setList(columnContents);
		int count = columnContentDao.findByColId(id, Integer.parseInt(tag));
		returnMsg.setTotalSize(count);
		returnMsg.setSuccess(true);
		returnMsg.setMsg(null);
		return returnMsg;
	}

	/**
	 * 根据 id 设置评论内容显示还是屏蔽 0 显示 1屏蔽 参数为 id state 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            comment
	 * @param @return
	 * @return ReturnMsg
	 * @throws @author
	 *             HD
	 * @date 2018年4月13日
	 */
	@RequestMapping(value = "/setCommentState1")
	public ReturnMsg setCommentState1(@ModelAttribute Comment comment) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			Comment comment2 = commentDao.findById(comment.getId());
			comment2.setBih(comment.getBih());
			commentDao.save(comment2);
			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据内容id分页查询评论 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            coid 内容id
	 * @param @param
	 *            start
	 * @param @param
	 *            limit
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getComments1")
	public ReturnMsg getComments1(@RequestParam("conId") Long conId, Integer start, Integer limit, Long appUserId,
			String tag) {
		ReturnMsg returnMsg = new ReturnMsg();
		ListRange<Comment> cms = new ListRange<Comment>();
		// 分页操作
		try {
			if (!"5".equals(tag)) {
				tag = "0";
			}
			PageRequest pageable = new PageRequest(start, limit);
			List<Comment> comments = commentDao.findByConId(conId, Integer.parseInt(tag), pageable);
			if (appUserId != null) {
				for (Comment comment : comments) {
					Thumbsup thumbsup = thumbsupDao.findByAppUserIdAndCommentId(appUserId, comment.getId());
					if (!MyTools.isEmpty(thumbsup)) {
						// 存在数据点过赞
						comment.setGive(1);
					} else {
						comment.setGive(0);
					}
				}
			}

			returnMsg.setList(comments);
			// comments = commentDao.findByConId(conId);
			int count = commentDao.findByConId(conId, Integer.parseInt(tag));
			returnMsg.setTotalSize(count);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("获取成功！");
		} catch (Exception e) {
			returnMsg.setMsg("异常错误！");
			returnMsg.setSuccess(false);
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据id把内容的bih设为2 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/delColumnContentById1")
	public ReturnMsg delColumnContentById1(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent columnContent2 = columnContentDao.findById(columnContent.getId());
			if (!MyTools.isEmpty(columnContent2)) {
				columnContent2.setBih(2);
				columnContentDao.save(columnContent2);
			}

			returnMsg.setSuccess(true);
			returnMsg.setList(null);
			returnMsg.setTotalSize(0);
			returnMsg.setMsg("操作成功！");
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 根据id设置内容为隐藏或显示 参数为 id bih 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            columnContent
	 * @param @return
	 * @return ListRange<?>
	 * @throws @author
	 *             HD
	 * @date 2018年4月12日
	 */
	@RequestMapping(value = "/hideContent1")
	public ReturnMsg hideContent1(@ModelAttribute ColumnContent columnContent) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			ColumnContent coliColumnContent2 = columnContentDao.findById(columnContent.getId());
			if (!MyTools.isEmpty(coliColumnContent2)) {
				if (coliColumnContent2.getBih() == 0) {
					coliColumnContent2.setBih(1);
				} else if (coliColumnContent2.getBih() == 1) {
					coliColumnContent2.setBih(0);
				}
				columnContentDao.save(coliColumnContent2);
				returnMsg.setSuccess(true);
				returnMsg.setList(null);
				returnMsg.setTotalSize(0);
				returnMsg.setMsg("操作成功！");
			}
		} catch (Exception e) {
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			e.printStackTrace();
		}

		return returnMsg;
	}

	@RequestMapping(value = "/testPush")
	public Object testPush(Long id, String name) {
		ReturnMsg returnMsg = new ReturnMsg();
		List<String> list = new ArrayList<String>();
		list.add("1223");
		returnMsg.setList(list);
		returnMsg.setSuccess(true);
		returnMsg.setMsg(id + name);
		returnMsg.setTotalSize(0);

		List<ColumnContent> ccs = new ArrayList<ColumnContent>();
		ColumnContent cc = columnContentDao.findById(5l);
		ccs.add(cc);
		ColumnContent cccc = columnContentDao.findById(50l);
		ccs.add(cccc);
		return ccs;
	}

	/**
	 * 根据id获取数据 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            ids
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月11日
	 */
	@RequestMapping(value = "/getPushCon")
	public Object getPushCon(String ids) {
		List<ColumnContent> list = new ArrayList<ColumnContent>();
		if (!MyTools.isEmpty(ids)) {
			String[] id = ids.split(",");

			if (id != null && id.length > 0) {
				for (String string : id) {
					ColumnContent columnContent = columnContentDao.getConById(Long.valueOf(string));
					if (!MyTools.isEmpty(columnContent)) {
						list.add(columnContent);
					}
				}
			}

		}
		return list;
	}

	/**
	 * 添加内容的阅读量 给外部项目调用的
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @param
	 *            playCount
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月13日
	 */
	@RequestMapping(value = "/setConPlayCount")
	public Object setConPlayCount(Long id, Integer playCount) {
		ColumnContent columnContent = columnContentDao.findById(id);
		if (!MyTools.isEmpty(columnContent)) {
			Integer count = columnContent.getPlayCount();
			if (count != null) {
				int num = count;
				num = num + playCount;
				columnContent.setPlayCount(num);
				columnContentDao.save(columnContent);
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	@RequestMapping(value = "/download")
	public void download(HttpServletResponse res) {
		String fileName = "UPEditorEdge.exe";
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File("E://"
					+ fileName)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("success");
	}
	public void dlown(HttpServletResponse response , String fileName){
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		byte [] bytes = new byte[1012];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(new File("E://")));
			os = response.getOutputStream();
			int i = bis.read(bytes);
			while (i != -1) {
				os.write(bytes , 0 , bytes.length);
				os.flush();
				i = bis.read(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
