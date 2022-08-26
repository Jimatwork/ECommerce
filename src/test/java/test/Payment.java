package test;

import com.kubian.mode.util.PayCommonUtil;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author HD
 * @Title: Payment
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/4/00411:08
 */
public class Payment {
    public static void testPay(){
        try {
            String appId = "wx73c2d36878ef2aa7";
            String mchId = "1463708302";
            String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder ";
            SortedMap<Object,Object> parameters = new TreeMap<>();
            parameters.put("appid", appId);
            parameters.put("mch_id", mchId);
            parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
            parameters.put("body", "626鲜花贸易");
            parameters.put("out_trade_no", new Date().getTime()+"");
            parameters.put("total_fee", "1");
            parameters.put("spbill_create_ip","127.0.0.1");
            parameters.put("notify_url", "http://manage.app4u.tv/HanaGlobal/ContentAction!weixinPaySucc.action");
            parameters.put("trade_type", "APP");
            String sign = PayCommonUtil.createSign("UTF-8", parameters);
            parameters.put("sign", sign);
            String requestXML = PayCommonUtil.getRequestXml(parameters);
            System.out.println("生成统一支付XML="+requestXML);
            String result =RequestUtil.httpsRequest(UNIFIED_ORDER_URL, "POST", requestXML);
            System.out.println("统一支付接口返回值="+result);
//            Map<String, String> map = XMLUtil.doXMLParse(result);
            //this.outJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 微信支付成功
     */
    public void weixinAjaxPaySucc(){
//        try {
//            ColumnContent cc = (ColumnContent)universalManager.get(ColumnContent.class, Long.parseLong(this.myIncome.getDsConId()));
//            if(cc.getAppUserId() != null && !"".equals(cc.getAppUserId())){
//                AppUser au = (AppUser)universalManager.get(AppUser.class, cc.getAppUserId());
//                this.myIncome.setDsUid(au.getId()+"");
//                this.myIncome.setDsUname(au.getUserName());
//            }
//            universalManager.saveOrUpdate(this.myIncome);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        this.outJsonString(this.succ);
    }
    /**
     * 微信支付成功返回给微信结果
     */
    public void weixinPaySucc(){

    }
 public static void main(String [] args) {
     testPay();
 }
}
