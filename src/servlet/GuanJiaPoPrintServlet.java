package servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import net.sf.json.JSONObject;

import uploadtotal.UploadTotal;

import utill.DoubleUtill;
import utill.StringUtill;
import utill.TimeUtill;
import wilson.upload.UploadManager;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


 

/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */ 
public class GuanJiaPoPrintServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(GuanJiaPoPrintServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		 
		String method = request.getParameter("method");
		if("total".equals(method) || "totalcategory".equals(method) || "typetotal".equals(method) ){
			exporttotalExport(request,response);
		}
    }
	/**
	 * 处理微信服务器发来的消息
	 */ 
	 
	public void exporttotalExport(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("said");
		
        String method = request.getParameter("method");
        String type = request.getParameter("type");
        int statues = Integer.valueOf(type);
		String branch = request.getParameter("branch");
		Map<String,Map<String,List<UploadTotal>>> mapc = null ;
		
		
		 if("totalcategory".equals(method)){  
			mapc = UploadManager.getTotalOrdersCategoryGroup(id,statues,"");
		} 
		
		
		String message = "";
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("管家婆开单表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		int count = 0 ;
		int y = 0 ;
		HSSFRow row = sheet.createRow((int) count);
		count++;
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		
		HSSFCell cell = row.createCell((short) y++);
		cell.setCellValue("类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("录单日期");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("单据编号");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("单据类型");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("往来单位编号");
		cell.setCellStyle(style); 
		
		cell = row.createCell((short) y++);
		cell.setCellValue("往来单位全名");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("经手人编号");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("经手人全名");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("部门编号");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("部门全名");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) y++);
		cell.setCellValue("收/付款日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("仓库编号");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("仓库全名");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("摘要");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("附加说明");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) y++);
		cell.setCellValue("制单人编号");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("制单人");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("收/付款账户编号");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("收/付款金额");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("交货日期(订单专用)");
		cell.setCellStyle(style);  
        
		
		row = sheet.createRow((int) count);
		y = 0 ;   
		count++;
		cell = row.createCell((short) y++);
		cell.setCellValue("类型");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("商品编号");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("商品全名");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("仓库编号");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("仓库全名");
		cell.setCellStyle(style); 
		
		cell = row.createCell((short) y++);
		cell.setCellValue("单位");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("生产日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("批号");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("单价");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) y++);
		cell.setCellValue("金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("折扣");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("折后单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("折后金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) y++);
		cell.setCellValue("税率");
		cell.setCellStyle(style); 
		
		cell = row.createCell((short) y++);
		cell.setCellValue("税额");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("含税单价");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("含税金额");
		cell.setCellStyle(style);  
		cell = row.createCell((short) y++);
		cell.setCellValue("单据备注");
		cell.setCellStyle(style);  
		 
		
	//	Map<String,String> map = BranchTypeChange.getinstance().getMap();
		
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
				   while(itmaptype.hasNext()){
					   Map.Entry<String,List<UploadTotal>> enmaptype = itmaptype.next();
					   List<UploadTotal> listup = enmaptype.getValue();
					   String branchname = enmaptype.getKey();
					   
					   if(null != listup){
						   int flag1 = 0 ;
						   int flag2 = 0;
						   for(int i=0;i<listup.size();i++){
							    UploadTotal up = listup.get(i);
							    if(!(up.getCount() == 0 && up.getTotalcount() == 0 )){
							    	String typeOrder = "";
								    if(up.getCount() > 0){ 
								    	typeOrder = "销售单";
								    	flag1 ++ ;
								    

										if( flag1 == 1 ){
										row = sheet.createRow((int) count);
									    count++;
										y = 0 ;  
										// 第四步，创建单元格，并设置值
										row.createCell((short) y++).setCellValue("L" );
										row.createCell((short) y++).setCellValue(TimeUtill.getdateString());
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(typeOrder);
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue(branch); 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getRealbranchname());
										row.createCell((short) y++).setCellValue(id);
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										}
										row = sheet.createRow((int) count);
									    count++;
										y = 0 ;   
										// 第四步，创建单元格，并设置值
										row.createCell((short) y++).setCellValue("M");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getRealtype()); 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getCount());
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue("");
										 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue(""); 
										//row.createCell((short) y++).setCellValue("");
								    }
							    } 	
					     } 
					    
						   
						   for(int i=0;i<listup.size();i++){
							    UploadTotal up = listup.get(i);
							    if(!(up.getCount() == 0 && up.getTotalcount() == 0 )){
							    	String typeOrder = "";
								    if(up.getCount() <= 0){ 
								    	typeOrder = "退货单";
								    	flag2 ++;
								    

										if(flag2 == 1 ){
										row = sheet.createRow((int) count);
									    count++;
										y = 0 ;  
										// 第四步，创建单元格，并设置值
										row.createCell((short) y++).setCellValue("L" );
										row.createCell((short) y++).setCellValue(TimeUtill.getdateString());
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(typeOrder);
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue(branch); 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getRealbranchname());
										row.createCell((short) y++).setCellValue(id);
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										}
										row = sheet.createRow((int) count);
									    count++;
										y = 0 ;   
										// 第四步，创建单元格，并设置值
										row.createCell((short) y++).setCellValue("M");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getRealtype()); 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(up.getCount());
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue("");
										 
										row.createCell((short) y++).setCellValue("");
										row.createCell((short) y++).setCellValue(DoubleUtill.getdoubleTwo(up.getTotalcount()/up.getCount()));
										row.createCell((short) y++).setCellValue(up.getTotalcount());
										row.createCell((short) y++).setCellValue("");  
										//row.createCell((short) y++).setCellValue(""); 
								    }
							    } 	
					     }    
				   }	 
			   }
		     } 
		  }  
		
		//System.out.println(count);
		// 第六步，将文件存到指定位置
		try{    
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""+ TimeUtill.getdateString() + ".xls\"");
			//FileOutputStream fout = new FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close(); 
	
		}
		catch (Exception e){
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
