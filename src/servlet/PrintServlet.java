package servlet;

import gift.Gift;
import gift.GiftManager;
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
public class PrintServlet extends HttpServlet {
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

		Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
		Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM();
		//System.out.println("%%%%%"+gMap);  
		//修改申请   
		//Map<Integer,OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user,0);
		// 退货申请
		//Map<Integer,OrderPrintln> opMap1 = OrderPrintlnManager.getOrderStatues(user,1);

		 
		 
		
		
		
		
	
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
				cell.setCellValue("单号");
				cell.setCellStyle(style);  
				cell = row.createCell((short) x++);
				cell.setCellValue("销售门店");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("销售员");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("pos(厂送)单号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("OMS订单号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("验证码(联保单)");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("顾客信息");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("票面名称");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("票面型号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("票面数量");
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
				cell.setCellValue("赠品");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("赠品数量");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("赠品状态");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("开票日期");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("安装日期");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("文员派单日期");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货地区");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货地址");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("上报状态");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货状态");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货人员");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货时间");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("安装人员");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("预约日期");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("派工公司");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货是否已结款");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("厂送票是否已回");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("厂送票是否已消");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("厂送票是否结款");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("是否已调账");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("是否已退货");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("备注");
				cell.setCellStyle(style);

				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，


				for (int i = 0; i < list.size(); i++){
					row = sheet.createRow((int) i + 1);
					Order order = list.get(i);

				     String str = "";
				     if(2 == order.getDeliveryStatues()) {
						
						 for(int j=0;j< listS.size();j++){
			          	   User u = listS.get(j);
			          	   if(u.getId() == order.getSendId()){
			          		   str = u.getUsername() ;
			          	   } 
						 }
					
			         }else {
                         str = "暂无送货员";
					  
			         }
					int y = 0 ;   
					// 第四步，创建单元格，并设置值
					row.createCell((short) y++).setCellValue(order.getPrintlnid() == null?"":order.getPrintlnid());
					row.createCell((short) y++).setCellValue(order.getbranchName(order.getBranch()));
					row.createCell((short) y++).setCellValue(usermap.get(order.getSaleID()).getUsername()+"    "+usermap.get(order.getSaleID()).getPhone());
					row.createCell((short) y++).setCellValue(order.getPos()); 
					row.createCell((short) y++).setCellValue(order.getSailId());
					row.createCell((short) y++).setCellValue(order.getCheck()); 
					row.createCell((short) y++).setCellValue(order.getUsername()+"    "+order.getPhone1()); 
	  
					
					row.createCell((short) y++).setCellValue(order.getCategory(1,"      "));
					row.createCell((short) y++).setCellValue(order.getSendType(1,"      "));
					row.createCell((short) y++).setCellValue(order.getSendCount(1,"      "));
					row.createCell((short) y++).setCellValue(order.getCategory(0,"      "));
					row.createCell((short) y++).setCellValue(order.getSendType(0,"      "));
					row.createCell((short) y++).setCellValue(order.getSendCount(0,"      ")); 
					row.createCell((short) y++).setCellValue(order.getGifttype("      "));
					row.createCell((short) y++).setCellValue(order.getGifcount("      "));
					row.createCell((short) y++).setCellValue(order.getGifStatues("      "));
					row.createCell((short) y++).setCellValue(order.getSaleTime());
					row.createCell((short) y++).setCellValue(order.getOdate()); 
					row.createCell((short) y++).setCellValue(order.getDealSendTime());
					row.createCell((short) y++).setCellValue(order.getLocate());  
					row.createCell((short) y++).setCellValue(order.getLocateDetail());
					row.createCell((short) y++).setCellValue(OrderManager.getOrderStatues(order)); 
					String songhuo = OrderManager.getDeliveryStatues(order);
					  
					row.createCell((short) y++).setCellValue(songhuo); 
					String senduser = "";
				    if(order.getSendId() != 0){
						  if(usermap.get(Integer.valueOf(order.getSendId())) != null){
					
							  senduser =  usermap.get(Integer.valueOf(order.getSendId())).getUsername() ;
					 
					  }}
				    row.createCell((short) y++).setCellValue(senduser);
				    row.createCell((short) y++).setCellValue(order.getSendtime());
					String installuser= "";
				    if(order.getInstallid() != 0){
						  if(usermap.get(Integer.valueOf(order.getInstallid())) != null){
					
							  installuser = usermap.get(Integer.valueOf(order.getInstallid())).getUsername() ;
					 
					  }}
				    
				    row.createCell((short) y++).setCellValue(installuser);
				    row.createCell((short) y++).setCellValue(order.getInstalltime()); 
				    
				    String dealuser = "" ;
				    if(order.getDealsendId() != 0){  
						 
				    	dealuser = usermap.get(Integer.valueOf(order.getDealsendId())).getUsername() ;
						 
						  }
				    
				    row.createCell((short) y++).setCellValue(dealuser);
					
				    row.createCell((short) y++).setCellValue(order.getStatues4()==0?"否":"是");
				    row.createCell((short) y++).setCellValue(order.getStatues1()==0?"否":"是");
				    row.createCell((short) y++).setCellValue(order.getStatues2()==0?"否":"是");
				    row.createCell((short) y++).setCellValue(order.getStatues3()==0?"否":"是");
				    row.createCell((short) y++).setCellValue(order.getStatuesDingma()==1?"是":"否");
				    row.createCell((short) y++).setCellValue(order.getDeliveryStatues()==3?"是":"否" );
				    row.createCell((short) y++).setCellValue(order.getRemark()); 

	 	    
				}
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
