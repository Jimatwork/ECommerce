package com.kubian.mode.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kubian.UserController;

/**
 * Ajax跨域请求过滤器
 * @author WangW
 * @date 2017-12-21下午16:15:08
 */
@Component("myFilter")
public class MyFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	public void destroy() {
		// System.out.println("过滤器销毁");
	}

	public void doFilter(ServletRequest request, ServletResponse response1, FilterChain chain)
			throws IOException, ServletException {
		
		//log.info("执行过滤操作,请求者为:" + MyTools.getIpAddr((HttpServletRequest) request) + "_请求时间:"
		//		+ new Date().toLocaleString());

		HttpServletResponse response = (HttpServletResponse) response1;

		response.setHeader("Access-Control-Allow-Origin", "*");

		response.setHeader("Access-Control-Allow-Headers",
				"User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken");

		response.setHeader("Access-Control-Allow-Credentials", "true");

		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

		response.setHeader("Access-Control-Max-Age", "1209600");

		response.setHeader("Access-Control-Expose-Headers", "accesstoken");

		response.setHeader("Access-Control-Request-Headers", "accesstoken");

		response.setHeader("Expires", "-1");

		response.setHeader("Cache-Control", "no-cache");

		response.setHeader("pragma", "no-cache");

		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// System.out.println("过滤器初始化");
	}

}
