package servlet;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.OrderManager;

import org.apache.commons.fileupload.*;
import java.util.*; 
import java.util.regex.*;
import java.io.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.inf.iis.bcs.BaiduBCS;

import upload.Sample;

public class FileUpload extends HttpServlet { 
	/**
	 *  
	 */ 
	private static final long serialVersionUID = 1L; 

	// BLOB
	private static final Log log = LogFactory.getLog(FileUpload.class);  
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
 
	private ServletConfig config = null;
        
	private File tempPath = null; // 用于存放临时文件的目录
 
	public void destroy() {      
		config = null;  
		super.destroy();  
	}   

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String printlnid = "";     
		//String imageName = "2012.jpg";    
		 //String path1 = req.getSession().getServletContext().getRealPath(req.getRequestURI()) ;
		// log.info("path"+path1);      
		// String path =  path1.substring(0,path1.lastIndexOf("/")+1);
		//String path = "D:/";     
		 String path = "/home/voip022jvboxi9p40r2c2/logs/";
		 //log.info("path"+path1);                
		 //String path = req.getContextPath()+"/";  
		 //log.info("path"+path);                      
		 tempPath = new File(path);        
         log.info("****temp"+tempPath.getAbsolutePath()  
);                   
		//String uploadPath = config.getInitParameter("uploadPath"); // 用于存放上传文件的目录
        // String uploadPath = path1.substring(0,path1.lastIndexOf("/")+1)+"Image"; // 用于存放上传文件的目录
         String uploadPath = path;   
         log.info("****uploadPath"+uploadPath);   
		res.setContentType("text/html; charset=UTF-8");   
		PrintWriter out = res.getWriter();
		System.out.println(req.getContentLength());
		System.out.println(req.getContentType());
		System.out.println(uploadPath);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(4096*10);
		// the location for saving data that is larger than getSizeThreshold()
		factory.setRepository(tempPath);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum size before a FileUploadException will be thrown
		upload.setSizeMax(1000000*100); 
		try {
			List fileItems = upload.parseRequest(req);
			// assume we know there are two files. The first file is a small
			// text file, the second is unknown and is written to a file on
			// the server
			Iterator iter = fileItems.iterator();
			// 正则匹配，过滤路径取文件名
			String regExp = ".+\\\\(.+)$";

			// 过滤掉的文件类型
			String[] errorType = { ".exe", ".com", ".cgi", ".jsp" };
			Pattern p = Pattern.compile(regExp);
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
	log.info(item.getFieldName()); 
				if (item.isFormField()) {
					log.info("111"+item.getFieldName()); 
					if (item.getFieldName().equals("printlnid")) {
						//id = Integer.parseInt(item.getString());
						printlnid = item.getString(); 
					} 
				}
				// 忽略其他不是文件域的所有表单信息 
				if (!item.isFormField()) { 
					String name = item.getName();
					//log.info("222"+name); 
					long size = item.getSize();

					if ((name == null || name.equals("")) && size == 0)
						continue;
					Matcher m = p.matcher(name);
					boolean result = m.find();
					if (result) { 
						for (int temp = 0; temp < errorType.length; temp++) {
							if (m.group(1).endsWith(errorType[temp])) {
								throw new IOException(name + ": wrong type");
							}
						} 
					}

					try {  
						item.write(new File(uploadPath + printlnid+".jpg"));  
						out.print(printlnid + "&nbsp;&nbsp;" + size + "<br>");
						
						String filepath = path + printlnid+".jpg";     
                        File file = new File(filepath);   
                         log.info(file.getAbsolutePath());         
						BaiduBCS baiduBCS = Sample.init();  
						 
						Sample.putObjectByFile(baiduBCS, file,printlnid+".jpg"); 
					
						OrderManager.updateimagerUrl(printlnid, "http://bcs.duapp.com/liaowuhen/image%2F"+printlnid+".jpg"); 
						
						file.delete();       
						
					} catch (Exception e) {
						out.println(e);
						log.info(e);
					}

				}
			}
		} catch (IOException e) {
			out.println(e);
			log.info(e);
		} catch (FileUploadException e) {
			out.println(e);
			log.info(e);
		}

	}

}
