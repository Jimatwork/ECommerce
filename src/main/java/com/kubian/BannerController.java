package com.kubian;

import com.kubian.mode.Banner;
import com.kubian.mode.dao.BannerDao;
import com.kubian.mode.util.ListRange;
import com.kubian.mode.util.MyTools;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Banner管理
 * ClassName:BannerController
 * @Description: TODO
 * @author WangW
 * @date 2018年4月10日
 */
@RestController
public class BannerController {

    @Autowired
    private BannerDao bannerDao;
    private ListRange<Banner> listRange = new ListRange<Banner>();
    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;

    /**
     * 查询所有的Banner
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/getAllBanner")
    @ResponseBody
    public Object getAllBanner() {
        List<Banner> list = bannerDao.findAll();
        return list;
    }

    /**
     * 查询所有的Banner(分页)
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/getAllBannerByPage")
    @ResponseBody
    public Object getAllBannerByPage(Integer page, Integer limit) {
        Page<Banner> result = bannerDao.findAll(new PageRequest(page, limit));
        return result;
    }

    /**
     * 增加Banner
     *
     * @param banner
     * @param multipartFile
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/addBanner")
    @ResponseBody
    public Object addBanner(Banner banner,@RequestParam("myUpload") MultipartFile multipartFile) {
    	ListRange<Banner> listRange = new ListRange<Banner>();
        try {
        	if (!MyTools.isEmpty(multipartFile)) {
        		if (multipartFile.getSize() > 0) {
        			 String FileType = "";
        	            String filePath= MyTools.saveFiles(multipartFile,uploadPath,webImgPath);
        	            if (filePath.equals("图片保存出错")) {
        	                listRange.setSuccess(false);
        	                listRange.setMessage("图片保存出错");
        	                listRange.setList(null);
        	                listRange.setTotalSize(0);
        	                return listRange;
        	            }
        	            banner.setImg(filePath);
        		}
        	}
           if (banner.getId() != null) {
        	   Banner banner2 = bannerDao.findById(banner.getId());
        	   MyTools.updateNotNullFieldForPatient(banner2, banner);
        	   bannerDao.save(banner2);
               listRange.setSuccess(true);
               listRange.setMessage("增加成功!");
               listRange.setList(null);
               listRange.setTotalSize(0);
               return listRange;
           }
            bannerDao.save(banner);
            listRange.setSuccess(true);
            listRange.setMessage("增加成功!");
            listRange.setList(null);
            listRange.setTotalSize(0);
        } catch (Exception e) {
            e.printStackTrace();
            listRange.setSuccess(false);
            listRange.setMessage("增加失败!");
            listRange.setList(null);
            listRange.setTotalSize(0);
        }
        return listRange;
    }

    /**
     * 删除Banner
     *
     * @param banner
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/delBanner")
    @ResponseBody
    public Object delBanner(Banner banner) {
    	ListRange<Banner> listRange = new ListRange<Banner>();
        try {
            bannerDao.delete(banner);
            listRange.setSuccess(true);
            listRange.setMessage("删除成功!");
            listRange.setList(null);
            listRange.setTotalSize(0);
        } catch (Exception e) {
            e.printStackTrace();
            listRange.setSuccess(false);
            listRange.setMessage("删除失败!");
            listRange.setList(null);
            listRange.setTotalSize(0);
        }
        return listRange;
    }
}
