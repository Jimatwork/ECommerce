package com.kubian;

import com.kubian.mode.BannerService;
import com.kubian.mode.dao.BannerServiceDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ResultVO;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:BannerServiceController
 *
 * @author WangW
 * @Description: 首页Banner管理
 * @date 2018年8月30日
 */
@RestController
public class BannerServiceController implements Serializable {

    @Autowired
    private BannerServiceDao bannerServiceDao;
    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;

    /**
     * 增加Banner
     *
     * @param banner
     * @param image
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/addBannerService")
    @ResponseBody
    public Object addBannerService(BannerService banner, MultipartFile image) {
        try {
            if (image != null) {
                String filePath = MyTools.saveFiles(image, uploadPath, webImgPath);
                banner.setImg(filePath);
            }
            //增加
            if (banner.getId() == null) {
                bannerServiceDao.save(banner);
            } else {
                BannerService bannerService = bannerServiceDao.findById(banner.getId());
                MyTools.updateNotNullField(bannerService, banner);
                bannerServiceDao.save(bannerService);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除Banner
     *
     * @param id
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/delBannerService")
    @ResponseBody
    public Object delBannerService(Long id) {
        try {
            bannerServiceDao.delete(bannerServiceDao.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 获取首页Banner
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/getBannerService")
    @ResponseBody
    public Object getBannerService() {
        List<BannerService> list = bannerServiceDao.findByType(2);
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setList(list);
        returnMsg.setSuccess(true);
        returnMsg.setTotalSize(list.size());
        return returnMsg;
    }

    /**
     * 根据店铺ID查找banner
     *
     * @param bid
     * @return Object
     */
    @RequestMapping(value = "/findBannerByBid")
    @ResponseBody
    public Object findBannerByBid(Long bid) {
        List<BannerService> list = bannerServiceDao.findByTypeAndLinkId(1, bid);
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setSuccess(true);
        returnMsg.setTotalSize(list.size());
        returnMsg.setList(list);
        return returnMsg;
    }
}
