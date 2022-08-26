package com.kubian.mode.util.alipay;

public class UnicodeUtil {
	//转换为unicode  
	public static String encodeUnicode(final String gbString) {     
	        char[] utfBytes = gbString.toCharArray();     
	        String unicodeBytes = "";     
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);     
	            if (hexB.length() <= 2) {     
	                hexB = "00" + hexB;     
	            }     
	            unicodeBytes = unicodeBytes + "\\u" + hexB;     
	        }     
	        return  unicodeBytes;
	    }   
	
	
	//unicode转化汉字     
    public static String decodeUnicode(final String dataStr) {     
        final StringBuffer buffer = new StringBuffer();     
        String tempStr = "";     
        String operStr = dataStr;     
        if (operStr != null && operStr.indexOf("\\u") == -1)     
            return buffer.append(operStr).toString();     
        if (operStr != null && !operStr.equals("")     
                && !operStr.startsWith("\\u")) {     
            tempStr = operStr.substring(0, operStr.indexOf("\\u"));     
            operStr = operStr.substring(operStr.indexOf("\\u"), operStr.length());// operStr字符一定是以unicode编码字符打头的字符串     
        }     
        buffer.append(tempStr);     
        // 循环处理,处理对象一定是以unicode编码字符打头的字符串     
        while (operStr != null && !operStr.equals("")&& operStr.startsWith("\\u")) {      
            tempStr = operStr.substring(0, 6);     
            operStr = operStr.substring(6, operStr.length());     
            String charStr = "";     
            charStr = tempStr.substring(2, tempStr.length());     
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
            buffer.append(new Character(letter).toString());     
            if (operStr.indexOf("\\u") == -1) {     
                buffer.append(operStr);     
            } else { // 处理operStr使其打头字符为unicode字符     
                tempStr = operStr.substring(0, operStr.indexOf("\\u"));     
                operStr = operStr.substring(operStr.indexOf("\\u"), operStr.length());     
                buffer.append(tempStr);     
            }     
        }     
        return buffer.toString();     
    }   
}
