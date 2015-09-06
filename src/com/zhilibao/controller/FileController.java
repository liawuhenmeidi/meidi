package com.zhilibao.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zhilibao.dao.TaxBasicMessageDao;
import com.zhilibao.function.export.TaxExchange;
import com.zhilibao.model.TaxBasicMessage;
import com.zhilibao.service.TaxBasicMessageService;

      
@Controller  //类似Struts的Action  
@RequestMapping("/meidiserver/admin/file/")
public class FileController {    
	      
	protected static Log logger = LogFactory.getLog(FileController.class);
	
    @Resource
	private TaxBasicMessageService taxBasicMessageService;
	  
	    @RequestMapping("tax.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView tax(){   
	    	ModelAndView modelAndView = new ModelAndView();
	    	modelAndView.setViewName("tax/taxExchange");  
	    	 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	   
	    
	    @RequestMapping("taxExchange.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView taxExchange(HttpServletResponse response,@Param("gfmc") String gfmc,@Param("Djh") String Djh,@Param("filename") String filename){ 
	    	   
		    	   
	    	TaxBasicMessage TaxBasicMessage = taxBasicMessageService.getByName(gfmc);
    		 
    		logger.info("gfmc"+gfmc); 
    		
		    	if(null == TaxBasicMessage){  
		    		Map<String, Object> context = new HashMap<String, Object>(); 
			    	context.put("message","请维护税务基本信息");   
			    	ModelAndView modelAndView = new ModelAndView("jieguo",context); 
			    	return modelAndView; 
		    	}else {   
		    		TaxExchange.export(response, TaxBasicMessage,gfmc,filename,Djh);  
		    	}
		    	
		    	
	    	
	    	//ModelAndView modelAndView = new ModelAndView();     
	    	//List<UploadOrder> list = UploadManager.getTotalUploadOrders(orderName); 
	    	//List<String> orderNames = UploadManager.getUnTotalUploadOrdersNames();
	    	   
	    	//
	    	//modelAndView.addObject("UploadOrders", list);   
	    	//modelAndView.addObject("orderNames", orderNames);  
	    	//modelAndView.addObject("orderName", orderName);  
	    	//modelAndView.setViewName("tax/taxExchange");  
	    	   
	    	return null;  // 采用重定向方式跳转页面  
	    } 
	    
	 
}
