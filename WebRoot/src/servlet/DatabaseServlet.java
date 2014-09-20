package servlet;

import gift.Gift;
import gift.GiftManager;
import group.Group;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.Order;
import order.OrderManager;
import order.OrderStatues;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;

import user.User;
import user.UserManager;
import utill.StringUtill;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import category.Category;
import category.CategoryManager;


/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */  
public class DatabaseServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(DatabaseServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        // Date date1 = new Date();
		// String printlntime = df2.format(date1);
		// User user  = (User)request.getSession().getAttribute("user");
         
		 String file = request.getParameter("databasefile");
		
		 
		 PrintWriter out = response.getWriter();
		 try {  
	            Runtime rt = Runtime.getRuntime();  
	            
	            String cmd ="D:\\soft\\mysql-5.6.19-winx64\\bin\\mysqldump -h 114.113.99.7 -uccwic -p1234abcd ccwic mdbranch mdbranchtype mdcategory mdgroup mdlocate mdorder mdordergift mdorderproduct mdorderupdateprint mdproduct mduser  > "+ file+"mysql.sql"; //一定要加 -h localhost(或是服务器IP地址)  
                logger.info(cmd);      
	            Process process =rt.exec("cmd /c " + cmd);        
	            InputStreamReader isr = new InputStreamReader(process.getErrorStream(),"utf-8");    
	            LineNumberReader input = new LineNumberReader(isr);  
	            String line;
	              
	            while((line = input.readLine())!= null){  
                   logger.info(line+"~~~~~~~~~~");  
                  // out.write(line);            
	               }     
              logger.info("备份成功!");
              out.write("备份成功!");
	        } catch (IOException e) {  
                  logger.info("备份失败!");
                  out.write("备份失败!");
	            e.printStackTrace();  
	        }finally{  
	        	out.close();
	        	out = null;
	        	
	        }
    } 
	/**
	 * 处理微信服务器发来的消息
	 */
	 
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
