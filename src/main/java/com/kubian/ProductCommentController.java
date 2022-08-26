package com.kubian;

import com.kubian.mode.MediaFile;
import com.kubian.mode.Order;
import com.kubian.mode.ProductComment;
import com.kubian.mode.dao.*;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:ProductCommentController
 *
 * @author WangW
 * @Description: 商品评论管理
 * @date 2018年8月30日
 */
@RestController
public class ProductCommentController {

    @Autowired
    private ProductCommentDao productCommentDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private SpecsDao specsDao;
    @Autowired
    private MediaFileDao mediaFileDao;
    @Autowired
    private OrderDao orderDao;

    @Value("${web.upload.path}")
    private String uploadPath;
    @Value("${web.img.path}")
    private String webImgPath;

    /**
     * 增加评论
     *
     * @param productComment
     * @return Object
     */
    @RequestMapping(value = "/saveProductComment")
    @ResponseBody
    public Object saveProductComment(ProductComment productComment, String images, Long orderid) {
        try {
            if (images != null) {
                String s[] = images.split(",");
                for (String s1 : s) {
                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setPid(productComment.getPid());
                    mediaFile.setType(2);
                    mediaFile.setLocation(s1);
                    mediaFileDao.save(mediaFile);
                }
            }
            productCommentDao.save(productComment);
            Order order = orderDao.findById(orderid);
            order.setState(4);
            orderDao.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除评论
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/delProductComment")
    @ResponseBody
    public Object delProductComment(Long id) {
        try {
            productCommentDao.delete(productCommentDao.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 根据商品ID获取评论
     *
     * @param pid
     * @return Object
     */
    @RequestMapping(value = "/getCommentByPid")
    @ResponseBody
    public Object getCommentByPid(Integer page, Integer limit, Long pid) {
        Page<ProductComment> list = productCommentDao.findByPid(new PageRequest(page - 1, limit), pid);
        List<ProductComment> productCommentList = list.getContent();
        for (ProductComment productComment : productCommentList) {
            productComment.setAppUser(appUserDao.findById(productComment.getUserId()));
            productComment.setSpecs(specsDao.findById(productComment.getSid()));
            productComment.setMediaFileList(mediaFileDao.findByPidAndType(pid,2));
        }
        ResultVO resultVO = new ResultVO();
        resultVO.put("list", productCommentList);
        resultVO.put("success", true);
        resultVO.put("totalSize", productCommentList.size());
        resultVO.put("totalPage", list.getTotalPages());
        return resultVO;
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

}
