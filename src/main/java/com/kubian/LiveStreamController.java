package com.kubian;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.LiveStream;
import com.kubian.mode.dao.LiveStreamDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;

/**
 * 直播频道 ClassName: LiveStreamController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年6月4日
 */
@RestController
public class LiveStreamController {
	@Autowired
	private LiveStreamDao liveStreamDao;

	/**
	 * 添加直播频道
	 * 
	 * @Description: TODO
	 * @param @param
	 *            liveStream
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/addLiveStream")
	public Object addLiveStream(@ModelAttribute LiveStream liveStream) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (!MyTools.isEmpty(liveStream.getId())) {
			// 有id 是修改
			LiveStream liveStream2 = liveStreamDao.findById(liveStream.getId());
			try {
				MyTools.updateNotNullFieldForPatient(liveStream2, liveStream);
				liveStreamDao.save(liveStream2);
				returnMsg.setList(null);
				returnMsg.setMsg("修改成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} catch (Exception e) {
				returnMsg.setList(null);
				returnMsg.setMsg("修改失败！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		} else {
			// 没id 是新增
			try {
				liveStreamDao.save(liveStream);
				returnMsg.setList(null);
				returnMsg.setMsg("添加成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} catch (Exception e) {
				returnMsg.setList(null);
				returnMsg.setMsg("添加失败！");
				returnMsg.setSuccess(false);
				returnMsg.setTotalSize(0);
				e.printStackTrace();
			}
		}
		return returnMsg;
	}

	/**
	 * 删除对应的直播频道
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/delLiveStream")
	public Object delLiveStream(Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			LiveStream liveStream = liveStreamDao.findById(id);
			if (!MyTools.isEmpty(liveStream)) {
				liveStreamDao.delete(liveStream);
				returnMsg.setList(null);
				returnMsg.setMsg("删除成功！");
				returnMsg.setSuccess(true);
				returnMsg.setTotalSize(0);
			} else {
				returnMsg.setList(null);
				returnMsg.setMsg("删除失败，数据不存在！");
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
	 * 获取对应用户的直播频道
	 * 
	 * @Description: TODO
	 * @param @param
	 *            page
	 * @param @param
	 *            size
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/getLiveStream")
	public Object getLiveStream(Long appUserId) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			// PageRequest pageable = new PageRequest(page, size);
			List<LiveStream> liveStreams = liveStreamDao.findByAppUserId(appUserId);
			returnMsg.setList(liveStreams);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(liveStreams.size());
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
	 * 根据id获取对应的直播频道
	 * 
	 * @Description: TODO
	 * @param @param
	 *            id
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年6月4日
	 */
	@RequestMapping(value = "/getLiveStreamById")
	public Object getLiveStreamById(Long id) {
		ReturnMsg returnMsg = new ReturnMsg();
		try {
			List<LiveStream> liveStreams = new ArrayList<LiveStream>();
			LiveStream liveStream = liveStreamDao.findById(id);
			liveStreams.add(liveStream);
			returnMsg.setList(liveStreams);
			returnMsg.setMsg("获取成功！");
			returnMsg.setSuccess(true);
			returnMsg.setTotalSize(1);
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
