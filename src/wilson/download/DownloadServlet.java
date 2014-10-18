package wilson.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -1772218636066067411L;
	private static String suningmubanPath = "";
	private static String tichengmubanPath = "";
	private static String xiaoshoudanmubanPath = "";
	
	private ServletConfig config = null;
	private ServletContext sc;

	

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		sc = config.getServletContext();
		suningmubanPath = config.getInitParameter("suningmubanPath");
		tichengmubanPath = config.getInitParameter("tichengmubanPath");
		xiaoshoudanmubanPath = config.getInitParameter("xiaoshoudanmubanPath");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
        // TODO Auto-generated method stub
		 
		String name = request.getParameter("name");
		
		String filename = "";
		String filePath = "";
		if(name != null && !name.equals("")){
			if(name.equals("suningmuban")){
				filename = "系统对比模板.xls";
				filePath = suningmubanPath;
			}else if(name.equals("tichengmuban")){
				filename = "提成标准模板.xls";
				filePath = tichengmubanPath;
			}else if (name.equals("xiaoshoudanmuban")){
				filename = "销售单模板.xls";
				filePath = xiaoshoudanmubanPath;
			}
		}else{
			return;
		}
		
		filename = URLEncoder.encode(filename,"UTF-8");
		response.setHeader("content-type", "application/xls");
		response.addHeader("content-disposition", "attachment;filename="+filename);
		InputStream is = sc.getResourceAsStream(filePath);
		   
	    int read = 0;
	    byte[] bytes = new byte[1024];
	   
	    OutputStream os = response.getOutputStream();
	    while((read = is.read(bytes)) != -1) {
	        os.write(bytes, 0, read);
	    }
	    os.flush();
	    os.close();
    }
	 
	 public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
  
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);

		// 响应消息
	}
	 
	 
}