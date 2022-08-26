package com.kubian;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.BackUpColumnContent;
import com.kubian.mode.ColumnContent;
import com.kubian.mode.dao.BackUpColumnContentDao;
import com.kubian.mode.dao.ColumnContentDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

/**
 * 备份内容
 * ClassName: BackUpColumnContentController 
 * @Description: TODO
 * @author HD
 * @date 2018年5月4日
 */
@RestController
public class BackUpColumnContentController {
	@Autowired
	private BackUpColumnContentDao backUpColumnContentDao;
	private ReturnMsg returnMsg = new ReturnMsg();
	
	/**
	 * 根据用户id获取用户的备份内容
	 * @Description: TODO
	 * @param @param appUserId
	 * @param @param userId
	 * @param @param page
	 * @param @param limit
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author HD
	 * @date 2018年5月10日
	 */
	@RequestMapping(value = "/getBackUpColumnContent")
	public Object getBackUpColumnContent(Long appUserId , Long userId,Integer page,Integer limit) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			PageRequest pageable = new PageRequest(page, limit);
			if (!MyTools.isEmpty(appUserId)) {
				Page<BackUpColumnContent> backUpColumnContents = backUpColumnContentDao.findByAppUserId(appUserId,pageable);
				return backUpColumnContents;
			}
			if (!MyTools.isEmpty(userId)) {
				Page<BackUpColumnContent> backUpColumnContents = backUpColumnContentDao.findByAppUserId(userId,pageable);
				return backUpColumnContents;
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
	 * 删除对应的备份内容  tag=5时候 删除对应用户的全部备份内容 参数为id(内容id)  appUserId(app用户id) userId (后台用户id)
	 * @Description: TODO
	 * @param @param backUpConId
	 * @param @param appUserId
	 * @param @param tag
	 * @param @return   
	 * @return Object  
	 * @throws
	 * @author HD
	 * @date 2018年5月10日
	 */
	@RequestMapping(value = "/delBackUpCon")
	public Object delBackUpCon(String id ){
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			BackUpColumnContent backUpColumnContent = new BackUpColumnContent();
			String [] id1 = id.split(",");
			for (int i = 0; i < id1.length; i++) {
				Long lo = Long.parseLong(id1[i]);
					backUpColumnContent.setId(lo);
					// 根据内容id删除内容
					backUpColumnContentDao.delete(backUpColumnContent);
			}
			returnMsg.setList(null);
			returnMsg.setSuccess(true);
			returnMsg.setMsg("删除成功！");
			returnMsg.setTotalSize(0);
		} catch (NumberFormatException e) {
			returnMsg.setList(null);
			returnMsg.setSuccess(false);
			returnMsg.setMsg("异常错误！");
			returnMsg.setTotalSize(0);
			e.printStackTrace();
		}
		return returnMsg;
	}
}
