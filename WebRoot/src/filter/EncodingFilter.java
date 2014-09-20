package filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
   
public class EncodingFilter implements Filter {
	// 声明编码的成员变量
	private String encoding;

	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
	}
 
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		request.setCharacterEncoding(encoding);//对get没有效
		response.setContentType("text/html;charset=" + encoding);
		// 判断是否需要包装
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getMethod().equals("GET")) {
			request = new MyRequest(req);
		}
		// 声明包装类的实例
		// 放行
		chain.doFilter(request, response);
	}
	public void destroy() {
	}
}

// 声明包装类
class MyRequest extends HttpServletRequestWrapper {
	public MyRequest(HttpServletRequest request) {
		super(request);
	}

	// 增强getParamter
	@Override
	public String getParameter(String name) {
		String val = super.getParameter(name);
		try {   
			if("".equals(val) || "null".equals(val) || null == val){
				return "";
			}else {
				val = new String(val.getBytes("ISO-8859-1"),
						super.getCharacterEncoding());
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}
}
