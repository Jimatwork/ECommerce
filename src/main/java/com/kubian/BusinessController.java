package com.kubian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kubian.mode.AppUser;
import com.kubian.mode.Business;
import com.kubian.mode.Product;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.BusinessDao;
import com.kubian.mode.dao.ProductDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ResultVO;
import com.kubian.mode.util.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName:BusinessController
 *
 * @author WangW
 * @Description: 店铺管理
 * @date 2018年7月2日
 */
@RestController
public class BusinessController {

    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;
    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    /**
     * 查询所有的商铺
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年6月28日
     */
    @RequestMapping(value = "/getAllBusiness")
    @ResponseBody
    public Object getAllBusiness(Integer page, Integer limit, String name) {
        Pageable pageable = new PageRequest(page == null ? 0 : page - 1, limit == null ? 10 : limit);
        List<Business> list = businessDao.findByNameLike(pageable, name == null || name == "" ? "%%" : "%" + name + "%");
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setSuccess(true);
        returnMsg.setList(list);
        returnMsg.setTotalSize(businessDao.findByNameCount(name == null || name == "" ? "%%" : "%" + name + "%"));
        return returnMsg;
    }

    /**
     * 查询已上线的商铺
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年6月28日
     */
    @RequestMapping(value = "/getNormalBusiness")
    @ResponseBody
    public Object getNormalBusiness(Integer page, Integer limit, String name, Long bid) {
        Pageable pageable = new PageRequest(page == null ? 0 : page - 1, limit == null ? 10 : limit);
        List<Business> list = businessDao.findByNameLike(pageable, name == null || name == "" ? "%%" : "%" + name + "%", 2, bid);
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setSuccess(true);
        returnMsg.setList(list);
        returnMsg.setTotalSize(businessDao.findByNameCount(name == null || name == "" ? "%%" : "%" + name + "%", 2, bid));
        return returnMsg;
    }

    /**
     * 修改商铺状态
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年6月28日
     */
    @RequestMapping(value = "/updateStatusByBusiness")
    @ResponseBody
    public Object updateStatusByBusiness(Business business) {
        Business business1 = businessDao.findById(business.getId());
        //商铺下线则下线其所有商品
        if (business.getStatus() == 3) {
            List<Product> productList = productDao.findByBid(business.getId());
            for (Product product : productList) {
                product.setStatus(1);
            }
            productDao.save(productList);
        }
        business1.setStatus(business.getStatus());
        businessDao.save(business1);
        return ResultVO.ok();
    }

    /**
     * 根据店铺ID查询商铺
     *
     * @return Object
     * @throws
     * @Description: TODO
     * @author WangW
     * @date 2018年7月17日
     */
    @RequestMapping(value = "/getBusinessById")
    @ResponseBody
    public Object getBusinessById(Long id) {
        Business business = businessDao.findById(id);
        if (business != null) {
            return new ResultVO().put("code", 0).put("msg", "操作成功").put("data", business);
        } else {
            return new ResultVO().put("code", 1).put("msg", "无数据").put("data", business);
        }
    }

    /**
     * (增加/修改)商家店铺
     *
     * @param business
     * @param logos
     * @return Object
     */
    @RequestMapping(value = "/saveBusiness")
    @ResponseBody
    public Object saveBusiness(Long userId, Business business, MultipartFile logos) {
        try {
            String logoPath = "";
            if (logos != null) {
                logoPath = MyTools.saveFiles(logos, uploadPath, webImgPath);
                business.setLogo(logoPath);
            }
            //增加
            if (business.getId() == null) {
                AppUser appUser = appUserDao.findById(userId);
                if (appUser != null & appUser.getBid() != null) {
                    return ResultVO.error(501, "您已经开设商铺了哦");
                }
                business.setMoney(0.0);
                business.setLogo(logoPath);
                business.setRegTime(new Date());
                businessDao.save(business);
                appUser.setBid(business.getId());
                appUser.setIsPc(1);
                appUserDao.save(appUser);
                User user2 = userDao.findByUserNameAndPrefix(appUser.getPhone(), appUser.getPrefix());
                if (user2 == null) {
                    User user = new User();
                    user.setUserName(appUser.getPhone());
                    user.setUserPwd(appUser.getPhonePwd());
                    user.setImg(appUser.getUserImg());
                    user.setAuId(appUser.getId());
                    user.setNickName(appUser.getUserName());
                    user.setUserRole(2);
                    user.setPrefix(appUser.getPrefix());
                    userDao.save(user);
                }
            }
            //修改
            else {
                Business business1 = businessDao.findById(business.getId());
                MyTools.updateNotNullFieldForPatient(business1, business);
                businessDao.save(business1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除商家店铺
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/delBusiness")
    @ResponseBody
    public Object delBusiness(Long id) {
        try {
            Business business = businessDao.findById(id);
            AppUser appUser = appUserDao.findByBid(id);
            appUser.setBid(null);
            List<Product> list = productDao.findByBid(id);
            productDao.delete(list);
            appUserDao.save(appUser);
            businessDao.delete(business);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 模糊搜索商铺名
     *
     * @param search
     * @return Object
     */
    @RequestMapping(value = "/findBusinessLike")
    @ResponseBody
    public Object findBusinessLike(String search) {
        try {
            List<Business> business = businessDao.findByNameLike(search == null || search == "" ? "%%" : "%" + search + "%", 2);
            JSONArray array = new JSONArray();
            for (Business business1 : business) {
                JSONObject o = new JSONObject();
                o.put("text", business1.getName());
                o.put("id", business1.getId());
                array.add(o);
            }
            return new ResultVO().put("results", array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户id获取对应的商铺信息
     *
     * @param appUserId
     * @return
     */
    @RequestMapping(value = "/getBusinessByUserId")
    public Object getBusinessByUserId(Long appUserId) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            AppUser appUser = appUserDao.findById(appUserId);
            if (appUser != null) {
                Business business = businessDao.findById(appUser.getBid());

                List<Business> list = new ArrayList<Business>();
                if (business != null) {
                    log.info(JSONObject.toJSON(business).toString());
                    list.add(business);
                    returnMsg.setMsg("获取成功！");
                    returnMsg.setList(list);
                    returnMsg.setSuccess(true);
                } else {
                    returnMsg.setMsg("没有数据！");
                    returnMsg.setList(null);
                    returnMsg.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("异常错误！");
            returnMsg.setList(null);
            returnMsg.setSuccess(false);
        }
        return returnMsg;
    }
}
