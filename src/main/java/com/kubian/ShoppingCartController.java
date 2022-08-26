package com.kubian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.kubian.mode.*;
import com.kubian.mode.dao.*;
import com.kubian.mode.util.*;
import com.kubian.mode.util.alipay.AlipayConfig;
import com.kubian.mode.util.alipay.AlipayNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:ShoppingCartController
 *
 * @author WangW
 * @Description: 购物车和订单管理
 * @date 2018年6月28日
 */
@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SpecsDao specsDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ReceivingAddressDao receivingAddressDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private BusinessDao businessDao;

    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    /**
     * 我的购物车
     *
     * @param userId
     * @return Object
     */
    @RequestMapping(value = "/getMyCart")
    @ResponseBody
    public Object getMyCart(Long userId) {
        List<ShoppingCart> list = shoppingCartDao.findByUserId(userId);
        for (ShoppingCart shoppingCart : list) {
            shoppingCart.setProduct(productDao.findById(shoppingCart.getPid()));
            shoppingCart.setSpecs(specsDao.findById(shoppingCart.getSid()));
            shoppingCart.setBusiness(businessDao.findById(shoppingCart.getBid()));
        }
        if (list != null) {
            return ResultVO.custom(0, "操作成功", list, list.size());
        } else {
            return ResultVO.custom(1, "无数据", list, list.size());
        }
    }

    /**
     * 加入购物车
     *
     * @param shoppingCart
     * @return Object
     */
    @RequestMapping(value = "/addCart")
    @ResponseBody
    public Object addCart(ShoppingCart shoppingCart) {
        ShoppingCart result = shoppingCartDao.findByUserIdAndPidAndSid(shoppingCart.getUserId(), shoppingCart.getPid(), shoppingCart.getSid());
        try {
            //如果库存小于购买量则无法加入购物车
            if (specsDao.findById(shoppingCart.getSid()).getInventory() < shoppingCart.getCount()) {
                return ResultVO.error(501, "库存不足");
            }
            //如果该用户购物车存在此商品则添加数量
            if (result != null) {
                result.setCount(result.getCount() + shoppingCart.getCount());
                shoppingCartDao.save(result);
            }
            //否则添加此条商品
            else {
                shoppingCartDao.save(shoppingCart);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ResultVO.ok();
    }

    /**
     * 批量删除购物车中的商品
     *
     * @param idList
     * @return Object
     */
    @RequestMapping(value = "/delCart")
    @ResponseBody
    public Object delCart(String idList) {
        try {
            List<Long> list = new ArrayList<>();
            String s[] = idList.split(",");
            for (String s1 : s) {
                list.add(Long.parseLong(s1));
            }
            shoppingCartDao.deleteBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 购物车中的商品数量修改
     *
     * @param cartid
     * @param count
     * @return Object
     */
    @RequestMapping(value = "/updateCart")
    @ResponseBody
    public Object updateCart(Long cartid, Integer count) {
        try {
            ShoppingCart shoppingCart = shoppingCartDao.findById(cartid);
            shoppingCart.setCount(count);
            shoppingCartDao.save(shoppingCart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    /**
     * 结算购物车
     *
     * @param shoppingCarts
     * @return Object
     */
    @RequestMapping(value = "/calculateCart")
    @ResponseBody
    public Object calculateCart(String shoppingCarts) throws Exception {
        List<ShoppingCart> shoppingCartList = JSONArray.parseArray(shoppingCarts, ShoppingCart.class);
        Map<Long, Order> map = new HashMap<>();
        ReceivingAddress receivingAddress = null;
        Integer id = 1;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            MyTools.updateNotNullField(shoppingCart, shoppingCartDao.findById(shoppingCart.getId()));
            receivingAddress = receivingAddressDao.findDefaultAddress(shoppingCart.getUserId());
            List<OrderDetail> orderDetailList = new ArrayList<>();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPid(shoppingCart.getPid());
            orderDetail.setSid(shoppingCart.getSid());
            orderDetail.setCount(shoppingCart.getCount());
            orderDetail.setPrice(specsDao.findById(shoppingCart.getSid()).getPrice() * shoppingCart.getCount());
            orderDetail.setCarriage(productDao.findById(shoppingCart.getPid()).getExpressMoney());
            Product product = productDao.findById(shoppingCart.getPid());
            orderDetail.setProduct(product);
            orderDetail.setSpecs(specsDao.findById(shoppingCart.getSid()));
            orderDetailList.add(orderDetail);
            if (map.get(productDao.findById(shoppingCart.getPid()).getBid()) == null) {
                Order order = new Order();
                order.setId(Long.parseLong(id + ""));
                id++;
                order.setUserId(shoppingCart.getUserId());
                order.setBid(productDao.findById(shoppingCart.getPid()).getBid());
                order.setSumPrice(orderDetail.getPrice());
                order.setCarriage(orderDetail.getCarriage());
                order.setRealPrice(orderDetail.getPrice() + orderDetail.getCarriage());
                order.setBusiness(businessDao.findById(order.getBid()));
                order.setOrderDetailList(orderDetailList);
                if (receivingAddress != null) {
                    order.setReceiverAddress(receivingAddress.getAddress() + receivingAddress.getDetailAddress());
                    order.setReceiverName(receivingAddress.getConsignee());
                    order.setReceiverPhone(receivingAddress.getContactNumber());
                }
                map.put(productDao.findById(shoppingCart.getPid()).getBid(), order);
            } else {
                Order order = map.get(productDao.findById(shoppingCart.getPid()).getBid());
                order.setId(Long.parseLong(id + ""));
                id++;
                order.setSumPrice(order.getSumPrice() + orderDetail.getPrice());
                order.setCarriage(order.getCarriage() + orderDetail.getCarriage());
                order.setRealPrice(order.getSumPrice() + order.getCarriage());
                order.setBusiness(businessDao.findById(order.getBid()));
                if (receivingAddress != null) {
                    order.setReceiverAddress(receivingAddress.getAddress() + receivingAddress.getDetailAddress());
                    order.setReceiverName(receivingAddress.getConsignee());
                    order.setReceiverPhone(receivingAddress.getContactNumber());
                }
                order.getOrderDetailList().add(orderDetail);
            }
        }
        if (map != null) {
            return new ResultVO().put("code", 0).put("msg", "操作成功").put("data", mapTransitionList(map));
        } else {
            return new ResultVO().put("code", 1).put("msg", "操作失败").put("data", mapTransitionList(map));
        }
    }

    /**
     * 直接购买
     *
     * @param shoppingCart
     * @return Object
     */
    @RequestMapping(value = "/directPurchase")
    @ResponseBody
    public Object directPurchase(ShoppingCart shoppingCart) throws Exception {
        Map<Long, Order> map = new HashMap<>();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPid(shoppingCart.getPid());
        orderDetail.setSid(shoppingCart.getSid());
        orderDetail.setCount(shoppingCart.getCount());
        orderDetail.setPrice(specsDao.findById(shoppingCart.getSid()).getPrice() * shoppingCart.getCount());
        orderDetail.setCarriage(productDao.findById(shoppingCart.getPid()).getExpressMoney());
        Product product = productDao.findById(shoppingCart.getPid());
        orderDetail.setProduct(product);
        orderDetail.setSpecs(specsDao.findById(shoppingCart.getSid()));
        orderDetailList.add(orderDetail);
        ReceivingAddress receivingAddress = receivingAddressDao.findDefaultAddress(shoppingCart.getUserId());
        if (map.get(productDao.findById(shoppingCart.getPid()).getBid()) == null) {
            Order order = new Order();
            order.setUserId(shoppingCart.getUserId());
            order.setBid(productDao.findById(shoppingCart.getPid()).getBid());
            order.setSumPrice(orderDetail.getPrice());
            order.setCarriage(orderDetail.getCarriage());
            order.setRealPrice(orderDetail.getPrice() + orderDetail.getCarriage());
            order.setOrderDetailList(orderDetailList);
            order.setBusiness(businessDao.findById(order.getBid()));
            if (receivingAddress != null) {
                order.setReceiverAddress(receivingAddress.getAddress() + receivingAddress.getDetailAddress());
                order.setReceiverName(receivingAddress.getConsignee());
                order.setReceiverPhone(receivingAddress.getContactNumber());
            }
            map.put(productDao.findById(shoppingCart.getPid()).getBid(), order);
        } else {
            Order order = map.get(productDao.findById(shoppingCart.getPid()).getBid());
            order.setSumPrice(order.getSumPrice() + orderDetail.getPrice());
            order.setCarriage(order.getCarriage() + orderDetail.getCarriage());
            order.setRealPrice(order.getSumPrice() + order.getCarriage());
            order.setBusiness(businessDao.findById(order.getBid()));
            if (receivingAddress != null) {
                order.setReceiverAddress(receivingAddress.getAddress() + receivingAddress.getDetailAddress());
                order.setReceiverName(receivingAddress.getConsignee());
                order.setReceiverPhone(receivingAddress.getContactNumber());
            }
            order.getOrderDetailList().add(orderDetail);
        }
        if (map != null) {
            return new ResultVO().put("code", 0).put("msg", "操作成功").put("data", mapTransitionList(map));
        } else {
            return new ResultVO().put("code", 1).put("msg", "操作失败").put("data", mapTransitionList(map));
        }
    }

    public static List mapTransitionList(Map map) {
        List list = new ArrayList();
        Iterator iter = map.entrySet().iterator();  //获得map的Iterator
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 提交订单返回预支付ID
     *
     * @param orderLists
     * @param payWay     (1:微信 2:支付宝 3:线下支付)
     * @return Object
     */
    @RequestMapping(value = "/submitOrder")
    @ResponseBody
    public Object submitOrder(String orderLists, Integer payWay) throws Exception {
        //总价格
        Double sum = 0.0;
        //ReceivingAddress receivingAddress = receivingAddressDao.findById(addressid);
        List<Order> orderList = JSONArray.parseArray(orderLists, Order.class);
        String serialNo = MyTools.getSerialNo();
        for (Order order : orderList) {
            order.setId(null);
            sum += order.getRealPrice();
            order.setOrderNumber(serialNo);
            order.setCreateTime(new Date());
            order.setState(0);
            order.setPayMethod(payWay);
            orderDao.save(order);
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
                orderDetail.setOrderid(order.getId());
                orderDetailDao.save(orderDetail);
                //通过用户ID 商品ID 规格ID找到购物车并删除
                try {
                    ShoppingCart shoppingCart = shoppingCartDao.findByUserIdAndPidAndSid(order.getUserId(), orderDetail.getPid(), orderDetail.getSid());
                    shoppingCartDao.delete(shoppingCart);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
        //微信支付
        if (payWay == 1) {
            String realprice = (int) (sum * 100) + "";
            Map m = WXpay(realprice, "商品购买", serialNo);
            return new ResultVO().put("result", m).put("way", "1").put("success", "success");
        }
        //支付宝APP支付
        else if (payWay == 2) {
            AlipayTradeAppPayResponse result = (AlipayTradeAppPayResponse) AliApppay(serialNo, sum + "", "Shoping", "test");
            return new ResultVO().put("result", result).put("way", "2").put("success", "success");
        } else {
            return ResultVO.ok().put("success", "success");
        }
    }

    //微信支付
    public Map WXpay(String price, String body, String orderNum) {
        try {
            String appId = "wx07978c55e928dc0a";
            String mchId = "1514928631";
            String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
            parameters.put("appid", appId);
            parameters.put("mch_id", mchId);
            parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
            parameters.put("body", body);
            parameters.put("out_trade_no", orderNum);
            parameters.put("total_fee", price);
            parameters.put("spbill_create_ip", "127.0.0.1");
            parameters.put("notify_url", "http://app.issop.cn/kumai/getWXpayResult");
            parameters.put("trade_type", "APP");
            String sign = PayCommonUtil.createSign("UTF-8", parameters);
            parameters.put("sign", sign);
            String requestXML = PayCommonUtil.getRequestXml(parameters);
            System.out.println("生成统一支付XML=" + requestXML);
            String result = RequestUtil.httpsRequest(UNIFIED_ORDER_URL, "POST", requestXML);
            System.out.println("___________" + result);
            Map<String, String> m = XMLUtil.doXMLParse(result);
            //生成调起支付需要的json,并且二次签名-----
            SortedMap<Object,Object> param1 = new TreeMap<Object,Object>();
            param1.put("appid", appId);
            param1.put("partnerid", mchId);
            param1.put("noncestr", m.get("nonce_str"));
            param1.put("package","Sign=WXPay");
            param1.put("prepayid", m.get("prepay_id"));
            param1.put("timestamp", new Date().getTime()+"");
            String sign1 = PayCommonUtil.createSign("UTF-8", param1);
            param1.put("sign", sign1);
            System.out.println("___________" + JSON.toJSONString(param1));
            return param1;
            //this.outJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //支付宝网站支付
    public Object Alipay(String outTradeNo, String totalAmount, String subject, String body) {
        try {
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(outTradeNo);
            model.setSubject(subject);
            model.setTotalAmount(totalAmount);
            model.setBody(body);
            model.setTimeoutExpress("2m");
            //model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            //请求
            String result;
            result = client.pageExecute(alipay_request).getBody();
            //client.execute(result);
            System.out.println("*********************\n返回结果为：" + result);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //支付宝app支付
    public Object AliApppay(String outTradeNo, String totalAmount, String subject, String body) {
        try {
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeAppPayRequest alipay_request = new AlipayTradeAppPayRequest();
            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(outTradeNo);
            model.setSubject(subject);
            model.setTotalAmount(totalAmount);
            model.setBody(body);
            model.setTimeoutExpress("2m");
            //model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            //请求
            AlipayTradeAppPayResponse response = client.sdkExecute(alipay_request);
            //client.execute(result);
            System.out.println("*********************\n返回结果为：" + JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new ShoppingCartController().WXpay("111","111","1232131");
    }

    /**
     * 微信支付回调结果
     *
     * @param request
     * @return Object
     */
    @RequestMapping(value = "/getWXpayResult", produces = "text/html;charset=utf-8")
    public Object getWXpayResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("**********************************微信回调成功*******************************************");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = XMLUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = params.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = params.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 判断签名是否正确
        String resXml = "";
        Map<String, String> return_data = new HashMap<String, String>();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code不正确");
            return StringUtil.GetMapToXML(return_data);
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
            List<Order> orderList = orderDao.findByOrderNumber(params.get("out_trade_no"));
            for (Order order : orderList) {
                order.setState(1);
                order.setTransactionNo(params.get("transaction_id"));
                order.setPayTime(new Date());
                RunningTab runningTab = new RunningTab();
                runningTab.setBid(order.getBid());
                runningTab.setCreateDate(new Date());
                runningTab.setMoney(order.getRealPrice() + "");
                runningTab.setState(1);
                Business business = businessDao.findById(order.getBid());
                business.setMoney(business.getMoney() + order.getRealPrice());
                businessDao.save(business);
            }
            orderDao.save(orderList);

            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            String out_trade_no = params.get("out_trade_no");
            Date accountTime = DateUtil.stringtoDate(params.get("time_end"), "yyyyMMddHHmmss");
            String ordertime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String totalAmount = String.valueOf(v);
            String appId = params.get("appid");
            String tradeNo = params.get("transaction_id");

            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
            return StringUtil.GetMapToXML(return_data);
        }
    }

    /**
     * 支付宝支付回调结果
     *
     * @param request
     * @return Object
     */
    @RequestMapping(value = "/getAlipayResult", produces = "text/html;charset=utf-8")
    public Object getAlipayResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取支付宝POST过来反馈信息
        log.info("**********************************支付宝回调成功*******************************************");
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");
        //支付宝交易号
        String trade_no = request.getParameter("trade_no");
        //交易状态
        String trade_status = request.getParameter("trade_status");
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if (AlipayNotify.verify(params)) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            boolean flg = false;
            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                //根据订单号将订单状态和支付宝记录表中状态都改为已支付

                List<Order> orderList = orderDao.findByOrderNumber(out_trade_no);
                for (Order order : orderList) {
                    order.setState(1);
                    order.setTransactionNo(trade_no);
                    order.setPayTime(new Date());
                    RunningTab runningTab = new RunningTab();
                    runningTab.setBid(order.getBid());
                    runningTab.setCreateDate(new Date());
                    runningTab.setMoney(order.getRealPrice() + "");
                    runningTab.setState(1);
                    Business business = businessDao.findById(order.getBid());
                    business.setMoney(business.getMoney() + order.getRealPrice());
                    businessDao.save(business);
                }
                orderDao.save(orderList);

            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            //out.print("success"); //请不要修改或删除
            if (flg) {
                return ResultVO.ok();
            } else {
                return ResultVO.error();
            }

            //////////////////////////////////////////////////////////////////////////////////////////
        } else {//验证失败
            //out.print("fail");
            return ResultVO.error();
        }
    }

    /**
     * 取消订单
     *
     * @param id
     * @return Object
     */
    @RequestMapping(value = "/cancelOrder")
    public Object cancelOrder(Long id) {
        try {
            Order order = orderDao.findById(id);
            List<OrderDetail> orderDetailList = orderDetailDao.findByOrderid(order.getId());
            orderDetailDao.delete(orderDetailList);
            orderDao.delete(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        return ResultVO.ok();
    }

    //通过xml 发给微信消息
    public static String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        return "<xml><return_code><![CDATA[" + return_code + "]]>" +
                "</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}
