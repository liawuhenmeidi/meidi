package com.zhilibao.base;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhilibao.service.DataExistsException;
import com.zhilibao.service.ResourceExistsException;
import com.zhilibao.utill.Result;


public class BaseController {
	
	protected Log logger = LogFactory.getLog(getClass());
 
	@Resource
	private MessageSource messageSource;
	
	@ExceptionHandler
	@ResponseBody
	public Result exception(Exception e) {
		
		String msg = null;
		if (e instanceof IllegalArgumentException) {
			msg = e.getMessage();
		} 
		else if (e instanceof ResourceExistsException) {
			msg = e.getMessage();
		} 
		else if (e instanceof DataExistsException) {
			msg = e.getMessage();
		}
		else {
			logger.error("系统出错", e);
			msg = "system.error";
		}
		
		String message = null;
		
		try {
			message = this.messageSource.getMessage(msg, null, null);
		} catch (NoSuchMessageException nsme) {
			message = msg;
		}
				
		return new Result(message);
	}
	
}
