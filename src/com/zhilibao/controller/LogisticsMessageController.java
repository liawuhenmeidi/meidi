package com.zhilibao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

   

@Controller  //类似Struts的Action
public class LogisticsMessageController {   
	    @RequestMapping("meidiserver/admin/logistics/add")  // 请求url地址映射，类似Struts的action-mapping
   
	    public ModelAndView add(){
	        // request和response不必非要出现在方法中，如果用不上的话可以去掉
	        // 参数的名称是与页面控件的name相匹配，参数类型会自动被转换
	    	ModelAndView modelAndView = new ModelAndView();  
	    	modelAndView.setViewName("logistics");   
	        return modelAndView;  // 采用重定向方式跳转页面
	        // 重定向还有一种简单写法 
	        // return new ModelAndView("redirect:../index.jsp");
	    }
	 
	   
}
