package com.kubian.mode.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String WX_APPID = "wx73c2d36878ef2aa7";
	public static final String WX_APPSECRET = "bb4c776019f9bf152d82617d463a1537";

	/**
	 * 微信网页授权流程: 1. 用户同意授权,获取 code 2. 通过 code 换取网页授权 access_token 3. 使用获取到的
	 * access_token 和 openid 拉取用户信息
	 * 
	 * @param code
	 *            用户同意授权后,获取到的code
	 * @param state
	 *            重定向状态参数
	 * @return
	 */
	@RequestMapping("/url")
	public String wecahtLogin(@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "state") String state) {

		// 1. 用户同意授权,获取code
		logger.info("收到微信重定向跳转.");
		logger.info("用户同意授权,获取code:{} , state:{}", code, state);

		// 2. 通过code换取网页授权access_token
		if (code != null || !(code.equals(""))) {

			String APPID = WX_APPID;
			String SECRET = WX_APPSECRET;
			String CODE = code;
			String WebAccessToken = "";
			String openId = "";
			String nickName, sex, openid = "";
			String REDIRECT_URI = "http://www.xxx.com/url";
			String SCOPE = "snsapi_userinfo";

			String getCodeUrl = UserInfoUtil.getCode(APPID, REDIRECT_URI, SCOPE);
			logger.info("第一步:用户授权, get Code URL:{}", getCodeUrl);

			// 替换字符串，获得请求access token URL
			String tokenUrl = UserInfoUtil.getWebAccess(APPID, SECRET, CODE);
			logger.info("第二步:get Access Token URL:{}", tokenUrl);

			// 通过https方式请求获得web_access_token
			String response = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

			JSONObject jsonObject = JSON.parseObject(response);
			logger.info("请求到的Access Token:{}", jsonObject.toJSONString());

			// {
			// "access_token":"ACCESS_TOKEN",
			// "expires_in":7200,
			// "refresh_token":"REFRESH_TOKEN",
			// "openid":"OPENID",
			// "scope":"SCOPE",
			// "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
			// }

			if (null != jsonObject) {
				try {

					WebAccessToken = jsonObject.getString("access_token");
					openId = jsonObject.getString("openid");
					logger.info("获取access_token成功!");
					logger.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);

					// 3. 使用获取到的 Access_token 和 openid 拉取用户信息
					String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
					logger.info("第三步:获取用户信息的URL:{}", userMessageUrl);

					// 通过https方式请求获得用户信息响应
					String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

					JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);

					logger.info("用户信息:{}", userMessageJsonObject.toJSONString());
					// {
					// "openid":" OPENID",
					// "nickname": NICKNAME,
					// "sex":"1",
					// "province":"PROVINCE"
					// "city":"CITY",
					// "country":"COUNTRY",
					// "headimgurl": "http://wx.qlogo.cn/mmopen/g3MoCfHe/46",
					// "privilege":[
					// "PRIVILEGE1"
					// "PRIVILEGE2"
					// ],
					// "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
					// }

					if (userMessageJsonObject != null) {
						try {
							// 用户昵称
							nickName = userMessageJsonObject.getString("nickname");
							// 用户性别
							sex = userMessageJsonObject.getString("sex");
							sex = (sex.equals("1")) ? "男" : "女";
							// 用户唯一标识
							openid = userMessageJsonObject.getString("openid");

							logger.info("用户昵称:{}", nickName);
							logger.info("用户性别:{}", sex);
							logger.info("OpenId:{}", openid);
						} catch (JSONException e) {
							logger.error("获取用户信息失败");
						}
					}
				} catch (JSONException e) {
					logger.error("获取Web Access Token失败");
				}
			}
		}
		return "登录成功";
	}
}
