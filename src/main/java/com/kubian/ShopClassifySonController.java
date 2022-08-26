package com.kubian;

import com.kubian.mode.Product;
import com.kubian.mode.ShopClassifySon;
import com.kubian.mode.dao.ProductDao;
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
 * 商品二级分类
 * @author HD
 * @Title: ShopClassifySonController
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/10/16/01615:50
 */
@RestController
public class ShopClassifySonController {
    @Autowired
    private ShopClassifySonDao shopClassifySonDao;
    @Autowired
    private ProductDao productDao;
    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;
    /**
     * 添加或修改二级分类
     * @param shopClassifySon
     * @return
     */
    @RequestMapping(value = "/addShopClassifySon")
    public Object addShopClassifySon(ShopClassifySon shopClassifySon , MultipartFile images){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (null != shopClassifySon.getId()) {
                // id不为空是修改

                ShopClassifySon shopClassifySon1 = shopClassifySonDao.findById(shopClassifySon.getId());
                if (!MyTools.isEmpty(shopClassifySon1)) {
                    try {
                        if (null != images) {
                            String imagesPath = MyTools.saveFiles(images, uploadPath, webImgPath);
                            shopClassifySon.setImg(imagesPath);
                        }
                        MyTools.updateNotNullFieldForPatient(shopClassifySon1,shopClassifySon);
                        shopClassifySonDao.save(shopClassifySon1);
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
                    returnMsg.setMsg("数据异常！");
                }
            } else {
                // id为空是添加
                if (null != images) {
                    String imagesPath = MyTools.saveFiles(images, uploadPath, webImgPath);
                    shopClassifySon.setImg(imagesPath);
                }
                shopClassifySonDao.save(shopClassifySon);
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
     * 后台根据一级分类id获取二级分类
     * @param sId
     * @return
     */
    @RequestMapping(value = "/getShopClassifySon")
    public Object getShopClassifySon(Long sId , Integer page , Integer limit){
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            if (MyTools.isEmpty(page) && MyTools.isEmpty(limit)) {
                List<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(sId);
                if (null != shopClassifySons) {
                    returnMsg.setMsg("获取成功！");
                    returnMsg.setSuccess(true);
                    returnMsg.setTotalSize(shopClassifySons.size());
                    returnMsg.setList(shopClassifySons);
                } else {
                    returnMsg.setMsg("没有数据！");
                    returnMsg.setSuccess(true);
                    returnMsg.setTotalSize(0);
                    returnMsg.setList(null);
                }
                return returnMsg;
            }
            Sort sort = new Sort(Sort.Direction.ASC,"sequence");
            page = page - 1 ;
            Pageable pageable = new PageRequest(page , limit , sort);
            Page<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySId(sId , pageable);
            if (null != shopClassifySons.getContent()) {
                returnMsg.setMsg("获取成功！");
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(shopClassifySons.getTotalElements());
                returnMsg.setList(shopClassifySons.getContent());
            } else {
                returnMsg.setMsg("没有数据！");
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(0);
                returnMsg.setList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * app根据一级分类id获取二级分类
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/getShopClasifySonApp")
    public Object getShopClasifySonApp(Long sId , Integer page , Integer limit) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Sort sort = new Sort(Sort.Direction.ASC,"sequence");
            Pageable pageable = new PageRequest(page , limit , sort);
            Page<ShopClassifySon> shopClassifySons = shopClassifySonDao.findBySIdAndState(sId , 0 , pageable);
            if (null != shopClassifySons.getContent()) {
                for (ShopClassifySon shopClassifySon : shopClassifySons) {
                    List<Product> products = productDao.findBySIdsAndIsSuggest(shopClassifySon.getId() , 0);
                    if (null != products) {
                        shopClassifySon.setProducts(products);
                    }
                }
                returnMsg.setMsg("获取成功！");
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(shopClassifySons.getTotalElements());
                returnMsg.setList(shopClassifySons.getContent());
            } else {
                returnMsg.setMsg("没有数据！");
                returnMsg.setSuccess(true);
                returnMsg.setTotalSize(0);
                returnMsg.setList(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 删除对应的二级分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/delShopClassifySon")
    public Object delShopClassifySon(Long id) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            ShopClassifySon shopClassifySon = shopClassifySonDao.findById(id);
            if (!MyTools.isEmpty(shopClassifySon)) {
                shopClassifySonDao.delete(shopClassifySon);
                returnMsg.setMsg("删除成功！");
                returnMsg.setSuccess(true);
            } else {
                returnMsg.setMsg("数据异常！");
                returnMsg.setSuccess(false);
            }
        } catch (Exception e) {

            returnMsg.setMsg("异常错误！");
            returnMsg.setSuccess(false);
        }
        return returnMsg;
    }
}
