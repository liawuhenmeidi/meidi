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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import order.Order;
import order.OrderManager;
import order.OrderStatues;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import orderproduct.OrderProductService;

import uploadtotal.UploadTotal;
import uploadtotalgroup.UploadTotalGroup;
import uploadtotalgroup.UploadTotalGroupManager;
import user.User;
import user.UserManager;
import utill.BasicUtill;
import utill.DoubleUtill;
import utill.HttpRequestUtill;
import utill.StringUtill;
import wilson.upload.UploadManager;
import wilson.upload.UploadSalaryModel;

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
import org.apache.poi.hssf.util.HSSFColor;

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
		 
		String method = request.getParameter("method");
		if("exportall".equals(method)){
			exportOrders( request,response);
		}else if("total".equals(method) || "totalcategory".equals(method) || "typetotal".equals(method) ){
			exporttotalExport(request,response);
		}
    }
	/**
	 * 处理微信服务器发来的消息
	 */ 
	 
	public void exporttotalExport(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("said");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Date date1 = new Date(); 
		String printlntime = df2.format(date1); 
        String method = request.getParameter("method");
        String type = request.getParameter("type");
        int statues = Integer.valueOf(type);
		Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
		
		Map<String,Map<String,List<UploadTotal>>> mapt = null ;
		Map<String,Map<String,List<UploadTotal>>> mapc = null ;
		HashMap<String, List<UploadTotal>> maptypeinit = null;
		
		if("total".equals(method)){
			mapt = UploadManager.getTotalOrdersGroup(id,statues,"");
		}else if("typetotal".equals(method)){   
			maptypeinit = UploadManager.getTotalOrdersGroup(id,"type",statues,"");
		}else if("totalcategory".equals(method)){  
			mapc = UploadManager.getTotalOrdersCategoryGroup(id,statues,"");
		} 
		
		
		String message = "";
		 
		UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
		if(upt != null){
		   message = upt.getCategoryname();
		}
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("销售统计表");
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
		cell.setCellValue("门店");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("品类");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("型号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("销售金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("扣点后单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("扣点后总价");
		cell.setCellStyle(style);
		
		
		int count = 0 ;
		int idcount = 0 ;
		double AllTotalcount = 0 ;
		int AllCount = 0 ;
		double AllTatalbreakcount = 0 ;
		   
		if(null != mapt){  
		Set<Map.Entry<String,Map<String,List<UploadTotal>>>> setmap = mapt.entrySet();
		Iterator<Map.Entry<String,Map<String,List<UploadTotal>>>> itmap = setmap.iterator();
	    while(itmap.hasNext()){
		   Map.Entry<String,Map<String,List<UploadTotal>>> enmap = itmap.next();
		   Map<String,List<UploadTotal>> maptype = enmap.getValue();
		   Set<Map.Entry<String,List<UploadTotal>>> setmaptype =  maptype.entrySet();
		   Iterator<Map.Entry<String,List<UploadTotal>>> itmaptype = setmaptype.iterator();
		   double Totalcount = 0 ;
		   int Count = 0 ;
		   double Tatalbreakcount = 0 ;
		   String branchname = "";
		   while(itmaptype.hasNext()){ 
			   Map.Entry<String,List<UploadTotal>> enmaptype = itmaptype.next();
			   String key = enmaptype.getKey();
			   if(!StringUtill.isNull(message)){
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					while(it.hasNext()){ 
						String t = it.next();
						if(key.equals(t)){ 
							key = jsObj.getString(key);
						}
					}
				}
			   List<UploadTotal> listup = enmaptype.getValue();
			   double initTotalcount = 0 ;
			   int initCount = 0 ;
			   double initTatalbreakcount = 0 ;
			   
			   if(null != listup){
				   for(int i=0;i<listup.size();i++){
					   UploadTotal up = listup.get(i);
					   branchname = up.getBranchname();
					   Totalcount += up.getTotalcount();
					   Count += up.getCount();
					   Tatalbreakcount += up.getTatalbreakcount();
					   AllTotalcount += up.getTotalcount();
					   AllCount += up.getCount();
					   AllTatalbreakcount += up.getTatalbreakcount();
					   initTotalcount += up.getTotalcount();
					   initCount += up.getCount();
					   initTatalbreakcount += up.getTatalbreakcount();
					   idcount ++;
					   
					   String tpe = "";
						if(null != mapus){
							UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
							if(null != ups){
								tpe = ups.getCatergory(); 
							}
						}
							
							
					    row = sheet.createRow((int) count + 1);
					    count++;
						int y = 0 ;   
						idcount ++;
						// 第四步，创建单元格，并设置值
						row.createCell((short) y++).setCellValue(idcount );
						row.createCell((short) y++).setCellValue(up.getBranchname());
						row.createCell((short) y++).setCellValue(tpe);
						row.createCell((short) y++).setCellValue(up.getType());
						row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount())); 
						row.createCell((short) y++).setCellValue(up.getCount());
						row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount())); 
						row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount())); 
						row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTatalbreakcount())); 
			         }
				 }
			  
			    row = sheet.createRow((int) count + 1);
			    count++; 
				int y = 0 ;  
				
				HSSFCellStyle cellStyle = wb.createCellStyle();    //创建一个样式
				cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);    //设置颜色为红色
		        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				// 第四步，创建单元格，并设置值
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(branchname);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(key);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(initCount);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(initTotalcount));
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(initTatalbreakcount)); 
				cell.setCellStyle(cellStyle);
				
   
		     } 
		   row = sheet.createRow((int) count + 1);
		   count++;
		   int y = 0 ; 
		   HSSFCellStyle cellStyle = wb.createCellStyle();    //创建一个样式
			cellStyle.setFillForegroundColor(HSSFColor.RED.index);    //设置颜色为红色
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			// 第四步，创建单元格，并设置值
			cell = row.createCell((short) y++);
			cell.setCellValue("");
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue(branchname);
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue("总计");
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue("");
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue("");
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue(Count);
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue(DoubleUtill.getdoubleTwo(Totalcount));
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue("");
			cell.setCellStyle(cellStyle);
			cell = row.createCell((short) y++);
			cell.setCellValue(DoubleUtill.getdoubleTwo(Tatalbreakcount)); 
			cell.setCellStyle(cellStyle);
			  
		   }
		}
		 
		
		if(null != mapc){ 
			   Set<Map.Entry<String,Map<String,List<UploadTotal>>>> setmap = mapc.entrySet();
			   Iterator<Map.Entry<String,Map<String,List<UploadTotal>>>> itmap = setmap.iterator();
			   while(itmap.hasNext()){
				   Map.Entry<String,Map<String,List<UploadTotal>>> enmap = itmap.next();
				   String key = enmap.getKey();
				   
				   if(!StringUtill.isNull(message)){
						JSONObject jsObj = JSONObject.fromObject(message);
						Iterator<String> it = jsObj.keys();
						while(it.hasNext()){ 
							String t = it.next();
							if(key.equals(t)){ 
								key = jsObj.getString(key);
							}
						}
					}
				   
				   Map<String,List<UploadTotal>> maptype = enmap.getValue();
				   Set<Map.Entry<String,List<UploadTotal>>> setmaptype =  maptype.entrySet();
				   Iterator<Map.Entry<String,List<UploadTotal>>> itmaptype = setmaptype.iterator();
				   double Totalcount = 0 ;
				   int Count = 0 ;
				   double Tatalbreakcount = 0 ;
				   while(itmaptype.hasNext()){
					   Map.Entry<String,List<UploadTotal>> enmaptype = itmaptype.next();
					   List<UploadTotal> listup = enmaptype.getValue();
					   String branchname = enmaptype.getKey();
					   double initTotalcount = 0 ;
					   int initCount = 0 ;
					   double initTatalbreakcount = 0 ;
					   if(null != listup){
						   for(int i=0;i<listup.size();i++){
							   UploadTotal up = listup.get(i);
							   Totalcount += up.getTotalcount();
							   Count += up.getCount();
							   Tatalbreakcount += up.getTatalbreakcount();
							   AllTotalcount += up.getTotalcount();
							   AllCount += up.getCount();
							   AllTatalbreakcount += up.getTatalbreakcount();
							   initTotalcount += up.getTotalcount();
							   initCount += up.getCount();
							   initTatalbreakcount += up.getTatalbreakcount();
							   idcount ++;
							   
							   String tpe = "";
								if(null != mapus){
									UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
									if(null != ups){
										tpe = ups.getCatergory(); 
									}
								}
				  
								row = sheet.createRow((int) count + 1);
							    count++;
								int y = 0 ;   
								idcount ++;
								// 第四步，创建单元格，并设置值
								row.createCell((short) y++).setCellValue(idcount );
								row.createCell((short) y++).setCellValue(up.getBranchname());
								row.createCell((short) y++).setCellValue(tpe);
								row.createCell((short) y++).setCellValue(up.getType());
								row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount())); 
								row.createCell((short) y++).setCellValue(up.getCount());
								row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount())); 
								row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount())); 
								row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTatalbreakcount())); 

					   } 
					   
				   }
		      
					   row = sheet.createRow((int) count + 1);
					    count++;
						int y = 0 ;   
						// 第四步，创建单元格，并设置值
						HSSFCellStyle cellStyle = wb.createCellStyle();    //创建一个样式
						cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);    //设置颜色为红色
				        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						// 第四步，创建单元格，并设置值
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue(branchname);
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue(key);
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue(initCount);
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue(DoubleUtill.getdoubleTwo(initTotalcount));
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						cell = row.createCell((short) y++);
						cell.setCellValue(DoubleUtill.getdoubleTwo(initTatalbreakcount)); 
						cell.setCellStyle(cellStyle);
						
			   }

				   row = sheet.createRow((int) count + 1);
				   count++;
				   int y = 0 ; 
				   HSSFCellStyle cellStyle = wb.createCellStyle();    //创建一个样式
					cellStyle.setFillForegroundColor(HSSFColor.RED.index);    //设置颜色为红色
			        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					// 第四步，创建单元格，并设置值
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("总计");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(Count);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill.getdoubleTwo(Totalcount));
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill.getdoubleTwo(Tatalbreakcount)); 
					cell.setCellStyle(cellStyle);
				  
		      } 
		  } 
		
		
		 if(null != maptypeinit){
			   Set<Map.Entry<String, List<UploadTotal>>> setmaptype =  maptypeinit.entrySet();
			   Iterator<Map.Entry<String, List<UploadTotal>>> itmaptype = setmaptype.iterator();
			   double Totalcount = 0 ;
			   int Count = 0 ;
			   double Tatalbreakcount = 0 ;
			   String branchname = "";
			   while(itmaptype.hasNext()){
				   Map.Entry<String, List<UploadTotal>> enmaptype = itmaptype.next();
				   String key = enmaptype.getKey();
				   if(!StringUtill.isNull(message)){
						JSONObject jsObj = JSONObject.fromObject(message);
						Iterator<String> it = jsObj.keys();
						while(it.hasNext()){ 
							String t = it.next();
							if(key.equals(t)){ 
								key = jsObj.getString(key);
							}
						}
					}
				   
				   List<UploadTotal> uplist = enmaptype.getValue();
				   double initTotalcount = 0 ;
				   int initCount = 0 ;
				   double initTatalbreakcount = 0 ;
				   
				   if(null != uplist){
					   Iterator<UploadTotal> it = uplist.iterator();
					   while(it.hasNext()){
						   UploadTotal up = it.next();
						   Totalcount += up.getTotalcount();
						   Count += up.getCount();
						   Tatalbreakcount += up.getTatalbreakcount();
						   AllTotalcount += up.getTotalcount();
						   AllCount += up.getCount();
						   AllTatalbreakcount += up.getTatalbreakcount();
						   initTotalcount += up.getTotalcount();
						   initCount += up.getCount();
						   initTatalbreakcount += up.getTatalbreakcount();
						   idcount ++; 
						   
						   String tpe = "";
							if(null != mapus){
								UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
								if(null != ups){
									tpe = ups.getCatergory(); 
								}
							}
                             
							
							   row = sheet.createRow((int) count + 1);
							   count++;
							   int y = 0 ; 
							    row.createCell((short) y++).setCellValue(idcount);
								row.createCell((short) y++).setCellValue("");
								row.createCell((short) y++).setCellValue(tpe);
								row.createCell((short) y++).setCellValue(up.getType());
								row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
								row.createCell((short) y++).setCellValue(up.getCount());
								row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()));
								row.createCell((short) y++).setCellValue(0==up.getCount()?"":DoubleUtill.getdoubleTwo(up.getTatalbreakcount()/up.getCount()));
								row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTatalbreakcount())); 
						   } 
					   } 
			   } 
		} 
	   row = sheet.createRow((int) count + 1);
	   count++;
		int y = 0 ;   
		// 第四步，创建单元格，并设置值
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("总计");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue(AllCount);
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(AllTotalcount)); 
		row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(AllTatalbreakcount)); 
		//System.out.println(count);
		// 第六步，将文件存到指定位置
		try{    
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""+ printlntime + ".xls\"");
			//FileOutputStream fout = new FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();
	
		}
		catch (Exception e){
			e.printStackTrace();
		}

		
	}

	public void exportOrders(HttpServletRequest request, HttpServletResponse response){
		User user = (User)request.getSession().getAttribute("user");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
        Date date1 = new Date();
		String printlntime = df2.format(date1); 
		String type = request.getParameter("type");
   	    String statues = request.getParameter("statues");
      	String num = request.getParameter("num");
   	    String page = request.getParameter("page");
	   	String sort = request.getParameter("sort");
	   	String search = request.getParameter("searched");
	   	String sear = "";
	   	if(!StringUtill.isNull(search)){ 
	   		sear = HttpRequestUtill.getSearch(request);
	   	} 
   	 
	   	List<Order> list = OrderManager.getOrderlist(user,Integer.valueOf(type),Integer.valueOf(statues),Integer.valueOf(num),Integer.valueOf(page),sort,sear);
		       
		
		
		
		HashMap<Integer,User> usermap = UserManager.getMap();

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
				cell.setCellValue("销售员姓名");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("销售员电话");
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
				cell.setCellValue("顾客姓名");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("顾客电话");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("票面名称");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("票面型号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				if(UserManager.checkPermissions(user, Group.dealSend)){
					cell.setCellValue("零售价");
					cell.setCellStyle(style);
					cell = row.createCell((short) x++);
					
				}
				cell.setCellValue("票面数量");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货名称");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				cell.setCellValue("送货型号");
				cell.setCellStyle(style);
				cell = row.createCell((short) x++);
				if(UserManager.checkPermissions(user, Group.dealSend)){
					cell.setCellValue("零售价");
					cell.setCellStyle(style);
					cell = row.createCell((short) x++);
				}  
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
				cell.setCellValue("预约日期");
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
				cell.setCellValue("安装时间");
				cell.setCellStyle(style); 
				cell = row.createCell((short) x++);
				cell.setCellValue("安装网点");
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
				cell.setCellValue("备注");
				cell.setCellStyle(style);
				

				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

                 int count = 0 ;
				for (int i = 0; i < list.size(); i++){
					
					Order order = list.get(i);
                   
					List<OrderProduct> listop = order.getOrderproduct();
					
					for(int m=0;m<listop.size();m++){
						OrderProduct op = listop.get(m);
						
						if(op.getStatues() == 0){
							row = sheet.createRow((int) count + 1);
							count++;
							int y = 0 ;   
							// 第四步，创建单元格，并设置值
							row.createCell((short) y++).setCellValue(order.getPrintlnid() == null?"":order.getPrintlnid());
							row.createCell((short) y++).setCellValue(order.getbranchName(order.getBranch()));
							row.createCell((short) y++).setCellValue(usermap.get(order.getSaleID()).getUsername());
							row.createCell((short) y++).setCellValue(usermap.get(order.getSaleID()).getPhone());
							row.createCell((short) y++).setCellValue(order.getPos()); 
							row.createCell((short) y++).setCellValue(order.getSailId());
							row.createCell((short) y++).setCellValue(order.getCheck()); 
							row.createCell((short) y++).setCellValue(order.getUsername()); 
			  
							row.createCell((short) y++).setCellValue(order.getPhone1()); 
							row.createCell((short) y++).setCellValue(order.getCategory(1,"      "));
							row.createCell((short) y++).setCellValue(order.getSendType(1,"      "));
							if(UserManager.checkPermissions(user, Group.dealSend)){
								row.createCell((short) y++).setCellValue(order.getSendprice(1,""));
							}  
							 
							row.createCell((short) y++).setCellValue(order.getSendCount(1,"      "));
							row.createCell((short) y++).setCellValue(op.getCategoryName());
							row.createCell((short) y++).setCellValue(op.getTypeName());
							if(UserManager.checkPermissions(user, Group.dealSend)){
								row.createCell((short) y++).setCellValue(op.getPrice());
							}
							
							row.createCell((short) y++).setCellValue(op.getCount()); 
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
	
						    row.createCell((short) y++).setCellValue(order.getsendName());
						    row.createCell((short) y++).setCellValue(order.getSendtime());
	
						    row.createCell((short) y++).setCellValue(order.getinstallName());
						    row.createCell((short) y++).setCellValue(order.getInstalltime()); 
						     
						    row.createCell((short) y++).setCellValue(order.getdealsendName());
							
						    row.createCell((short) y++).setCellValue(order.getStatues4()==0?"否":"是");
						    row.createCell((short) y++).setCellValue(order.getStatues1()==0?"否":"是");
						    row.createCell((short) y++).setCellValue(order.getStatues2()==0?"否":"是"); 
						    row.createCell((short) y++).setCellValue((StringUtill.isNull(order.getStatuesCharge())?"否":order.getStatuesCharge()));
						    row.createCell((short) y++).setCellValue(order.getRemark()); 
					 }
					}

				}
				//System.out.println(count);
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
