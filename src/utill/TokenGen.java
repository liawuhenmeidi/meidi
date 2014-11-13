package utill;

import java.util.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import filter.EncodingFilter;
 
public class TokenGen {
	
	protected static Log logger = LogFactory.getLog(TokenGen.class);
    private static TokenGen instance = new TokenGen();
 
    private TokenGen() {}
 
    public static TokenGen getInstance() {
        return instance;
    }
 
    public synchronized boolean isTokenValid(HttpServletRequest request) {
        // 没有session,判为非法
        HttpSession session = request.getSession(false); 
        if (session == null)
            return false;
 
        // session中不含token,
        // 说明form被提交过后执行了resetToken()清除了token
        // 判为非法
        String stoken = (String) session.getAttribute("token"); 
        if (stoken == null)
            return false;
 
        // request请求参数中不含token,
        // 判为非法
        String rtoken = request.getParameter("token");
        //String rtoken = (String)request.getAttribute("token"); 
        
        if (rtoken == null)
            return false;
 
        // request请求中的token与session中保存的token不等,判为非法
        return stoken.equals(rtoken);
    }
     
    public synchronized boolean isTokenValidNew(HttpServletRequest request) {
    	boolean flag = false ;
    	 
    	String savedToken = "";
    	String requestToken=request.getParameter("token");
    	
    	
    	if(null != request.getSession().getAttribute("token")){
    		savedToken=request.getSession().getAttribute("token").toString();
    	}
    	
    	if(StringUtill.isNull(requestToken)){
    		return true ;
    	} 
    	//String requestToken=request.getParameter("token");

    	request.getSession().removeAttribute("token");
    	
    	logger.info(savedToken);
    	logger.info(requestToken);
    	if(savedToken.equals(requestToken))
    	{
    	   flag = true ;
    	}
    	
    	return flag ;
    }
    
    /*
     * 重新设置token，当页面被请求后，将session中的token属性去除
     */
    public synchronized void resetToken(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if (session!=null)
        {
            session.removeAttribute("token");
        }
    }
    /*
     * 为请求新建一个token标记，此标记由一个随机的double数toString形成，并把字符值存入session中
     */
      
    public synchronized void saveToken(HttpServletRequest request)
    {
        HttpSession session = request.getSession(true);
        Random rand = new Random();
        Double d = rand.nextDouble();
        session.setAttribute("token", d.toString()); 
        request.setAttribute("token", d.toString()); 
    }
}
