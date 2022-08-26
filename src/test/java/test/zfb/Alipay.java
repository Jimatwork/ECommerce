package test.zfb;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
//import com.aspire.boc.util.ResourceManager;
//import com.google.gson.Gson;
//import com.qlwb.business.payment.vo.AlipayVo;

/**
 * 测试支付宝转账
 *
 * @author HD
 * @Title: Alipay
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/10/01010:00
 */
public class Alipay {
    private static String gateway = "https://openapi.alipay.com/gateway.do";//支付宝网关
    private static String appid = "2018090561259477";//阿里公共账户的id
    //商家私钥
    private static String private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyIC+KYVfqVchJozdP7tP/2yNBFCZpSX9T4ZJq/tZJtpKczXU17KNq18zcLXdvuRBrcQO8GTHJgL1NZ0aHoYOFFndlC9tCtz3ZdiIP8bRCPd4NQicdtbBvrqzJ+qS6bFCi4yeTJRPzsi0IJB7jkSOZAm7DQPAqOh1Ze+eJVJ/VAH9B6svBiWhPWMEv9SG8x+VEki+M/RGIlXAz/PhwdbI0YQeH1kCMCQsXZXLhlgDrA1SIUOooygRyTti9TEmGsHFs9XbAtiN/xX6ef14hwRAGvMTLvedaFybFj/R4kGi7veizzc29HSXeQp1Vg6z1E9AnhR8a/FgSU3J46iYKJZKzAgMBAAECggEABkXj5reu5O3Ic+9vjz0V8jjBcRVIlQSlm0qiYWpAGbB4UVVa/18qEiUvbcGcJy8ZSO9d9k3c1v/VqtgsP7KMcy9rXP+h5SOW5gWnBXC5rGuJWmYuAnnXkNdR9nsBOh9+Z34gcqonnbl0pIjBHbqKymSviejLjHEnf3NBoDB+wH2I0RNURoEnz2BnUs+NbYZiCyQqTcNUPStyiwzC4TlVpXrvkwB1opdlFdl3YBrE/jTRcEpyHPmAVofcHxcjHdkBkCipSoR7VKA+q/OKoH1fYQXduJm5e8wsdga5AodFWXlAnvVHcYSF1FtM++AXUF8RZ+DW1ghAhYLXXdNhPj+UoQKBgQDz/rB+h0lJgMnq6ug055zMBgSv9fU6wiM2F94Ukci6Q07e6s/YM17+ptdBP49/SLuyOcTj8YN1ipvCg2gg5Wa87bNPwFtXwOY8yyiHNUEkhCW+4TgPEENcEtAn6RHLr0OZ3/mqf4BLIAdT9ALIJVTPXo5loK/22fmN2kDPcfw33wKBgQC649JVAqHqZPCW6VWpRlOFVO9j3kfzHXDthVk9tyo1vm1wRg+ndA7MaZ4HwNg+FlJSUo2IRtjTOjN1SfIciqWcaNP12Doz4ttXizfwp0lc0zNV8pIJXg1w1QCushHtRWxSWn6vkz7eMee6Kl5gi5mJjbTCefSxVF+QP2kb2UpPrQKBgEPkjr/mbSIi0AyqKMrv19V9pzg/PGmJM7sNkSIwHqaVrBCjTgpe4QQC8MqbIRbN6kyBfqPexDGkzAqn/gWJT7kqe0apw8D7UBWCglh9HQ9IJiijCrJGWDf6lClG1UjUD/91L0eeCtjcpK1brJp8qMi7CuyEdfI+XQ53uKkAkqoJAoGARCwKc1wMeBXt9P6UcrTmjoJW+JUCpdodcWbtEKgZy5T1ErlbyHd7VnFIZgegWbWGveTDH+zH9vUKBES+/k5M7usGNm1zfa3I2Pw9oZlEF4oq9bUQND6MpJXuM8tc4rIqOzgrchxCGIcXwj7mQk1p6lU4lKAr8+uVGGcpDjGK+9kCgYBVjat0hFOlqDEu6pHCYCQt707sxd/ll24rNpzLu4viSGAUaFAhO1Pl4+RioAb3jck97LmVqu3UoMGrZHm5k+n2ejyTXHbUwBrkPQl8RdckDpHQGaW61XjY0149jkGxQ4DdZ8o3YQFXlWZMyKsClwOG0d5pJZQW8Zsn3mXifzgNDA==";
    private static String input_charset = "UTF-8";//字段类型
    //支付宝公匙(固定值)
    private static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmsra5X2puymVyOv3ky/51At465V/imoNZlqaT18pC+8cLrdELjRw70mxUEj4/yZjZdM6Vb93hau5vDF4oba297LC/h2IU+sAux9/xtAmhFhIX+BH0coKDnH3wixjeW7I81luwv5uFOXadVCif7iuVNqOwPPhmmZ4NHYkF+HcNW3gM7x29jXN+QV8pRA2bCfqv9ZQCNUtEjDxUV3y4gO3Y2/7wEDfaGBpGRpSMS9uAELnftJJr3rTYiqwT24Q7sOBVMJsywjfts67FBE3glIg/66lEgSekUvS7Z+Amw09UK4OdxEdD3u8p35/M1ZJv1GqT8PSPM5H5llzBzpq1lwqiwIDAQAB";


    private static AlipayClient alipayClient;
    private static Alipay instance = new Alipay();

    public static void main(String[] args) {
        zz();
    }

    public static Object zz() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, private_key, "json", input_charset, ali_public_key, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "    \"out_biz_no\":\"3142321423437\"," + //商户转账唯一订单号
                "    \"payee_type\":\"ALIPAY_LOGONID\"," + //收款方账户类型
                "    \"payee_account\":\"13187197733\"," + //收款方账户
                "    \"amount\":\"0.1\"," +  // 转账金额，单位：元
                "    \"payer_show_name\":\"酷脉官方\"," +  //付款方姓名
                "    \"payee_real_name\":\"黄丹\"," + // 收款方真实姓名
                "    \"remark\":\"转账备注\"," +
                "  }");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println("response=" + response.toString());
        JSONObject jsonObject = JSONObject.parseObject(response.toString());

        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return null;
    }

//    private Alipay(){
//        alipayClient=new DefaultAlipayClient(gateway, appid, private_key, "json",input_charset,ali_public_key,"RSA2");
//    }
//
//    public static Alipay getInstance(){
//        return instance;
//    }
//    /**
//     * 支付宝向用户转账
//     * @param bizNo  逻辑单号
//     * @param amount 转账金额 "1.21"单位元
//     * @param account 支付宝账号
//     * @param userName 支付宝真实姓名
//     * @return
//     */
//    public Map<String,String> alipay2User(String bizNo,String amount,String account,String userName){
//        Map<String,String> resultMap=new HashMap<String,String>();
//        AlipayVo vo = new AlipayVo();
//        vo.setOut_biz_no(bizNo);
//        vo.setPayee_type("ALIPAY_LOGONID");
//        vo.setAmount(amount);
//        vo.setPayee_account(account);
//        vo.setPayer_show_name(userName);
//        vo.setPayee_real_name(userName);
//        vo.setRemark("支付宝转账");
//        String json = new Gson().toJson(vo);
//        // 设置请求参数
//        AlipayFundTransToaccountTransferRequest alipayRequest = new AlipayFundTransToaccountTransferRequest();
//        alipayRequest.setBizContent(json);
//        AlipayFundTransToaccountTransferResponse response=null;
//        try {
//            response = alipayClient.execute(alipayRequest);
//            System.out.println(JSON.toJSONString(response));
//            if("10000".equals(response.getCode())){
//                resultMap.put("success", "true");
//                resultMap.put("des", "转账成功");
//            }else{
//                resultMap.put("success", "false");
//                resultMap.put("des", response.getSubMsg());
//            }
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//            resultMap.put("success", "false");
//            resultMap.put("des", "转账失败！");
//        }
//        return resultMap;
//    }


}
