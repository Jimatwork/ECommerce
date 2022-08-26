package com.kubian;

import com.kubian.mode.Marquee;
import com.kubian.mode.dao.MarqueeDao;
import com.kubian.mode.util.ListRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字幕管理 ClassName:SubtitleController
 * 
 * @Description: TODO
 * @author WangW
 * @date 2018年4月10日
 */
@RestController
public class SubtitleController {

	private static final Logger log = LoggerFactory.getLogger(SubtitleController.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private MarqueeDao marqueeDao;

	private ListRange<Marquee> listRange = new ListRange<Marquee>();

	/**
	 * 查询所有的APP滚动字幕
	 *
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getAllMarquee")
	@ResponseBody
	public Object getAllMarquee() {
		List<Marquee> sub = marqueeDao.findAll();
		return sub;
	}

	/**
	 * 增加APP滚动字幕
	 *
	 * @param marquee
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/addMarquee")
	@ResponseBody
	public Object addMarquee(Marquee marquee) {
		ListRange<Marquee> listRange = new ListRange<Marquee>();
		marqueeDao.save(marquee);
//		System.out.println(marquee.getId() + "_______" + marquee.getContent());
		if (marquee.getId() != null) {
			listRange.setMessage("添加成功!");
			listRange.setSuccess(true);
		} else {
			listRange.setMessage("添加失败!");
			listRange.setSuccess(false);
		}
		return listRange;
	}

	/**
	 * 删除APP滚动字幕
	 *
	 * @param marquee
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/delMarquee")
	@ResponseBody
	public Object delMarquee(Marquee marquee) {
		ListRange<Marquee> listRange = new ListRange<Marquee>();
		try {
			marqueeDao.delete(marquee);
			listRange.setSuccess(true);
			listRange.setMessage("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			listRange.setSuccess(false);
			listRange.setMessage("删除失败!");
		}
		return listRange;
	}
}
