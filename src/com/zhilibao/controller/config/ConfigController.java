package com.zhilibao.controller.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utill.StringUtill;
import wilson.upload.ExcelUpload;
import wilson.upload.UploadManager;
import wilson.upload.XLSReader;

import com.zhilibao.config.Configs;
import com.zhilibao.filter.authority.Authority;
import com.zhilibao.function.authority.AuthorityAll;
import com.zhilibao.function.authority.OperationAuthority;
import com.zhilibao.mapper.ConfigsOperation;
import com.zhilibao.mapper.TaxBasicMessageOperation;
import com.zhilibao.model.tax.Tax;
import com.zhilibao.model.tax.TaxBasicMessage;
import com.zhilibao.service.MapperService;
import com.zhilibao.utill.bean.BeanUtill;
       
@Controller  //类似Struts的Action  
@RequestMapping("/meidiserver/admin/config/") 
public class ConfigController {    

	  
	protected static Log logger = LogFactory.getLog(ConfigController.class);
	                       
	  //  @Authority(isverification=true,AuthorityMessage={AuthorityAll.TaxBasicMessage+"_"+OperationAuthority.write})       
	    @RequestMapping("SNSmallApplianceadd.do")
	    public ModelAndView add(@Param("remark") String remark){                              
	    	ModelAndView modelAndView = new ModelAndView();

	    	modelAndView.setViewName("config/SNSmallAppliance");  

	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	      
	    @RequestMapping("SNSmallAppliancesave.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView save(@Param("username")String username,@Param("password")String password){  
	    	 
	    	Configs config = new Configs();
	    	config.setName("SNSmallAppliance");
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("username", username);
	    	map.put("password", password);   
	    	config.setMessage(StringUtill.GetJson(map));
	    	//TaxBasicMessage TaxBasicMessage = MapperService.getTaxBasicMessageOperation().getByName(gfmc);
	    	//configsOperation.save(config);      
	    	ModelAndView modelAndView = new ModelAndView();
             
	    	modelAndView.setViewName("jieguo");  
	    	modelAndView.addObject("message", "保存成功");  
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	     
	    
	     
	    @RequestMapping("taxUpload.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView taxUpload(HttpServletRequest request){  
	    	   
	    	String fileName = request.getParameter("fileName");
	    	ModelAndView modelAndView = new ModelAndView();
	    	String filePath = ExcelUpload.getTaxFilePath();   
	    	try{
	    		List<Tax> li = new XLSReader().readTaxXML(filePath, fileName);
	    		modelAndView.addObject("list", li); 
	    	}catch(Exception e){
	    		
	    		modelAndView.addObject("message", "文件错误，请参考模板文件");  
	    		return modelAndView;  
	    	}
	    	
    
	    	
            
	    	List<TaxBasicMessage> list = MapperService.getTaxBasicMessageOperation().getList();
	    	modelAndView.addObject("TaxBasicMessages", list); 
	    	
	    	modelAndView.setViewName("tax/taxExchange");  
	    	
	    	modelAndView.addObject("filename", fileName);    
	    	 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	      
	   
	    
	 
}
