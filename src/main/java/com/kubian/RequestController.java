package com.kubian;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转到登录页面
 * ClassName: RequestController 
 * @Description: TODO
 * @author HD
 * @date 2018年4月28日
 */
@Controller
public class RequestController {
	@RequestMapping("/")
    public String index() {
        return "forward:/login1.html";
    }
	
	@RequestMapping("/tes2")
    public String test1() {
        return "forward:/test1.html";
    }
}
