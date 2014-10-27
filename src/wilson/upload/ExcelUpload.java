package wilson.upload;



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;

import java.text.SimpleDateFormat;
import java.util.*; 
import java.util.regex.*;
import java.io.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.inf.iis.bcs.BaiduBCS;

public class ExcelUpload extends HttpServlet { 
	/**
	 *  
	 */ 
	private static final long serialVersionUID = 1L; 
	private static String uploadPath;
	private static String realPath;
	
	private static String suningFilePath ;
	private static String salaryFilePath ;
	private static String salesFilePath ;

	
	private ServletContext sc;

	// BLOB
	private static final Log log = LogFactory.getLog(ExcelUpload.class);  
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		sc = config.getServletContext();
		suningFilePath = sc.getRealPath("/") + config.getInitParameter("suningFilePath");
		salaryFilePath = sc.getRealPath("/") + config.getInitParameter("salaryFilePath");
		salesFilePath = sc.getRealPath("/") + config.getInitParameter("salesFilePath");
	}
 
	private ServletConfig config = null;
        
	private File tempFile = null; // 用于存放临时文件的目录
	
 
	public void destroy() {      
		config = null;  
		super.destroy();  
	}   
	
	public String getUploadPath(){
		return this.realPath;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException { 
		String tempPath = req.getSession().getServletContext().getRealPath("/") + "data" + File.separator + "tempFile"; 
		System.out.println("tempFilePath=" + tempPath);
		
		tempFile = new File(tempPath); 
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		res.setContentType("text/html; charset=UTF-8");   

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096*10);
		factory.setRepository(tempFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1000000*100); 
		
		//文件名起名为时间+uuid形式
     	SimpleDateFormat fmt=new SimpleDateFormat("yyyyMMdd");
		String uuid = UUID.randomUUID().toString();
		String fileName = fmt.format(new Date()) + "-" + uuid + ".xls";
		
		
		String type = "";
		String directUrl = "";
		String filePath = "";
		
		
		
		try {
			List fileItems = upload.parseRequest(req);
			Iterator iter = fileItems.iterator();
			FileItem item;
			
			while(iter.hasNext()){
				item = (FileItem) iter.next();
				if(item.isFormField()){
					if(item.getFieldName().equals("uploadType")){
						type = item.getString("UTF-8");
						if(type.equals("1")){
							//苏宁excel
							directUrl = "/meidi/meidiserver/admin/suningExcelUpload.jsp?fileName=";
							filePath = suningFilePath;
						}else if(type.equals("2")){
							//提成标准
							directUrl = "/meidi/meidiserver/admin/salaryModelUpload.jsp?fileName=";
							filePath = salaryFilePath;
						}else if(type.equals("3")){
							//销售单上传
							directUrl = "/meidi/meidiserver/admin/salesUpload.jsp?fileName=";
							filePath = salesFilePath;
						}else{
							directUrl = "/meidi/meidiserver/admin/excelUpload.jsp?fileName=";
							res.sendRedirect(directUrl+fileName);
							return;
						}
					}
				}
			}
			
			iter = fileItems.iterator();

			while (iter.hasNext()) {
				item = (FileItem) iter.next();
				
				if (item.isFormField()) {
					System.out.println("表单参数名:" + item.getFieldName() + "，表单参数值:" + item.getString("UTF-8"));
				}
				// 忽略其他不是文件域的所有表单信息 
				if (!item.isFormField()) { 

					if (item.getName() != null && !item.getName().equals("")) {

						if (item.getName().endsWith(".xls")) { 
							System.out.println();
							System.out.println("上传文件的大小:" + item.getSize());
					     	System.out.println("上传文件的类型:" + item.getContentType());
					     	System.out.println("上传文件的名称:" + item.getName());
					     	
					     	File upperFolder = new File(filePath);
					     	if(!upperFolder.exists()){
					     		upperFolder.mkdirs();
					     	}
					     	File file = new File(filePath, fileName);
					     	item.write(file);
					     	System.out.println(("upload.message" + "上传文件成功！"));
							
						}else{
							System.out.println("wront type " + item.getName());
							System.out.println(("upload.message" + "文件类型错误，只能是xls类型！"));
						}
	
					 }else{
						 System.out.println(("upload.message" +  "没有选择上传文件！"));
					 }

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(("upload.message" + "文件IO错误！"));
		} catch (FileUploadException e) {
			e.printStackTrace();
			System.out.println(("upload.message" +"文件上传错误！"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(("upload.message" + "文件上传错误！"));
		}
		res.sendRedirect(directUrl+fileName);
		//req.getRequestDispatcher("/meidiserver/admin/updateExcel.jsp").forward(req, res);
	}

	public static String getSuningFilePath() {
		return suningFilePath;
	}

	public static String getSalaryFilePath() {
		return salaryFilePath;
	}

	public static String getSalesFilePath() {
		return salesFilePath;
	}

}
