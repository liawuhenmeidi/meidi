package com.zhilibao.filter.authority;
 
import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
 
 
public class AuthorityInterceptor extends HandlerInterceptorAdapter {
	protected static Log logger = LogFactory.getLog(AuthorityInterceptor.class);
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(handler instanceof HandlerMethod);  
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
                boolean needSaveSession = annotation.isverification();
                if (needSaveSession) { 
                    request.getSession(false).setAttribute("token", UUID.randomUUID().toString());
                    logger.info(123);  
                } 
                boolean needRemoveSession = annotation.verification();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        return false;
                    }
                    request.getSession(false).removeAttribute("token");
                }
            }
            return true;
        } else {  
        	//logger.info(super.preHandle(request, response, handler));
            return super.preHandle(request, response, handler);
        } 
    }
 
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute("token");
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("token");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
}
