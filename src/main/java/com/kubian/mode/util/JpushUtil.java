package com.kubian.mode.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

@SuppressWarnings("deprecation")
public class JpushUtil {
	private static final String APPKEY = "4eab229f125ba1d31d792b71";  //必填，例如466f7032ac604e02fb7bda89 
	private static final String MSECRET = "aad6bddfc4cd9f142d07ea94";  //必填，每个应用都对应一个masterSecret
	private static JPushClient jpushClient;
	static{
		 jpushClient = new JPushClient(MSECRET, APPKEY, 3);
	}
	/**
	 * 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为msg
	 * @param msg
	 * @return
	 */
	public static PushPayload buildPushObject_all_alias_alert(String alias,String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience("all".equals(alias)?Audience.all():Audience.alias(alias.split(",")))
                .setNotification(Notification.alert(msg))
                .setOptions(Options.newBuilder()
                         .setApnsProduction(true).build())
                .build();
    }
	/**
	 * 推送消息
	 * @param alias
	 * @param msg
	 * @return
	 */
	public static PushResult sendMsg(String alias,String msg){
		PushPayload payload = null;
		PushResult result = null;
		System.out.println(alias+"---推送内容----"+msg);
		if(alias == null || alias == ""){
			payload = buildPushObject_all_alias_alert("all",msg);
		}else{
			payload = buildPushObject_all_alias_alert(alias,msg);
		}
		try {
			result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		PushResult result = sendMsg("newton,admin,18674831237,13142183326,18012086060,15674903694,21677168,13299853737,15211188914,13142183323,18627591800","测试-------111111111-----");
		System.out.println(result.msg_id);
	}
}
