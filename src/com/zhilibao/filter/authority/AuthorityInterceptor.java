package com.zhilibao.filter.authority;
 
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
 
import user.User;
import utill.StringUtill;
 
  
public class AuthorityInterceptor extends HandlerInterceptorAdapter {
	protected static Log logger = LogFactory.getLog(AuthorityInterceptor.class);
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(handler instanceof HandlerMethod);  
    	
        User user = (User)request.getSession().getAttribute("user"); 
        logger.info(user);  
        if (handler instanceof HandlerMethod) {  
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            /*logger.info(handlerMethod.getBeanType().getMethods());  
            
            for(int i=0;i<handlerMethod.getBeanType().getMethods().length;i++){
            	logger.info(handlerMethod.getBeanType().getMethods()[i]);
            }  */
            Method method = handlerMethod.getMethod()  ;
    
           //logger.info(method.getAnnotations() );  
            
          /* for(int i=0;i<method.getAnnotations().length;i++){
           	logger.info(method.getAnnotations()[i]);  
           } */
            Authority annotation = method.getAnnotation(Authority.class);
           // logger.info(annotation);
            if (annotation != null) {  
                boolean needAuthority = annotation.isverification();
                if (needAuthority) {
                	boolean flag = check(user,annotation);
                	if(flag){
                		return true ;
                	}else {
                		return true ;
                		// response.sendRedirect("");
                		 
                	}
                	//return true ;  
                }  
        
            }
            return true;
        } else {  
        	//logger.info(super.preHandle(request, response, handler));
        	
            return super.preHandle(request, response, handler);
        } 
    }
  
    
     
    private boolean check(User user ,Authority annotation){
    	boolean flag = true ; 
    	Map<String, List<String>> aosmap =  user.getAuthority();
    	String[] aoss = annotation.AuthorityMessage();
    	if(null != aoss && aoss.length >1){
    		for(int i=0;i<aoss.length;i++){
    			String aos = aoss[i]; 
    			String[] aosp = aos.split("_");
    			String ao = aosp[0];
    			String op = aosp[1];
    			
    			if(null == aosmap){
    				return false ;
    			}else {
    				 List<String> li = aosmap.get(ao);
    				 if(!StringUtill.isNull(op)){
    					 if(!li.contains(op)){
    						 return false ;
    					 }
    				 }
    			}
    			
    		}
    	}else {
    		return false ;
    	}
    	return flag ;
    } 
    
    
    public void postHandle(    
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)    
            throws Exception {
    	
    	logger.info("postHandle");
    }    
    public void afterCompletion(    
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)    
            throws Exception {  
    	logger.info("afterCompletion");
    }  
    
    
}
