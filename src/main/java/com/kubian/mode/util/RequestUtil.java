package com.kubian.mode.util;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.URL;
import java.util.Enumeration;

/**
 * Convenience class for setting and retrieving cookies.
 */
public final class RequestUtil {
    private static final Log log = LogFactory.getLog(RequestUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private RequestUtil() {
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name the name of the cookie
     * @param value the value of the cookie
     * @param path the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, String path) {
        if (log.isDebugEnabled()) {
            log.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(3600 * 24 * 30); // 30 days

        response.addCookie(cookie);
    }

    /**
     * Convenience method to get a cookie by name
     *
     * @param request the current request
     * @param name the name of the cookie to find
     *
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if (cookies == null) {
            return returnCookie;
        }

        for (Cookie thisCookie : cookies) {
            if (thisCookie.getName().equals(name)) {
                // cookies with no value do me no good!
                if (!thisCookie.getValue().equals("")) {
                    returnCookie = thisCookie;

                    break;
                }
            }
        }

        return returnCookie;
    }

    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie the cookie to delete
     * @param path the path on which the cookie was set (i.e. /appfuse)
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }

    /**
     * Convenience method to get the application's URL based on request
     * variables.
     * 
     * @param request the current request
     * @return URL to application
     */
    public static String getAppURL(HttpServletRequest request) {
    	return getServerURL(request) + request.getContextPath();
    }
    public static String getServerURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        System.out.println("scheme-----------"+scheme);
        url.append(scheme);
        url.append("://");
        //url.append(request.getServerName());
        String host = request.getServerName();
        try {
            if ("localhost".equals(host)) {
                host = java.net.InetAddress.getLocalHost().getHostAddress();
            }
            if ("127.0.0.1".equals(host)) {
            	//Linux???????????????????????????
            	for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            			e.hasMoreElements();) {
            		InetAddress addr = e.nextElement().getInetAddresses().nextElement();
            		if (addr.isSiteLocalAddress() && !addr.isLoopbackAddress() &&
            				addr.getHostAddress().indexOf(":") == -1) {
            			host = addr.getHostAddress();
            			break;
            		}
            	}
            }
        } catch(Exception e) {
        	e.printStackTrace();
        	host = "21ad.net";
        }
        url.append(host);
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        
        return url.toString();
    }
    
	/**
	 * ??????https??????
	 * @param requestUrl ????????????
	 * @param requestMethod ???????????????GET???POST???
	 * @param outputStr ???????????????
	 * @return ????????????????????????????????????
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			// ??????SSLContext?????????????????????????????????????????????????????????
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ?????????SSLContext???????????????SSLSocketFactory??????
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// ?????????????????????GET/POST???
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
			// ???outputStr??????null????????????????????????
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// ??????????????????
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// ??????????????????????????????
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// ????????????
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			log.error("???????????????{}", ce);
		} catch (Exception e) {
			log.error("https???????????????{}", e);
		}
		return null;
	}

	public static String urlEncodeUTF8(String source){
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	 }
}
