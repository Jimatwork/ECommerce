package com.kubian;

import com.alibaba.fastjson.JSON;
import com.kubian.mode.Product;
import com.kubian.mode.Specs;
import com.kubian.mode.dao.ProductDao;
import com.kubian.mode.dao.SpecsDao;
import com.kubian.mode.util.ResultVO;
import com.kubian.mode.util.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:SpecsController
 *
 * @author WangW
 * @Description: 规格管理
 * @date 2018年6月28日
 */
@RestController
public class SpecsController {

    @Autowired
    private SpecsDao specsDao;
    @Autowired
    private ProductDao productDao;

    private static final Logger log = LoggerFactory.getLogger(SpecsController.class);

    /**
     * (增加/修改)商品规格
     *
     * @param specs
     * @return Object
     */
    @RequestMapping(value = "/saveSpecs")
    @ResponseBody
    public Object saveSpecs(Specs specs) {
        try {
            Double min = specsDao.findByPidMin(specs.getPid());
            Product product = productDao.findById(specs.getPid());
            if (min != null) {
                Double moremin = min < specs.getPrice() ? min : specs.getPrice();
                product.setPriceInterval(moremin + "");
            } else {
                product.setPriceInterval(specs.getPrice() + "");
            }
            productDao.save(product);
            specsDao.save(specs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 删除商品规格
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/delSpecs")
    @ResponseBody
    public Object delSpecs(Long id) {
        try {
            Specs specs = specsDao.findById(id);
            specsDao.delete(specsDao.findById(id));
            String min = specsDao.findByPidMin(specs.getPid()) + "";
            Product product = productDao.findById(specs.getPid());
            product.setPriceInterval(min);
            productDao.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 根据商品ID查询规格
     *
     * @param pid
     * @return Object
     */
    @RequestMapping(value = "/getSpecsByPid")
    @ResponseBody
    public Object getSpecsByPid(Long pid) {
        try {
            List<Specs> list = specsDao.findByPid(pid);
            ReturnMsg returnMsg = new ReturnMsg();
            returnMsg.setSuccess(true);
            returnMsg.setList(list);
            returnMsg.setTotalSize(list.size());
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
