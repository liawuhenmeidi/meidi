package filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

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
		Enumeration<String> params = req.getParameterNames();  
		
        String sql = "";  
        while (params.hasMoreElements()) {  
            //得到参数名  
            String name = params.nextElement().toString();  
            //System.out.println("name===========================" + name + "--");  
            //得到参数对应值  
            String[] value = req.getParameterValues(name);  
            for (int i = 0; i < value.length; i++) {  
                sql = sql + value[i];  
            }  
        }  
        //System.out.println("============================SQL"+sql);  
        //有sql关键字，跳转到error.html  
        if (sqlValidate(sql)) {  
            throw new IOException("您发送请求中的参数中含有非法字符");  
            //String ip = req.getRemoteAddr();  
        } else {  
        	chain.doFilter(request, response);  
        }  
		
	}
	
	
	public void destroy() {
	}
	
	public static void main(String args[]){
		String badStr = "exec|execute|insert|delete|update|drop|chr|mid|master|truncate|" +  
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|create|drop|" +  
                "table|grant|use|group_concat|column_name|" +   
                "information_schema.columns|table_schema|union|*|" +  
                "--|//|/|#";//过滤掉的sql关键字，可以手动添加  
        String[] badStrs = badStr.split("\\|");  
        for (int i = 0; i < badStrs.length; i++) {  
            System.out.println(badStrs[i]);
        }  
	}
	 
	protected static boolean sqlValidate(String str) {  
        str = str.toLowerCase();//统一转为小写  
        System.out.println(str); 
        String badStr = "exec|execute|insert|delete|drop|chr|mid|master|truncate|" +  
                "declare|sitename|net user|xp_cmdshell|;|create|drop|" +  
                "table|grant|group_concat|column_name|" +   
                "information_schema.columns|table_schema|union|*|" +  
                "--|//|#";//过滤掉的sql关键字，可以手动添加  
         
        String[] badStrs = badStr.split("\\|");  
        for (int i = 0; i < badStrs.length; i++) {  
            if (str.indexOf(badStrs[i]) >= 0) { 
            	System.out.println(badStrs[i]);
                return true;  
            }  
        }  
        return false;  
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
