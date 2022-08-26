package com.kubian;

import com.kubian.mode.*;
import com.kubian.mode.dao.BusinessClassifyDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ResultVO;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName:BusinessClassifyController
 *
 * @author WangW
 * @Description: 店铺分类管理
 * @date 2018年10月17日
 */
@RestController
public class BusinessClassifyController {

    @Autowired
    private BusinessClassifyDao businessClassifyDao;

    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;

    /**
     * 增加Banner
     *
     * @param businessClassify
     * @param image
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年4月11日
     */
    @RequestMapping(value = "/addBusinessClassify")
    @ResponseBody
    public Object addBusinessClassify(BusinessClassify businessClassify, MultipartFile image) {
        try {
            if (image != null) {
                String filePath = MyTools.saveFiles(image, uploadPath, webImgPath);
                businessClassify.setImg(filePath);
            }
            //增加
            if (businessClassify.getId() == null) {
                businessClassifyDao.save(businessClassify);
            } else {
                BusinessClassify businessClassify1 = businessClassifyDao.findById(businessClassify.getId());
                MyTools.updateNotNullField(businessClassify1, businessClassify);
                businessClassifyDao.save(businessClassify1);
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
    @RequestMapping(value = "/delBusinessClassify")
    @ResponseBody
    public Object delBusinessClassify(Long id) {
        try {
            businessClassifyDao.delete(businessClassifyDao.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 分页获取店铺分类
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/getBusinessClassify")
    public Object getBusinessClassify(Integer page, Integer limit) {
        Page<BusinessClassify> businessClassifies = businessClassifyDao.findAll(new PageRequest(page - 1, limit));
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setSuccess(true);
        returnMsg.setList(businessClassifies.getContent());
        returnMsg.setTotalSize(businessClassifies.getTotalPages());
        return returnMsg;
    }

    /**
     * 分页获取显示的店铺分类
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/getshowBusinessClassify")
    public Object getshowBusinessClassify(Integer page, Integer limit) {
        Page<BusinessClassify> businessClassifies = businessClassifyDao.findByState(new PageRequest(page - 1, limit), 0);
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setSuccess(true);
        returnMsg.setList(businessClassifies.getContent());
        returnMsg.setTotalSize(businessClassifies.getTotalPages());
        return returnMsg;
    }
}
