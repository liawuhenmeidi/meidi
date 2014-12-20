package servlet;

import gift.Gift;
import gift.GiftManager;
import gift.GiftService;
import group.Group;

import java.io.IOException;
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
import orderproduct.OrderProductService;

import saledealsend.SaladealsendMessageManager;
import saledealsend.Saledealsend;
import saledealsend.SaledealsendManager;
import saledealsend.SaledealsendMessage;
import user.User;
import user.UserManager;
import utill.HttpRequestUtill;
import utill.StringUtill;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.write.WritableSheet;

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
public class ChargeExportServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(ChargeExportServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		
		String id = request.getParameter("said");
 
		 User user = (User)request.getSession().getAttribute("user");
		 
		 Saledealsend sa = SaledealsendManager.getSaledealsend(id);

		 List<SaledealsendMessage> list = SaladealsendMessageManager.getlistByid(sa);
		   
		 Map<String ,Order> map = OrderManager.getOrdermapByIds(user,sa.getOrderids());
		       // 第一步，创建一个webbook，对应一个Excel文件
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
				HSSFSheet sheet = wb.createSheet("安装网点结款");
				// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
				HSSFRow row = sheet.createRow((int) 0);
				// 第四步，创建单元格，并设置值表头 设置表头居中
				HSSFCellStyle style = wb.createCellStyle();
				
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
				int x = 0 ; 
				
				HSSFCell cell = row.createCell((short) x++);
				cell.setCellValue("序号");
				cell.setCellStyle(style);  
				cell = row.createCell((short) x++);
				cell.setCellValue("单号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("门店");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("安装网点");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("顾客信息");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货名称");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货型号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货数量");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("送货地区");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("送货地址");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货状态");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				
				cell.setCellValue("备注");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("结款金额");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("网点建议");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);

				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

				List<OrderProduct> listopid = new ArrayList<OrderProduct>();
				int total = 0 ;
				int xx = 0 ;
				int counts = 0 ;
				
				for(int i = 0 ; i < list.size() ; i ++){
					SaledealsendMessage sas = list.get(i);
					int price1 = sas.getDealsendprice();
					String price2 = sas.getDealsendMessage();
					String orderid = sas.getOrderids();
					
					 total += price1;
					
					 String[] ordersid = orderid.split("_");
					 int count = ordersid.length ;
					 
					 for(int j=0;j<ordersid.length;j++){
						 Order o = map.get(ordersid[j]);
						 xx++;
						 row = sheet.createRow((int) counts + 1);
							counts++;
							int y = 0 ;
								
								row.createCell((short) y++).setCellValue(xx);   
								row.createCell((short) y++).setCellValue(o.getPrintlnid() == null?"":o.getPrintlnid());
								row.createCell((short) y++).setCellValue(o.getbranchName(o.getBranch()));   
								row.createCell((short) y++).setCellValue(o.getdealsendName());
								
								row.createCell((short) y++).setCellValue(o.getUsername()+o.getPhone1()); 
								row.createCell((short) y++).setCellValue(o.getCategory(0, " ")); 
								row.createCell((short) y++).setCellValue(o.getSendType(0, "  "));  
								row.createCell((short) y++).setCellValue( o.getSendCount(0, "  ")); 
								row.createCell((short) y++).setCellValue(o.getLocate());
								row.createCell((short) y++).setCellValue(o.getLocateDetail() );
								row.createCell((short) y++).setCellValue(OrderManager.getDeliveryStatues(o));
								row.createCell((short) y++).setCellValue(o.getRemark() ); 
						  
									if(j == 0 && count > 1 || count <= 1 ){
								   if(count >1){ 
									 if(count == 1){
										 row.createCell((short) y++).setCellValue(price1 ); 
										   row.createCell((short) y++).setCellValue(price2 ); 
									 }
									   row.createCell((short) y++).setCellValue(o.getRemark() ); 
								   }else {
									   row.createCell((short) y++).setCellValue(price1 ); 
									   row.createCell((short) y++).setCellValue(price2 ); 
								   }		 

								}
					    }
		               } 

				//System.out.println(count);
				// 第六步，将文件存到指定位置
				try     
				{    
					response.setContentType("APPLICATION/OCTET-STREAM");
					response.setHeader("Content-Disposition", "attachment; filename=\""+ sa.getName() + ".xls\"");
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
