package filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import user.User;

public class AdminFilter implements Filter {
	
	protected static Log logger = LogFactory.getLog(AdminFilter.class);
	
	public void destroy() {
logger.info("authfilter destroy");
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
//logger.info("authfilter dofilter"); 
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		HttpSession session = request.getSession(false);
		
		if(session == null || (User)session.getAttribute("user") == null) {
				response.sendRedirect( request.getContextPath() + "/meidiserver/login.jsp");
				return;	
		}	
			
		chain.doFilter(req, resp);
	}
 
	public void init(FilterConfig config) throws ServletException {
logger.info("authfilter init"); 
	}

}
