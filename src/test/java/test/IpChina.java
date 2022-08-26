package test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class IpChina {
	private static final String ipUrl = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
	private static final String China = "\u4e2d\u56fd";
	private static final String Taiwan = "\u53f0\u6e7e";
	private static final String HongKong = "\u9999\u6e2f";
	private static final String Macao = "\u6fb3\u95e8";

	public static boolean ipIsChinaInland() {
		try {
			String json = getIpData();
			System.out.println("json=" + json);
			JSONObject jsonObject = new JSONObject(json);
			String country = jsonObject.getString("country");
			String city = jsonObject.getString("province");
			if (country != null && city != null) {
				if (country.equals(China)) {
					// if (city.equals(Taiwan) || city.equals(HongKong) ||
					// city.equals(Macao)) {
					// // deal with non-china inland
					// } else {
					// // deal with china inland
					// return true;
					// }
					return true;
				} else {
					// deal with non-china inland
				}
			} else {
				// deal with non-china inland
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private static String getIpData() throws Exception {
		HttpGet httpRequest = new HttpGet(ipUrl);// 建立http get联机
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);// 发出http请求
		if (httpResponse.getStatusLine().getStatusCode() == 200)
			return EntityUtils.toString(httpResponse.getEntity());// 获取相应的字符串
		return null;
	}

	public static void main(String[] args) {
		System.out.println(ipIsChinaInland());
	}
}
