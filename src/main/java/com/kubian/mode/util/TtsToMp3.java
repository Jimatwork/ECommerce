package com.kubian.mode.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;


public class TtsToMp3 {
    //设置APPID/AK/SK
    public static final String APP_ID = "11024729";
    public static final String API_KEY = "qAtEx4OR1XGzGwrC84q32pKF";
    public static final String SECRET_KEY = "00661217d0970a79218a4593c04afdcf";
    // 初始化一个AipSpeech
    public static AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
    static{
    	// 可选：设置网络连接参数
    	client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
    }
    public static void toMp3(String str,String url) {
        // 调用接口
    	byte[] data = new byte[1];
    	int i=0,len=500;
    	for(;i+len<=str.length();i+=len){
    		String tmpstr = str.substring(i, i+len);
//    	    System.out.println(i+":"+tmpstr);
    	    TtsResponse res = client.synthesis(tmpstr, "zh", 1, null);
    	    data = TtsToMp3.unitByteArray(data,res.getData());
    	}
    	
    	String tmpstr = str.substring(i, str.length());
    	if(tmpstr != null || !"".equals(tmpstr)){
    		TtsResponse res = client.synthesis(tmpstr, "zh", 1, null);
    		System.out.println("2----------"+tmpstr);
    		data = TtsToMp3.unitByteArray(data,res.getData());
    	}
    	System.out.println("3----------"+data.length);
        //JSONObject res1 = res.getResult();
        if (data.length > 1) {
            try {
                Util.writeBytesToFileSystem(data, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if (res1 != null) {
//            System.out.println(res1.toString(2));
//        }
    }
    
    /**
     * 合并byte数组
     */
    public static byte[] unitByteArray(byte[] byte1,byte[] byte2){
        byte[] unitByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return unitByte;
    }
    
    
    
    public static void main(String[] args) {
//    	String a = "未来三年";
//    	int i=0,len=6;
//    	for(;i+len<=a.length();i+=len){
//    	    System.out.println(a.substring(i, i+len));
//    	}
//    	System.out.println("-------"+a.substring(i, a.length()));
    	String str = "未来三年，历史性地解决中华民族千百年来的绝对贫困问题，让现行标准下的贫困人口同全国人民一道迈入小康社会——新一轮脱贫攻坚战，已到后半程，更是冲刺期。全国两会期间，习近平总书记参加内蒙古代表团审议时强调，打好脱贫攻坚战，关键是打好深度贫困地区脱贫攻坚战，关键是攻克贫困人口集中的乡（苏木）村（嘎查）。瞄准特定贫困群众精准帮扶，向深度贫困地区聚焦发力，各地各部门实招硬招迭出，以非常之策，解非常之困，坚决打好打赢精准脱贫攻坚这场历史性决战。以最实作风“啃”最硬骨头抬头是山，低头还是山，武陵山深处的湖南省泸溪县洗溪镇宋家寨村，距县城不过25公里，车行却足足1小时。";
    	String mp3url = "C:/gz/MyEcpliseWork/KuBian/target/classes/static/KuBianImg/mp3";
    	File file = new File(mp3url);
		if (!file.exists()) {
			file.mkdirs();
		}
    	TtsToMp3.toMp3(str,mp3url+"/123333.mp3");
    	
    	
	}
}