package com.kubian.mode.util.alipay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
 

/**
 * Created by dong on 2014/8/12.
 */
public class HttpUtil {

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }

    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    private static String doGet(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    private static String doPost(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 
     * @param paycolname 实际支付金额字段名
     * @param data       订单参数
     * @param total_fee  实际支付金额
     * @return
     */
    public static Map<String,String> getParamsMapByData(String paycolname,String  data,String total_fee){
    	
    	Integer fee= 0;
    	
    	if(!StringUtils.isEmpty(total_fee)){
    		fee =	(int) (Float.parseFloat(total_fee)*100);
    	}
    
    	
    	
    	
    	Map<String,String> params = new HashMap<String,String>();
    	
    	if(!StringUtils.isEmpty(data)){
    		
    		String[] pairedParams= data.split("&");
    		for (int i = 0; i < pairedParams.length; i++) {
    			String[] result=pairedParams[i].split("=");
    			
    			 //以支付宝实际交易金额作为本地系统的付款金额参与业务计算
    			 if(paycolname.equals(result[0])){
    			 	params.put(result[0],fee.toString());
    			 }else {
    				params.put(result[0], result[1]);
			     }
    			
    			
			}
    	}
    	
    	return params;
    	
    }
    
    public static void main(String[] args) {
    	
    	Map<String,String> params=new HashMap<>();
/*    	params.put("ip", "122.224.221.84");
    	params.put("ak", "6AgWwNFoRzuGgLLYxFV21Eii");
    	//System.out.println( doPost("http://api.map.baidu.com/location/ip",params,"UTF-8") );
    	
    	String jsonStr=doPost("http://api.map.baidu.com/location/ip",params,"UTF-8");
    	  
    	JSONObject jsonObject= JSONObject.parseObject(jsonStr);
    	
    
    	
    	 
    	
        System.out.println( JSONObject.parseObject(jsonObject.get("content").toString()).get("address_detail"));
    */
  
    	params.put("orderpay", "1400");
    	params.put("deposit", "1200");
    	params.put("orderno", "1804604971720165602035638");
    	params.put("recordid", "63");
 
     	params.put("paycallback", "true");
    	String jsonStr=doPost("http://localhost:8080/btkjsite/order/pay_order?orderuser=15&paytype=1",params,"UTF-8");
    	 
    	System.out.println(jsonStr);
    	
    }
}
