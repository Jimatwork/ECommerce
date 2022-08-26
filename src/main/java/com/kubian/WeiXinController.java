package com.kubian;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.util.HttpXmlClient;

import net.sf.json.JSONObject;

/**
 * 获得微信分享的签名 ClassName: WeiXinController
 * 
 * @Description: TODO
 * @author HD
 * @date 2018年7月19日
 */
@RestController
public class WeiXinController {
	/**
	 * 获得微信分享的签名
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return Object
	 * @throws @author
	 *             HD
	 * @date 2018年7月19日
	 */
	@RequestMapping(value = "/getWeiXinQianMing")
	public Object getWeiXinQianMing() {
		// 获取access_token
		Map<String, String> params = new HashMap<String, String>();
		params.put("corpid", "wx73c2d36878ef2aa7");
		params.put("corpsecret", "f28f6f85205818454e8aa175dd916fd6");
		String xml = HttpXmlClient.post("https://qyapi.weixin.qq.com/cgi-bin/gettoken", params);
		JSONObject jsonMap = JSONObject.fromObject(xml);
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> it = jsonMap.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String u = jsonMap.get(key).toString();
			map.put(key, u);
		}
		String access_token = map.get("access_token");
		System.out.println("access_token=" + access_token);

		// 获取ticket
		params.put("access_token", access_token);
		xml = HttpXmlClient.post("https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket", params);
		jsonMap = JSONObject.fromObject(xml);
		map = new HashMap<String, String>();
		it = jsonMap.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String u = jsonMap.get(key).toString();
			map.put(key, u);
		}
		String jsapi_ticket = map.get("ticket");
		System.out.println("jsapi_ticket=" + jsapi_ticket);

		// 获取签名signature
		String noncestr = UUID.randomUUID().toString();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String url = "http://mp.weixin.qq.com";
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;
		// sha1加密
		String signature = HttpXmlClient.SHA1(str);
		System.out.println("noncestr=" + noncestr);
		System.out.println("timestamp=" + timestamp);
		System.out.println("signature=" + signature);
		// 最终获得调用微信js接口验证需要的三个参数noncestr、timestamp、signature
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("noncestr", noncestr);
		map2.put("timestamp", timestamp);
		map2.put("signature", signature);
		map2.put("weixinId", "wx73c2d36878ef2aa7");
		return map2;
	}
}
