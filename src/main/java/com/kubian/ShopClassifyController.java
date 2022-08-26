package com.kubian;

import com.kubian.mode.ShopClassify;
import com.kubian.mode.ShopClassifySon;
import com.kubian.mode.dao.ShopClassifyDao;
import com.kubian.mode.dao.ShopClassifySonDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 商品一级分类
 * @author HD
 * @Title: ShopClassifyController
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/10/16/01615:45
 */
@RestController
public class ShopClassifyController {
    @Autowired
    private ShopClassifyDao shopClassifyDao;
    @Autowired
    private ShopClassifySonDao shopClassifySonDao;

    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;
    /**
     * 后台分页获取所有的一级分类
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/getShopClassify")
    public Object getShopClassify(Integer page , Integer limit){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (MyTools.isEmpty(page) && MyTools.isEmpty(limit)) {
                List<ShopClassify> shopClassifies = shopClassifyDao.findAll();
                if (null != shopClassifies) {
                    for ( ShopClassify shopClassify:shopClassifies) {
                        List<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(shopClassify.getId());
                        if (null != shopClassifySons) {
                            shopClassify.setShopClassifySons(shopClassifySons);
                        }
                    }
                    returnMsg.setList(shopClassifies);
                    returnMsg.setTotalSize(shopClassifies.size());
                    returnMsg.setSuccess(true);
                    returnMsg.setMsg("获取成功！");
                } else {
                    returnMsg.setSuccess(true);
                    returnMsg.setMsg("数据为空！");
                }
                return returnMsg;
            }
            Sort sort = new Sort(Sort.Direction.ASC,"sequence");
            page = page - 1 ;
            Pageable pageable = new PageRequest(page  , limit , sort);
            Page<ShopClassify> shopClassifies = shopClassifyDao.findAll(pageable);
            if (null != shopClassifies.getContent()) {
                for ( ShopClassify shopClassify:shopClassifies.getContent()) {
                    List<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(shopClassify.getId());
                    if (null != shopClassifySons) {
                        shopClassify.setShopClassifySons(shopClassifySons);
                    }
                }
                returnMsg.setList(shopClassifies.getContent());
                returnMsg.setTotalSize(shopClassifies.getTotalElements());
                returnMsg.setSuccess(true);
                returnMsg.setMsg("获取成功！");
            } else {
                returnMsg.setSuccess(true);
                returnMsg.setMsg("数据为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * app分页获取所有一级分类
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/getShopClassifyApp")
    public Object getShopClassifyApp(Integer page , Integer limit){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.ASC,"sequence");
            Pageable pageable = new PageRequest(page , limit , sort);
            Page<ShopClassify> shopClassifies = shopClassifyDao.findByState( 0 , pageable);
            if (null != shopClassifies.getContent()) {
                for ( ShopClassify shopClassify:shopClassifies.getContent()) {
                    List<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(shopClassify.getId());
                    if (null != shopClassifySons) {
                        shopClassify.setShopClassifySons(shopClassifySons);
                    }
                }
                returnMsg.setList(shopClassifies.getContent());
                returnMsg.setTotalSize(shopClassifies.getTotalElements());
                returnMsg.setSuccess(true);
                returnMsg.setMsg("获取成功！");
            } else {
                returnMsg.setSuccess(true);
                returnMsg.setMsg("数据为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 添加修改分类
     * @param shopClassify
     * @return
     */
    @RequestMapping(value = "/updateShopClassify")
    public Object updateShopClassify(ShopClassify shopClassify , MultipartFile images){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (null != shopClassify.getId()) {
                // id 不为空是修改
                ShopClassify shopClassify1 = shopClassifyDao.findById(shopClassify.getId());
                if (!MyTools.isEmpty(shopClassify1)) {
                    try {
                        if (null != images) {
                            String imagesPath = MyTools.saveFiles(images, uploadPath, webImgPath);
                            shopClassify.setImg(imagesPath);
                        }
                        MyTools.updateNotNullFieldForPatient(shopClassify1,shopClassify);
                        shopClassifyDao.save(shopClassify1);
                        returnMsg.setSuccess(true);
                        returnMsg.setMsg("修改成功！");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } else {
                    returnMsg.setSuccess(false);
                    returnMsg.setMsg("数据不存在！");
                    return returnMsg;
                }
            } else {
                // id 为空 是添加
                if (null != images ) {
                    String imagesPath = MyTools.saveFiles(images, uploadPath, webImgPath);
                    shopClassify.setImg(imagesPath);
                }
                shopClassifyDao.save(shopClassify);
                returnMsg.setSuccess(true);
                returnMsg.setMsg("添加成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }

        return returnMsg;
    }

    /**
     * 根据id删除对应的分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/delShopClassify")
    public Object delShopClassify(Long id) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            ShopClassify shopClassify = shopClassifyDao.findById(id);
            if (!MyTools.isEmpty(shopClassify)) {
                shopClassifyDao.delete(shopClassify);
                List<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(shopClassify.getId());
                if (null != shopClassifySons) {
                    // 删除一级分类下的所有的二级分类
                    for (ShopClassifySon scs:shopClassifySons) {
                        shopClassifySonDao.delete(scs);
                    }
                }
                returnMsg.setSuccess(true);
                returnMsg.setMsg("删除成功！");
            } else {
                returnMsg.setSuccess(false);
                returnMsg.setMsg("当前数据不存在！");
            }
        } catch (Exception e) {

            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }
}
