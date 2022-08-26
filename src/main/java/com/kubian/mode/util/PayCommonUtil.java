package com.kubian.mode.util;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCommonUtil {
	private static Logger log = LoggerFactory.getLogger(PayCommonUtil.class);

	public static String API_KEY = "wx73c2d36878ef2aa7";

	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}
	/**
	 * @author 李欣桦
	 * @date 2014-12-5下午2:29:34
	 * @Description：sign签名
	 * @param characterEncoding 编码格式
	 * @param parameters 请求参数
	 * @return
	 */
	public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=O636hQa3lHvHAleTed8tIybTyQG52YuD");
		System.out.println(sb.toString());
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * 验证回调签名
	 * @return
	 */
	public static boolean isTenpaySign(Map<String, String> map) {
		String characterEncoding="utf-8";
		String charset = "utf-8";
		String signFromAPIResponse = map.get("sign");
		if (signFromAPIResponse == null || signFromAPIResponse.equals("")) {
			System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
			return false;
		}
		System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);
		//过滤空 设置 TreeMap
		SortedMap<String,String> packageParams = new TreeMap();

		for (String parameter : map.keySet()) {
			String parameterValue = map.get(parameter);
			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}

		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();

		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);

		//将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		//算出签名
		String resultSign = "";
		String tobesign = sb.toString();

		if (null == charset || "".equals(charset)) {
			resultSign = MD5Util.MD5Encode(tobesign, characterEncoding).toUpperCase();
		}else{
			try{
				resultSign = MD5Util.MD5Encode(tobesign, characterEncoding).toUpperCase();
			}catch (Exception e) {
				resultSign = MD5Util.MD5Encode(tobesign, characterEncoding).toUpperCase();
			}
		}

		String tenpaySign = ((String)packageParams.get("sign")).toUpperCase();
		return tenpaySign.equals(resultSign);
	}

	/**
	 * @author 李欣桦
	 * @date 2014-12-5下午2:32:05
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters  请求参数
	 * @return
	 */
	public static String getRequestXml(SortedMap<Object,Object> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			System.out.println(k+":\""+v+"\"");
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	/**
	 * @author 李欣桦
	 * @date 2014-12-3上午10:17:43
	 * @Description：返回给微信的参数
	 * @param return_code 返回编码
	 * @param return_msg  返回信息
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
}
