package com.zhilibao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import user.User;
import user.UserService;
import utill.StringUtill;
import utill.TimeUtill;

import branch.Branch;
import branch.BranchManager;
import branchtype.BranchType;
import branchtype.BranchTypeManager;

import com.zhilibao.model.Cars;
import com.zhilibao.model.LogisticsMessage;
import com.zhilibao.service.MapperService;
import com.zhilibao.token.Token;
       
@Controller  //类似Struts的Action 
@RequestMapping("/meidiserver/admin/logistics/")
public class LogisticsMessageController {  
	 protected static Log logger = LogFactory.getLog(LogisticsMessageController.class);
	  
	     
	    @Token(save=true)    
	    @RequestMapping("add.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView add(HttpSession session,@Param("pid")String pid){  
	        // request和response不必非要出现在方法中，如果用不上的话可以去掉 
	        // 参数的名称是与页面控件的name相匹配，参数类型会自动被转换 
	    	ModelAndView modelAndView = new ModelAndView();  
	    	User user = (User)session.getAttribute("user");
	    	//logger.info(user);    
	    	
	    	
	    	List<Cars> cars = MapperService.getCarsOperation().getlist();
	    	//logger.info(cars);    
	    	List<User>  users= UserService.getLogistics(user);
	    	//logger.info(users);  
	    	List<BranchType> BranchTypes = BranchTypeManager.getLocate();  
	    	
	    	Map<String,List<Branch>> map = BranchManager.getLocateMapBranch(); 
	    	//Map<String,List<Branch>> map = BranchService.g
	    	String mapjosn = StringUtill.GetJson(map);
	    	String time = TimeUtill.getdateString(); 
	    	String submittime = TimeUtill.gettime();
	    	if(StringUtill.isNull(pid)){ 
	    		pid = "0"; 
	    	}         
	    	logger.info(pid); 
	    	modelAndView.setViewName("logistics/logistics"); 
	    	modelAndView.addObject("cars", cars);  
	    	modelAndView.addObject("users", users);     
	    	modelAndView.addObject("BranchTypes", BranchTypes);
	    	modelAndView.addObject("mapjosn", mapjosn);
	    	modelAndView.addObject("time", time);
	    	modelAndView.addObject("submittime", submittime);
	    	modelAndView.addObject("pid", pid); 
	        return modelAndView;  // 采用重定向方式跳转页面  
	        // 重定向还有一种简单写法        
	        // return new ModelAndView("redirect:../index.jsp");
	    } 
	      
	     @RequestMapping("save.do")  // 请求url地址映射，类似Struts的action-mapping
	   //  @Token(remove=true)           
	    public ModelAndView save(LogisticsMessage lm){  
	        // request和response不必非要出现在方法中，如果用不上的话可以去掉
	        // 参数的名称是与页面控件的name相匹配，参数类型会自动被转换  
	    	logger.info("save");  
	    	MapperService.getLogisticsMessageOperation().sava(lm);
	    	 
	    	 
	    	 
	    	Map<String, Object> context = new HashMap<String, Object>(); 
	    	context.put("message","保存成功");  
	    	ModelAndView modelAndView = new ModelAndView("jieguo",context);
	        return modelAndView;  // 采用重定向方式跳转页面
	        // 重定向还有一种简单写法  
	        // return new ModelAndView("redirect:../index.jsp");
	    }
	   
}
