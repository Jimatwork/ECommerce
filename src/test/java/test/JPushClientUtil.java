package test;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class JPushClientUtil {
	// 在极光注册上传应用的 appKey 和 masterSecret
	private static final String appKey = "4eab229f125ba1d31d792b71";//// 必填，例如466f7032ac604e02fb7bda89
	private static final String masterSecret = "aad6bddfc4cd9f142d07ea94";// 必填，每个应用都对应一个masterSecret

	public static void main(String[] args) throws Exception {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_ios_tagAnd_alertWithExtrasAndMessage();

		PushResult result = jpushClient.sendPush(payload);
		System.out.println(result.msg_id);
		System.out.println(result);
	}

	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
		return PushPayload.newBuilder().setPlatform(Platform.ios())
				.setAudience(Audience.alias("1647af0e50e4444dbe05b3d7b79dadfd"))
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder().setAlert("大家好，我来推个消息").setSound("happy")
								.addExtra("issueId", "xxx").addExtra("type", "我是问题").build())
						.build())
				.setMessage(Message.content("hgg推车了")).setOptions(Options.newBuilder().setApnsProduction(false).build())
				.build();
	}
}
