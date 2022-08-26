package com.kubian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kubian.mode.*;
import com.kubian.mode.dao.*;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ResultVO;
import com.kubian.mode.util.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:ProductController
 *
 * @author WangW
 * @Description: 商品管理
 * @date 2018年6月28日
 */
@RestController
public class ProductController {


    @Autowired
    private ProductDao productDao;
    @Autowired
    private MediaFileDao mediaFileDao;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private SpecsDao specsDao;
    @Autowired
    private ProductCommentDao productCommentDao;
    @Autowired
    private AppUserDao appUserDao;
    @PersistenceContext
    private EntityManager em;

    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    /**
     * 根据商品分类和商家ID获取商品
     *
     * @param page
     * @param limit
     * @param bid
     * @return Object
     */
    @RequestMapping(value = "/getProductByBus")
    @ResponseBody
    public Object getProductByBus(Integer page, Integer limit, Long bid, Long sIds) {
        Page<Product> result = productDao.findByBidAndIsCloseAndStatus(new PageRequest(page, limit), bid, 0, 0, sIds);
        List<Product> productList = result.getContent();
        JSONArray jsonArray = new JSONArray();
        for (Product product : productList) {
            product.setBusiness(businessDao.findById(product.getBid()));
            List<Specs> specsList = specsDao.findByPid(product.getId());
            product.setSpecsList(specsList);
            Integer count = productCommentDao.findByTypeAndPid(product.getId());
            Integer good = productCommentDao.findByTypeAndPid(product.getId(), 1);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(product));
            jsonObject.remove("introduction");
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            String results = numberFormat.format((float) good / (float) count * 100);
            if (results.equals("�")) {
                jsonObject.put("commentrate", "100%");
            } else {
                jsonObject.put("commentrate", results + "%");
            }
            jsonObject.put("commentcount", count);
            jsonArray.add(jsonObject);
        }
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setTotalSize(result.getTotalPages());
        returnMsg.setSuccess(true);
        returnMsg.setList(jsonArray);
        return returnMsg;
    }

    /**
     * 根据商品分类|商家信息|商品信息分页获取商品
     *
     * @param page
     * @param limit
     * @param search
     * @param way
     * @return Object
     */
    @RequestMapping(value = "/getAllProduct")
    @ResponseBody
    public Object getAllProduct(Integer page, Integer limit, String search, String way) {
        Pageable pageable = new PageRequest(page == null ? 0 : page - 1, limit == null ? 10 : limit);
        //商铺名搜索
        if ("bus".equals(way)) {
            ReturnMsg returnMsg = new ReturnMsg();
            String sql = "from Product";
            List<Business> list1 = businessDao.findByNameLike(search == null || search == "" ? "%%" : "%" + search + "%");
            for (int i = 0; i < list1.size(); i++) {
                if (i == 0) {
                    sql += " where bid=" + ((Business) list1.get(i)).getId();
                } else if (i > 0 && i < list1.size()) {
                    sql += " or bid=" + ((Business) list1.get(i)).getId();
                }
            }
            returnMsg.setSuccess(true);
            if (list1.size() == 0) {
                return returnMsg;
            }
            returnMsg.setTotalSize(em.createQuery(sql).getResultList().size());
            List<Product> list = em.createQuery(sql).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
            for (Product product : list) {
                product.setBusiness(businessDao.findById(product.getBid()));
            }
            returnMsg.setList(list);
            return returnMsg;
        }
        //商品名
        else {
            List<Product> list = productDao.findByNameLike(pageable, search == null || search == "" ? "%%" : "%" + search + "%");
            for (Product product : list) {
                product.setBusiness(businessDao.findById(product.getBid()));
            }
            ReturnMsg returnMsg = new ReturnMsg();
            returnMsg.setSuccess(true);
            returnMsg.setList(list);
            returnMsg.setTotalSize(productDao.findByNameCount(search == null || search == "" ? "%%" : "%" + search + "%"));
            return returnMsg;
        }
    }

    /**
     * 根据商品ID获取商品信息
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/getProductByID")
    @ResponseBody
    public Object getProductByID(Long id) {
        Product product = productDao.findById(id);
        product.setBusiness(businessDao.findById(product.getBid()));
        product.setIntroductionPhoto(mediaFileDao.findByPidAndType(product.getId(), 1));
        product.setSpecsList(specsDao.findByPid(product.getId()));

        if (product != null) {
            return new ResultVO().put("code", 0).put("msg", "操作成功").put("data", product);
        } else {
            return new ResultVO().put("code", 1).put("msg", "操作失败").put("data", product);
        }
    }

    /**
     * 保存媒体文件
     *
     * @param multipartFiles
     * @return Object
     */
    private void saveMediaFile(MultipartFile[] multipartFiles, String uploadPath, String webImgPath, MediaFileDao mediaFileDao, Long id, Integer type) {
        List<MediaFile> list = new ArrayList<MediaFile>();
        for (MultipartFile introductionPhoto : multipartFiles) {
            String filePath = MyTools.saveFiles(introductionPhoto, uploadPath, webImgPath);
            MediaFile mediaFile1 = new MediaFile();
            mediaFile1.setPid(id);
            mediaFile1.setLocation(filePath);
            mediaFile1.setType(type);
            mediaFileDao.save(mediaFile1);
            list.add(mediaFile1);
        }
    }

    /**
     * 测试
     *
     * @return Object
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    public Object test() {
        return new ResultVO().put("data", null);
    }

    /**
     * 修改商品状态
     *
     * @param param
     * @return Object
     */
    @RequestMapping(value = "/saveStatus")
    @ResponseBody
    public Object saveStatus(Product param) {
        try {
            Product product = productDao.findById(param.getId());
            if (businessDao.findById(product.getBid()).getStatus() == 3) {
                return ResultVO.error(501, "商铺已被强制下线");
            }
            if (param.getIsClose() != null) {
                product.setIsClose(param.getIsClose());
            }
            if (param.getStatus() != null) {
                product.setStatus(param.getStatus());
            }
            productDao.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * (增加/修改)商品
     *
     * @param product
     * @param introductionPhotos
     * @param images
     * @return Object
     */
    @RequestMapping(value = "/saveProduct")
    @ResponseBody
    public Object saveProduct(Product product, @RequestParam("introductionPhotos") MultipartFile[] introductionPhotos, MultipartFile images) {
        try {
            if (images != null) {
                String imagesPath = MyTools.saveFiles(images, uploadPath, webImgPath);
                product.setImage(imagesPath);
            }
            if (product.getId() == null) {
                productDao.save(product);
                saveMediaFile(introductionPhotos, uploadPath, webImgPath, mediaFileDao, product.getId(), 1);
            } else {
                Product product1 = productDao.findById(product.getId());
                MyTools.updateNotNullField(product1, product);
                productDao.save(product1);
                List<MediaFile> list = mediaFileDao.findByPidAndType(product1.getId(), 1);
                mediaFileDao.delete(list);
                saveMediaFile(introductionPhotos, uploadPath, webImgPath, mediaFileDao, product.getId(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除商品
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/delProduct")
    @ResponseBody
    public Object delProduct(Long id) {
        try {
            productDao.delete(productDao.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 根据商铺id获取商品
     *
     * @param bid    商铺id
     * @param page   当前页
     * @param limit  每页大小
     * @param search 搜索的内容
     * @param num    0.正序 1.倒序
     * @param sId    一级栏目id
     * @param sIds   二级栏目id
     * @return
     */
    @RequestMapping(value = "/getProductByBid")
    public Object getProductByBid(Long bid, Integer page, Integer limit, String search, Integer num, Long sId, Long sIds) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Pageable pageable = new PageRequest(page, limit);
            if (!MyTools.isEmpty(search)) {
                // search不为空是模糊搜索
                List<Product> productList = productDao.getProductByName("%" + search + "%", bid, pageable);
                if (null != productList) {
                    for (Product ps : productList) {
                        List<Specs> specsList = specsDao.findByPid(ps.getId());
                        ps.setSpecsList(specsList);
                    }

                    returnMsg.setList(productList);
                    returnMsg.setSuccess(true);
                    returnMsg.setMsg("获取成功！");
                    int con = productDao.getProductByName("%" + search + "%", bid);
                    returnMsg.setTotalSize(con);
                    return returnMsg;
                }
            }

//            Sort sort = new Sort(Sort.Direction.DESC, "id");
//            if (!MyTools.isEmpty(num) && num == 0) {
//                sort = new Sort(Sort.Direction.ASC, "id");
//            }
            Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "id");
            Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "slhc");
            if (!MyTools.isEmpty(num) && num == 0) {
                order1 = new Sort.Order(Sort.Direction.ASC, "id");
            }
            List<Sort.Order> list = new ArrayList<>();
            list.add(order2);
            list.add(order1);
            Sort sort = new Sort(list);
            pageable = new PageRequest(page, limit, sort);
            Page<Product> productPage = null;
            if (sId == 0) {
                // 获取的是对应的商家所有的数据
                productPage = productDao.findByBid(bid, pageable);
            } else if (sIds == 0) {
                // 获取的是对应一级栏目下的数据
                productPage = productDao.findByBidAndSId(bid, sId, pageable);
            } else {
                // 获取的是对应二级栏目下的数据
                productPage = productDao.findByBidAndSIds(bid, sIds, pageable);
            }

            if (null != productPage.getContent()) {
                for (Product ps : productPage.getContent()) {
                    List<Specs> specsList = specsDao.findByPid(ps.getId());
                    ps.setSpecsList(specsList);
                }
                returnMsg.setList(productPage.getContent());
                returnMsg.setSuccess(true);
                returnMsg.setMsg("获取成功！");
                returnMsg.setTotalSize(productPage.getTotalElements());
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setList(null);
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
            returnMsg.setTotalSize(0);
        }
        return returnMsg;
    }

    /**
     * 商品上下架
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setIsColse")
    public Object setIsColse(Long id) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Product product = productDao.findById(id);
            if (!MyTools.isEmpty(product)) {
                if (product.getIsClose() == 0) {
                    product.setIsClose(1);
                } else if (product.getIsClose() == 1) {
                    product.setIsClose(0);
                }
                productDao.save(product);
                returnMsg.setSuccess(true);
                returnMsg.setMsg("操作成功！");

            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 把对应商品修改为热销或促销
     *
     * @param id   商品id
     * @param flag 0.设为热销  1.设为促销
     * @return
     */
    @RequestMapping(value = "/updateSlhc")
    public Object updateSlhc(Long id, Integer flag) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Product product = productDao.findById(id);
            if (!MyTools.isEmpty(product)) {
                if (flag == 0) {
                    if (product.getSlhc() == null) {// 设为热销
                        product.setSlhc(1);
                    } else {
                        product.setSlhc(null);   // 取消热销
                    }
                } else if (flag == 1) {
                    if (product.getSales() == null) {  // 设为促销
                        product.setSales(1);
                    } else {
                        product.setSales(null);  // 取消促销
                    }
                }
                productDao.save(product);
                returnMsg.setSuccess(true);
                returnMsg.setMsg("设置成功！");
            } else {
                returnMsg.setSuccess(false);
                returnMsg.setMsg("数据不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    /**
     * 根据商家id 分页获取热销商品 或 促销商品
     *
     * @param bid   商家id
     * @param page  当前页
     * @param limit 每页大小
     * @param flag  0.获取的是热销商品 1.获取的是促销商品
     * @return
     */
    @RequestMapping(value = "/getProductBySlhcOrSales")
    public Object getProductBySlhcOrSales(Long bid, Integer page, Integer limit, Integer flag) {
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            Pageable pageable = new PageRequest(page, limit);
            Page<Product> products = null;
            if (flag == 0) { // 获取的是热销的商品
                products = productDao.findByBidAndSlhc(bid, 1, pageable);

            } else if (flag == 1) { // 获取的是促销的商品
                products = productDao.findByBidAndSales(bid, 1, pageable);
            }
            if (products.getTotalElements() > 0) {
                returnMsg.setMsg("获取成功！");
                returnMsg.setSuccess(true);
                returnMsg.setList(products.getContent());
                returnMsg.setTotalSize(products.getTotalElements());
            } else {
                returnMsg.setMsg("没有数据！");
                returnMsg.setSuccess(true);
                returnMsg.setList(null);
                returnMsg.setTotalSize(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setSuccess(false);
            returnMsg.setMsg("异常错误！");
        }
        return returnMsg;
    }

    public static void main(String[] args) {
        int a = (3 > 2) ? 1 : 2;
        System.out.println(a);
    }
}
