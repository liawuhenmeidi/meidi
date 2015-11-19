package com.zhilibao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import utill.StringUtill;
import wilson.upload.ExcelUpload;
import wilson.upload.XLSReader;

import com.zhilibao.dao.TaxBasicMessageDao;
import com.zhilibao.model.Tax;
import com.zhilibao.model.TaxBasicMessage;
import com.zhilibao.service.TaxBasicMessageService;
import com.zhilibao.utill.QueryResult;
       
@Controller  //类似Struts的Action  
@RequestMapping("/meidiserver/admin/tax/")
public class TaxBasicMessageController {    
	 @Resource  
	private TaxBasicMessageService taxBasicMessageService;
	 
	protected static Log logger = LogFactory.getLog(TaxBasicMessageController.class);
	                       
	  //  @Authority(isverification=true,AuthorityMessage={AuthorityAll.TaxBasicMessage+"_"+DaoAuthority.write})       
	    @RequestMapping("add.do")             // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView add(@Param("gfmc") String gfmc){  
	    	 
	    	ModelAndView modelAndView = new ModelAndView();
	    	modelAndView.setViewName("tax/taxAdd");    
	    	
	    	Map<String,Object> params = new HashMap<String,Object>();
	    	QueryResult<TaxBasicMessage>  result = taxBasicMessageService.getList(params);
	    	     
	    	List<TaxBasicMessage> list = result.getRows(); 
	    	modelAndView.addObject("TaxBasicMessages", list); 
	    	  
	    	logger.info(gfmc);    
	    	if(!StringUtill.isNull(gfmc)){ 
	    		TaxBasicMessage TaxBasicMessage =taxBasicMessageService.getByName(gfmc);
	    		modelAndView.addObject("TaxBasicMessage",TaxBasicMessage); 
	    	
	    	} 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    }  
	      
	    @RequestMapping("save.do")  // 请求url地址映射，类似Struts的action-mapping             
	    public ModelAndView save(TaxBasicMessage tbm){  
	    	//taxBasicMessageDao.delete(tbm);   
	    	//taxBasicMessageDao.save(tbm);         
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
	    	
    
	    	  
	    	Map<String,Object> params = new HashMap<String,Object>();   
	    	QueryResult<TaxBasicMessage> result = taxBasicMessageService.getList(params);
	    	List<TaxBasicMessage> list = result.getRows();  
	    	modelAndView.addObject("TaxBasicMessages", list);   
	    	
	    	modelAndView.setViewName("tax/taxExchange");  
	    	
	    	modelAndView.addObject("filename", fileName);    
	    	 
	    	return modelAndView;  // 采用重定向方式跳转页面  
	    } 
	      
	   
	    
	 
}
