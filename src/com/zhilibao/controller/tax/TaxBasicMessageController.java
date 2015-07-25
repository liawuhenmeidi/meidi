package com.zhilibao.controller.tax;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utill.StringUtill;

import com.zhilibao.config.Configs;
import com.zhilibao.filter.authority.Authority;
import com.zhilibao.mapper.ConfigsOperation;
import com.zhilibao.model.tax.TaxBasicMessage;
import com.zhilibao.service.MapperService;
import com.zhilibao.utill.bean.BeanUtill;
     
@Controller  //类似Struts的Action  
@RequestMapping("/meidiserver/admin/tax/")
public class TaxBasicMessageController {    
	     
	protected static Log logger = LogFactory.getLog(TaxBasicMessageController.class);
	                
	    @Authority(isverification=true)                
	    @RequestMapping("add.do")             // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView add(){                        
	    	ModelAndView modelAndView = new ModelAndView();
	    	                       
	    	modelAndView.setViewName("tax/taxAdd");          
	    	ConfigsOperation lmo = MapperService.getConfigsOperation();
	    //	logger.info("******************8888");      
	    //	logger.info("%%%%%%%%%%%%%*******"+Configs.class.getName());
	    	Configs cf = lmo.getByName(TaxBasicMessage.class.getName()); 
	    	String message = cf.getMessage();    
	    	//JSONObject object = (JSONObject)message; 
	    	JSONObject jsObj = JSONObject.fromObject(message);
	    	logger.info(message);                                   
	    	      
	    	      
	    	modelAndView.addObject("TaxBasicMessage", jsObj);  
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	             
	    @RequestMapping("save.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView save(TaxBasicMessage tbm){  
	    	String message= StringUtill.GetJson(tbm); 
	    	
	    	Configs cg = new Configs(); 
	    	cg.setMessage(message);
	    	cg.setName(BeanUtill.getBeanName(tbm));
	    	  
	    	ConfigsOperation lmo = MapperService.getConfigsOperation();
	    	 
	    	lmo.save(cg);      
	    	logger.info(message);   
	    	ModelAndView modelAndView = new ModelAndView();
             
	    	modelAndView.setViewName("jieguo");  
	    	modelAndView.addObject("message", "保存成功");  
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	 
}
