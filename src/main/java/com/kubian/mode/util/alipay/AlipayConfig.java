package com.kubian.mode.util.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2018090561259477";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyIC+KYVfqVchJozdP7tP/2yNBFCZpSX9T4ZJq/tZJtpKczXU17KNq18zcLXdvuRBrcQO8GTHJgL1NZ0aHoYOFFndlC9tCtz3ZdiIP8bRCPd4NQicdtbBvrqzJ+qS6bFCi4yeTJRPzsi0IJB7jkSOZAm7DQPAqOh1Ze+eJVJ/VAH9B6svBiWhPWMEv9SG8x+VEki+M/RGIlXAz/PhwdbI0YQeH1kCMCQsXZXLhlgDrA1SIUOooygRyTti9TEmGsHFs9XbAtiN/xX6ef14hwRAGvMTLvedaFybFj/R4kGi7veizzc29HSXeQp1Vg6z1E9AnhR8a/FgSU3J46iYKJZKzAgMBAAECggEABkXj5reu5O3Ic+9vjz0V8jjBcRVIlQSlm0qiYWpAGbB4UVVa/18qEiUvbcGcJy8ZSO9d9k3c1v/VqtgsP7KMcy9rXP+h5SOW5gWnBXC5rGuJWmYuAnnXkNdR9nsBOh9+Z34gcqonnbl0pIjBHbqKymSviejLjHEnf3NBoDB+wH2I0RNURoEnz2BnUs+NbYZiCyQqTcNUPStyiwzC4TlVpXrvkwB1opdlFdl3YBrE/jTRcEpyHPmAVofcHxcjHdkBkCipSoR7VKA+q/OKoH1fYQXduJm5e8wsdga5AodFWXlAnvVHcYSF1FtM++AXUF8RZ+DW1ghAhYLXXdNhPj+UoQKBgQDz/rB+h0lJgMnq6ug055zMBgSv9fU6wiM2F94Ukci6Q07e6s/YM17+ptdBP49/SLuyOcTj8YN1ipvCg2gg5Wa87bNPwFtXwOY8yyiHNUEkhCW+4TgPEENcEtAn6RHLr0OZ3/mqf4BLIAdT9ALIJVTPXo5loK/22fmN2kDPcfw33wKBgQC649JVAqHqZPCW6VWpRlOFVO9j3kfzHXDthVk9tyo1vm1wRg+ndA7MaZ4HwNg+FlJSUo2IRtjTOjN1SfIciqWcaNP12Doz4ttXizfwp0lc0zNV8pIJXg1w1QCushHtRWxSWn6vkz7eMee6Kl5gi5mJjbTCefSxVF+QP2kb2UpPrQKBgEPkjr/mbSIi0AyqKMrv19V9pzg/PGmJM7sNkSIwHqaVrBCjTgpe4QQC8MqbIRbN6kyBfqPexDGkzAqn/gWJT7kqe0apw8D7UBWCglh9HQ9IJiijCrJGWDf6lClG1UjUD/91L0eeCtjcpK1brJp8qMi7CuyEdfI+XQ53uKkAkqoJAoGARCwKc1wMeBXt9P6UcrTmjoJW+JUCpdodcWbtEKgZy5T1ErlbyHd7VnFIZgegWbWGveTDH+zH9vUKBES+/k5M7usGNm1zfa3I2Pw9oZlEF4oq9bUQND6MpJXuM8tc4rIqOzgrchxCGIcXwj7mQk1p6lU4lKAr8+uVGGcpDjGK+9kCgYBVjat0hFOlqDEu6pHCYCQt707sxd/ll24rNpzLu4viSGAUaFAhO1Pl4+RioAb3jck97LmVqu3UoMGrZHm5k+n2ejyTXHbUwBrkPQl8RdckDpHQGaW61XjY0149jkGxQ4DdZ8o3YQFXlWZMyKsClwOG0d5pJZQW8Zsn3mXifzgNDA==";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://app.issop.cn/kumai/getAlipayResult";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmsra5X2puymVyOv3ky/51At465V/imoNZlqaT18pC+8cLrdELjRw70mxUEj4/yZjZdM6Vb93hau5vDF4oba297LC/h2IU+sAux9/xtAmhFhIX+BH0coKDnH3wixjeW7I81luwv5uFOXadVCif7iuVNqOwPPhmmZ4NHYkF+HcNW3gM7x29jXN+QV8pRA2bCfqv9ZQCNUtEjDxUV3y4gO3Y2/7wEDfaGBpGRpSMS9uAELnftJJr3rTYiqwT24Q7sOBVMJsywjfts67FBE3glIg/66lEgSekUvS7Z+Amw09UK4OdxEdD3u8p35/M1ZJv1GqT8PSPM5H5llzBzpq1lwqiwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    public static void main(String [] args) {
        //实例化客户端   app支付
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID, RSA_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo("123456789");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("商户外网可以访问的异步地址");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println("返回值:"+response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
