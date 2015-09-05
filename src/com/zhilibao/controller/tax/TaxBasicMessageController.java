package com.zhilibao.controller.tax;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utill.StringUtill;
import wilson.upload.ExcelUpload;
import wilson.upload.XLSReader;
import com.zhilibao.mapper.TaxBasicMessageOperation;
import com.zhilibao.model.tax.Tax;
import com.zhilibao.model.tax.TaxBasicMessage;
       
@Controller  //类似Struts的Action  
@RequestMapping("/meidiserver/admin/tax/")
public class TaxBasicMessageController {    
	@Autowired
	private TaxBasicMessageOperation taxBasicMessageOperation;
	 
	protected static Log logger = LogFactory.getLog(TaxBasicMessageController.class);
	                      
	  //  @Authority(isverification=true,AuthorityMessage={AuthorityAll.TaxBasicMessage+"_"+OperationAuthority.write})       
	    @RequestMapping("add.do")             // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView add(@Param("gfmc") String gfmc){                              
	    	ModelAndView modelAndView = new ModelAndView();
	    	modelAndView.setViewName("tax/taxAdd");  
	    	List<TaxBasicMessage> list = taxBasicMessageOperation.getList();
	    	modelAndView.addObject("TaxBasicMessages", list); 
	    	logger.info(gfmc); 
	    	if(!StringUtill.isNull(gfmc)){
	    		TaxBasicMessage TaxBasicMessage =taxBasicMessageOperation.getByName(gfmc);
	    		modelAndView.addObject("TaxBasicMessage",TaxBasicMessage); 
	    	
	    	} 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	     
	    @RequestMapping("save.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView save(TaxBasicMessage tbm){  
	    	taxBasicMessageOperation.delete(tbm);   
	    	taxBasicMessageOperation.save(tbm);         
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
	    	
    
	    	
            
	    	List<TaxBasicMessage> list = taxBasicMessageOperation.getList();
	    	modelAndView.addObject("TaxBasicMessages", list); 
	    	
	    	modelAndView.setViewName("tax/taxExchange");  
	    	
	    	modelAndView.addObject("filename", fileName);    
	    	 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	      
	   
	    
	 
}
