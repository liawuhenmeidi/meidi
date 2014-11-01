package servlet;

import gift.Gift;
import gift.GiftManager;
import gift.GiftService;
import group.Group;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.Order;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import orderproduct.OrderProductService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import user.User;
import user.UserManager;
import utill.StringUtill;
import category.Category;
import category.CategoryManager;


/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */  
public class ModelServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(PrintServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		 SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
         Date date1 = new Date();
		String printlntime = df2.format(date1);
		User user  = (User)request.getSession().getAttribute("user");
		
		request.setCharacterEncoding("utf-8");
		
		 
		String pageNum = request.getParameter("page");
		String numb = request.getParameter("numb");
		String search = request.getParameter("search");

		if(StringUtill.isNull(pageNum)){
			pageNum = "1"; 
		}
		if(StringUtill.isNull(numb)){
			numb = "10";
		}

	
		int Page = Integer.valueOf(pageNum);

		System.out.println("Page"+Page);

		int num = Integer.valueOf(numb);
		if(Page <=0){
			Page =1 ;
		}

		List<Order> list = null;

		list = (List)request.getSession().getAttribute("exportList"); 
		       
		HashMap<Integer,User> usermap = UserManager.getMap();
		    
		//获取二次配单元（工队）

		List<User> listS = UserManager.getUsers(user,Group.sencondDealsend);   

		HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

		Map<Integer,List<OrderProduct>> OrPMap = OrderProductService.getStaticOrderStatuesM();
		Map<Integer,List<Gift>> gMap = GiftService.getmap();
		System.out.println("%%%%%"+gMap);  
		//修改申请  
		Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,0);
		// 退货申请
		Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);

		 
		
		
		
		
		
	
		       // 第一步，创建一个webbook，对应一个Excel文件
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
				HSSFSheet sheet = wb.createSheet("报装单");
				// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
				HSSFRow row = sheet.createRow((int) 0);
				// 第四步，创建单元格，并设置值表头 设置表头居中
				HSSFCellStyle style = wb.createCellStyle();
				
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
				int x = 0 ; 
				HSSFCell cell = row.createCell((short) x++);
				
				cell.setCellValue("门店名称");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) x++);
				cell.setCellValue("POS订单号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("销售日期");
				cell.setCellStyle(style);
				
				
				cell = row.createCell((short) x++);
				cell.setCellValue("交货日期");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("票面型号");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) x++);
				cell.setCellValue("票面数量");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("供价");
				cell.setCellStyle(style);				
				cell = row.createCell((short) x++);
				cell.setCellValue("扣点");
				cell.setCellStyle(style);
				

				// 第六步，将文件存到指定位置
				try    
				{    
					response.setContentType("APPLICATION/OCTET-STREAM");
					response.setHeader("Content-Disposition", "attachment; filename=\""+ printlntime + ".xls\"");
					//FileOutputStream fout = new FileOutputStream("E:/报装单"+printlntime+".xls");
					wb.write(response.getOutputStream());
					response.getOutputStream().close();
			
				}
				catch (Exception e)
				{
					e.printStackTrace();
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
