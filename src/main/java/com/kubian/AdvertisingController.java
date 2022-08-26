package com.kubian;

import com.kubian.mode.Advertising;
import com.kubian.mode.AdvertisingEx;
import com.kubian.mode.Columns;
import com.kubian.mode.dao.AdvertisingDao;
import com.kubian.mode.dao.AdvertisingExDao;
import com.kubian.mode.dao.ColumnsDao;
import com.kubian.mode.util.ListRange;
import com.kubian.mode.util.MyTools;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 普通广告管理和浏览区广告管理 ClassName:AdvertisingController
 *
 * @author WangW
 * @Description: TODO
 * @date 2018年4月10日
 */
@RestController
public class AdvertisingController {

	@Autowired
	private AdvertisingDao advertisingDao;
	@Autowired
	private AdvertisingExDao advertisingExDao;
	@Autowired
	private ColumnsDao columnDao;

	private ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();

	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;

	/**
	 * 查询所有的普通广告
	 *
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getAllAdvertising")
	@ResponseBody
	public Object getAllAdvertising() {
		List<Advertising> list = advertisingDao.findAll();
		return list;
	}

	/**
	 * 查询所有的普通广告(分页)
	 *
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/getAllAdvertisingByPage")
	@ResponseBody
	public Object getAllAdvertisingByPage(Integer page, Integer limit, String site) {
		if (MyTools.isEmpty(site)) {
			Page<Advertising> list = advertisingDao.findAll(new PageRequest(page, limit));
			return list;
		}
		Page<Advertising> list = advertisingDao.findBySite(site, new PageRequest(page, limit));
		return list;
	}

	/**
	 * 增加普通广告
	 *
	 * @param advertising
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/addAdvertising")
	@ResponseBody
	public Object addAdvertising(@ModelAttribute Advertising advertising,
			@RequestParam("myUpload") MultipartFile multipartFile) {
		ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();
		try {
			String filePath = "";
			if (!MyTools.isEmpty(multipartFile)) {
				if (multipartFile.getSize() > 0) {
					// 获取文件名
					filePath = MyTools.saveFiles(multipartFile, uploadPath, webImgPath);
					if (filePath.equals("图片保存出错")) {
						listRange.setSuccess(false);
						listRange.setMessage("图片保存出错");
						return listRange;
					} else {
						advertising.setImgUrl(filePath);
					}
				}
			}

			if (advertising.getId() != null) {
				Advertising advertising2 = advertisingDao.findById(advertising.getId());
				MyTools.updateNotNullFieldForPatient(advertising2, advertising);
				advertisingDao.save(advertising2);
				listRange.setSuccess(true);
				listRange.setMessage("增加成功!");
				return listRange;
			}
			advertisingDao.save(advertising);
			listRange.setSuccess(true);
			listRange.setMessage("增加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			listRange.setSuccess(false);
			listRange.setMessage("增加失败!");
		}
		return listRange;
	}

	/**
	 * 删除普通广告
	 *
	 * @param advertising
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/delAdvertising")
	@ResponseBody
	public Object delAdvertising(Advertising advertising) {
		ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();
		try {
			advertisingDao.delete(advertising);
			listRange.setSuccess(true);
			listRange.setMessage("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			listRange.setSuccess(false);
			listRange.setMessage("删除失败!");
		}
		return listRange;
	}

	/**
	 * 浏览区广告查询(分页)
	 *
	 * @param page
	 * @param limit
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/queryExNew")
	@ResponseBody
	public Object queryExNew(Integer page, Integer limit, Long colid) {
		ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();
		Page<AdvertisingEx> result = null;
		if (MyTools.isEmpty(colid)) {
			result = advertisingExDao.findAll(new PageRequest(page, limit, Sort.Direction.DESC, "id"));
		} else {
			result = advertisingExDao.findByColid(colid, new PageRequest(page, limit, Sort.Direction.DESC, "id"));
		}

		List<AdvertisingEx> list = new ArrayList<AdvertisingEx>();
		list.addAll(result.getContent());
		for (AdvertisingEx ae : list) {
			Columns column = columnDao.findById(ae.getColid());
			if (!MyTools.isEmpty(column)) {
				ae.setColName(column.getColName());
			}

		}
		listRange.setSuccess(true);
		listRange.setList(list);
		listRange.setTotalSize(result.getTotalElements());
		listRange.setMessage(null);
		return listRange;
	}

	/**
	 * 删除浏览区广告查询
	 *
	 * @param advertisingEx
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/delEx")
	@ResponseBody
	public Object delEx(AdvertisingEx advertisingEx) {
		ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();
		AdvertisingEx advertisingEx1 = advertisingExDao.findById(advertisingEx.getId());
		try {
			advertisingExDao.delete(advertisingEx1);
			listRange.setSuccess(true);
			listRange.setMessage("删除成功!");
			listRange.setList(null);
		} catch (Exception e) {
			e.printStackTrace();
			listRange.setSuccess(false);
			listRange.setMessage("删除失败!");
			listRange.setList(null);
		}
		return listRange;
	}

	/**
	 * 增加浏览区广告查询
	 *
	 * @param advertisingEx
	 * @param multipartFile
	 * @return Object
	 * @throws @Description:
	 *             TODO
	 * @author WangW
	 * @date 2018年4月11日
	 */
	@RequestMapping(value = "/addEx")
	@ResponseBody
	public Object addEx(AdvertisingEx advertisingEx, @RequestParam("myUpload") MultipartFile multipartFile) {
		ListRange<AdvertisingEx> listRange = new ListRange<AdvertisingEx>();
		try {
			String filePath = "";
			if (!MyTools.isEmpty(multipartFile)) {
				if (multipartFile.getSize() > 0) {
					// 判断是否是图片
					filePath = MyTools.saveFiles(multipartFile, uploadPath, webImgPath);
					if (filePath.equals("图片保存出错")) {
						listRange.setSuccess(false);
						listRange.setMessage("图片保存出错");
						listRange.setList(null);
						return listRange;
					} else {
						advertisingEx.setImgUrl(filePath);
					}
				}
			}

			if (advertisingEx.getId() != null) {
				AdvertisingEx advertisingEx2 = advertisingExDao.findById(advertisingEx.getId());
				MyTools.updateNotNullFieldForPatient(advertisingEx2, advertisingEx);
				advertisingExDao.save(advertisingEx2);
				listRange.setSuccess(true);
				listRange.setMessage("增加成功!");
				listRange.setList(null);
				return listRange;
			}
			advertisingExDao.save(advertisingEx);
			listRange.setSuccess(true);
			listRange.setMessage("增加成功!");
			listRange.setList(null);
		} catch (Exception e) {
			e.printStackTrace();
			listRange.setSuccess(false);
			listRange.setMessage("增加失败!");
			listRange.setList(null);
		}
		return listRange;
	}
}
