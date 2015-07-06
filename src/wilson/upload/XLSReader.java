package wilson.upload;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.Product;

import change.BranchTypeChange;
import change.UploadChangeAll;

import utill.StringUtill;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class XLSReader {
	
	 Properties props=System.getProperties();
     String osName = props.getProperty("os.name");
     protected static Log logger = LogFactory.getLog(XLSReader.class);
	
	//读取指定位置的xls内容，并解析成UploadOrder对象进行返回
	public List<UploadOrder> readSuningXLS(String path,String fileName){
		//D:/software/apache-tomcat-7.0.21/webapps/meidi/data/xls/2014092412.xls
		if(fileName == null || path == null){
			return null;
		}
		
		final int SHOP_POS = 0;
		final int POSNO_POS = 1;
		final int SALETIME_POS = 2;
		final int TYPE_POS = 3;
		final int NUM_POS = 4;
		final int SALEPRICE_POS = 5;
		final int BACKPOINT_POS = 6;

		
		String filepath = path.replace("\\", "/");
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrder uo = new UploadOrder();
		int i = 0;
		
		Workbook wb = null;
		try{
			File srcFile = new File(filepath,fileName); 
			wb = Workbook.getWorkbook(srcFile);
			Sheet sheet0 = wb.getSheet(0);
			
			String name = sheet0.getCell(1,0).getContents();
			
			//如果文件名一样
			if(UploadManager.isUploaderFileNameExist(name)){
				UploadOrders = new ArrayList<UploadOrder>();
				uo = new UploadOrder();
				uo.setId(-1);
				uo.setName("文件名称重复!请修改名称");
				UploadOrders.add(uo);
				return UploadOrders;
			}
			
			for(i = 2 ; i < sheet0.getRows(); i ++){
				if(sheet0.getCell(0,i).getContents().equals("")){
					continue;
				}
				uo.setName(name);
				uo.setShop(sheet0.getCell(SHOP_POS,i).getContents().trim());
				
				String tempPos = sheet0.getCell(POSNO_POS,i).getContents().trim();
				if(tempPos.equals("")){
					uo.setPosNo("0");
				}else{
					uo.setPosNo(tempPos);
				}
				
				
				String tmpDate = sheet0.getCell(SALETIME_POS,i).getContents().replace("-", "").replace("/", "").trim();
				//logger.info(tmpDate); 
				if(tmpDate.length() == 7){
					tmpDate = tmpDate.substring(0,4) + "0" + tmpDate.substring(4,tmpDate.length());
				}
				uo.setSaleTime(tmpDate);
				uo.setType(sheet0.getCell(TYPE_POS,i).getContents().trim());
				uo.setNum(Integer.parseInt(sheet0.getCell(NUM_POS,i).getContents().replace(",", "").trim()));
				uo.setSalePrice(Double.parseDouble(sheet0.getCell(SALEPRICE_POS,i).getContents().replace(",", "").trim()));
				if(!StringUtill.isNull(sheet0.getCell(BACKPOINT_POS,i).getContents())){
					uo.setBackPoint(Double.parseDouble(sheet0.getCell(BACKPOINT_POS,i).getContents().trim()));
				}
				uo.setFileName(srcFile.getName());
				//logger.info(StringUtill.GetJson(uo)); 
				UploadOrders.add(uo);
				uo = new UploadOrder();
			}
	        
		}catch (Exception e){
			e.printStackTrace();
			UploadOrders = new ArrayList<UploadOrder>();
			uo = new UploadOrder();
			uo.setId(-1);
			uo.setName("第"+ (i+1) + "行附近有问题，请检查");
			UploadOrders.add(uo);
			return UploadOrders;
		}finally{
			wb.close();
		}
		return UploadOrders;
	}
	
	//读取指定位置的xls内容，并解析成UploadSalaryModel对象进行返回
		public List<UploadSalaryModel> readSalaryModelXLS(String path,String fileName){
			if(fileName == null || path == null){
				return null;
			}
			
			String filepath = path.replace("\\", "/");
			List <UploadSalaryModel> uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
			UploadSalaryModel usm = new UploadSalaryModel();
			
			int i = 0;
			int j = 0;
			Workbook wb = null;
			try{
				File srcFile = new File(filepath,fileName); 
				wb = Workbook.getWorkbook(srcFile);
				Sheet sheet0 = wb.getSheet(0);
				String tempString = "{";
				Double tempDouble = 0.0;
				
				String name = sheet0.getCell(1,0).getContents().trim();
				
				//如果文件名一样
				if(UploadManager.isSalaryModelFileNameExist(name)){
					uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
					usm = new UploadSalaryModel();
					usm.setId(-1);
					usm.setName("文件名称重复!请修改名称");
					uploadSalaryModelList.add(usm);
					return uploadSalaryModelList;
				}
				
				String startTime = "";
				String endTime = "";
				
				//暂时先去掉门店
				//String shop = sheet0.getCell(7,0).getContents().trim();
				String shop = "无";
				
				
				//暂时先去掉时间
				//excel取到格式有问题，做下特殊处理
				/**
				SimpleDateFormat s1 = new SimpleDateFormat("M/dd/yy HH:mm");
				Date date = s1.parse(sheet0.getCell(3,0).getContents().trim());
				SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
				startTime = s2.format(date);
				
				if(sheet0.getCell(5,0).getContents().trim().equals("/")){
					endTime = "3014-01-01 00:00:00";
				}else{
					endTime = s2.format(s1.parse(sheet0.getCell(5,0).getContents().trim()));
				}
				**/
				
				
				endTime = "3014-01-01 00:00:00";
				startTime = "2014-01-01 00:00:00";
				
				for(i = 2 ; i < sheet0.getRows(); i ++){
					usm.setStartTime(startTime);
					usm.setName(name);
					usm.setEndTime(endTime);
					usm.setShop(shop);
					//判断是否为空行
					if(sheet0.getCell(1,i).getContents().trim().equals("")||sheet0.getCell(2,i).getContents().trim().equals("")){
						break;
					}
					////暂时先去掉类别
					usm.setCatergory(sheet0.getCell(0,i).getContents().trim());
					////型号
					usm.setType(sheet0.getCell(1,i).getContents().trim());
					
					
					//判断提成内容
					for(j = 2 ; j < sheet0.getColumns();j = j+2){
						//第一个就是/的话，默认所有区间都是这个提成标准
						if(j == 2){
							if(sheet0.getCell(j,i).getContents().trim().equals("/")){
								tempString = "{\"";
								tempString += "0-/";
								tempString += "\":";
								tempString += "\"";
								//验证格式
								String.valueOf(Double.parseDouble(sheet0.getCell(j+1,i).getContents().trim().replace("%", "")));
								tempString += sheet0.getCell(j+1,i).getContents().trim();
								tempString += "\"";
								break;
							}
						}
						
						
						//零售价为空，break;
						if(sheet0.getCell(j,i).getContents().trim()==""||sheet0.getCell(j,i).getContents().trim().equals("")){
							
							if(tempString.length() ==0){
								break;
							}
							//如果上一项是最后一项
							if(!tempString.contains("-/")){
								tempString += "\"" + tempDouble + "-/" + "\"" + ":" + tempString.split(":")[tempString.split(":").length - 1];
								// "0-111":"10",
							}
							break;
						}
						//没有成对匹配，throw Exception
						if(sheet0.getCell(j+1,i).getContents().trim()==""||sheet0.getCell(j+1,i).getContents().trim().equals("")){
							throw new Exception();
						}
						
						//如果上一项大于这一项,不让上传
						if( tempDouble  >= Double.parseDouble(sheet0.getCell(j,i).getContents().trim()) ){
							throw new Exception();
						}
						
						//如果格式不对，不让上传
						Double.parseDouble(sheet0.getCell(j,i).getContents().trim());
						Double.parseDouble(sheet0.getCell(j+1,i).getContents().trim().replace("%", ""));
						
						
						//增加提成内容
						tempString += "\"" + tempDouble + "-" + (Double.parseDouble(sheet0.getCell(j,i).getContents().trim()) + 1) + "\"" + ":" + "\"" + sheet0.getCell(j+1,i).getContents().trim() + "\""+",";
						tempDouble = Double.parseDouble(sheet0.getCell(j,i).getContents().trim()) + 1;
						//如果是最后一项
						if(j + 2 >= sheet0.getColumns()){
							tempString += "\"" + tempDouble + "-/" + "\"" + ":" + "\"" + sheet0.getCell(j+1,i).getContents().trim() + "\""+",";
						}
//						if(!sheet0.getCell(j,i).getContents().contains("以上")){
//							tempString += "\"" + sheet0.getCell(j,i).getContents() + "\"" + ":" + "\"" + sheet0.getCell(j+1,i).getContents() + "\""+",";
//							//判断是否是完整的价格区间
//							boolean zone = Double.parseDouble(sheet0.getCell(j,i).getContents().split("-")[0]) == tempInt; 
//							if(zone){
//								tempInt = Double.parseDouble(sheet0.getCell(j,i).getContents().split("-")[1]);
//							}else{
//								throw new Exception();
//							}
//						}else{
//							tempString += "\"" + sheet0.getCell(j,i).getContents().trim().replace("以上", "-/") + "\"" + ":" + "\"" + sheet0.getCell(j+1,i).getContents() + "\""+",";
//							boolean zone = Double.parseDouble(sheet0.getCell(j,i).getContents().replace("以上", "").trim()) == tempInt;
//							if(zone){
//								break;
//							}else{
//								throw new Exception();
//							}
//						}
					}
					
					if(tempString.endsWith(",")){
						tempString = tempString.substring(0,tempString.length() - 1);
					}
					tempString += "}";
					usm.setContent(tempString);
					usm.setFileName(srcFile.getName());
					uploadSalaryModelList.add(usm);
					usm = new UploadSalaryModel();
					tempDouble = 0.0;
					tempString = "{";
				}
			}catch (Exception e){
				e.printStackTrace();
				uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
				usm = new UploadSalaryModel();
				usm.setId(-1);
				usm.setName("第"+ (i+1) + "行第"+ (j+1) + "列附近有问题，请检查");
				uploadSalaryModelList.add(usm);
				return uploadSalaryModelList;
			}finally{
				wb.close();
			}
			return uploadSalaryModelList;
		}
		
		//读销售单
		//读取指定位置的xls内容，并解析成UploadOrder对象进行返回(已经checked的uploadOrder对象)
		public List<UploadOrder> readSalesXLS(String path,String fileName){
			if(fileName == null || path == null){
				return null;
			}
			
			final int SHOP_POS = 0;
			final int SALETIME_POS = 1;
			final int TYPE_POS = 2;
			final int NUM_POS = 3;
			final int SALEPRICE_POS = 4;
			final int BACKPOINT_POS = 5;

			
			String filepath = path.replace("\\", "/");
			List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
			UploadOrder uo = new UploadOrder();
			 
			File srcFile = new File(filepath,fileName); 
			Workbook wb = null;
			try {   
				wb = Workbook.getWorkbook(srcFile);
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}   
			logger.info(wb.getSheets()[0]);  
			Sheet sheet0 = wb.getSheets()[0];  
			logger.info(sheet0);   
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			SimpleDateFormat s1 = new SimpleDateFormat("yy-MM-dd");
			SimpleDateFormat s2 = new SimpleDateFormat("yyyyMMdd");
			 
			String name = "";
			try{
				//logger.info(sheet0.getCell(0,0)); 
				//logger.info(sheet0.getCell(1,0));  
				name = sheet0.getCell(1,0).getContents().trim();
				if(UploadManager.isUploaderFileNameExist(name)){
					UploadOrders = new ArrayList<UploadOrder>();
					uo = new UploadOrder();
					uo.setId(-1); 
					uo.setName("文件名称重复!请修改名称");
					UploadOrders.add(uo); 
					return UploadOrders;
				}
			}catch(Exception e){
				e.printStackTrace();
				UploadOrders = new ArrayList<UploadOrder>();
				uo = new UploadOrder();
				uo.setId(-1);
				uo.setName("文件名有问题，请检查");
				UploadOrders.add(uo);
				return UploadOrders;
			}
			
			
			for(int i = 2 ; i < sheet0.getRows(); i ++){
				
				try{
					if(sheet0.getCell(SHOP_POS,i).getContents().trim().equals("")){
						continue;
					}
					uo.setName(name);
					uo.setShop(sheet0.getCell(SHOP_POS,i).getContents().trim());
					uo.setType(sheet0.getCell(TYPE_POS,i).getContents().trim());
					//uo.setSaleManName(sheet0.getCell(3,i).getContents().trim());
					uo.setSalePrice(Double.parseDouble(sheet0.getCell(SALEPRICE_POS,i).getContents().trim()));
					uo.setNum(Integer.parseInt(sheet0.getCell(NUM_POS,i).getContents().trim()));
					if(!StringUtill.isNull(sheet0.getCell(BACKPOINT_POS,i).getContents())){
						uo.setBackPoint(Double.parseDouble(sheet0.getCell(BACKPOINT_POS,i).getContents().trim()));
					}
				}catch(Exception e){
					e.printStackTrace();
					UploadOrders = new ArrayList<UploadOrder>();
					uo = new UploadOrder();
					uo.setId(-1);
					uo.setName("第"+ (i+1) + "行附近有问题，请检查");
					UploadOrders.add(uo);
					return UploadOrders;
				}
				
				if(!"".equals(sheet0.getCell(SALETIME_POS,i).getContents().trim())){
					try {
						uo.setSaleTime(s2.format(s1.parse(sheet0.getCell(SALETIME_POS,i).getContents().trim())));
					} catch (ParseException e) {
						s1 = new SimpleDateFormat("MM/dd/yy");
						try {
							uo.setSaleTime(s2.format(s1.parse(sheet0.getCell(SALETIME_POS,i).getContents().trim())));
						} catch (ParseException e1) {
							s1 = new SimpleDateFormat("yyyyMMdd");
							try{
								uo.setSaleTime(s2.format(s1.parse(sheet0.getCell(SALETIME_POS,i).getContents().trim())));
							}catch(ParseException e2){
								e.printStackTrace();
								UploadOrders = new ArrayList<UploadOrder>();
								uo = new UploadOrder();
								uo.setId(-1);
								uo.setName("第"+ (i+1) + "行附近有问题，请检查");
								UploadOrders.add(uo);
								return UploadOrders;
							}						
						}
					} catch(Exception e){
						e.printStackTrace();
						UploadOrders = new ArrayList<UploadOrder>();
						uo = new UploadOrder();
						uo.setId(-1);
						uo.setName("第"+ (i+1) + "行附近有问题，请检查");
						UploadOrders.add(uo);
						return UploadOrders;
					}
				}else{
					uo.setSaleTime("");
				}
				
				uo.setFileName(srcFile.getName());
				uo.setChecked(0);
				uo.setCheckedTime(fmt.format(new Date()));
				uo.setCheckOrderId(-1);
				UploadOrders.add(uo);
				uo = new UploadOrder();
			}
	        wb.close();
			
			return UploadOrders; 
		}
		
		public UploadChangeAll readchangeXML(String path,String fileName){
			UploadChangeAll all = new UploadChangeAll(); 
			//Map<String, Set<String>> map = b.getMaplist();
			if(fileName == null || path == null){
				return null;  
			} 
			
			String filepath = path.replace("\\", "/");
			//List<String> list =   new ArrayList<String>();
			File srcFile = new File(filepath,fileName); 
			Workbook wb = null;
			try {
				wb = Workbook.getWorkbook(srcFile);
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			Sheet sheet0 = wb.getSheet(0);
			
			String name = "";
			try{ 
				name = sheet0.getCell(1,0).getContents().trim();
				//if(UploadManager.isUploaderFileNameExist(name)){
					//all.setFilename("文件名称重复!请修改名称");
					//return all;
				//}else {      
					all.setFilename(name);
				//} 
				  
				if(sheet0.getColumns() >2){
					all.setFilename("您上传的文件格式有问题");
					return all;
				} 
				
			}catch(Exception e){
				e.printStackTrace();
				all.setFilename("文件名有问题，请检查");
				return all;
			}  
			
			
			logger.info(sheet0.getRows()); 
			//logger.info(sheet0.getColumns()); 
			for(int i = 2 ; i < sheet0.getRows(); i ++){
				 
				try{
					
					int j = 0 ;  
					 
					while(j < 2){  
						String str = sheet0.getCell(j,i).getContents().trim();
						if(StringUtill.isNull(str) ){
							j++; 
							continue;
						}else { 
							if(j == 0){
								all.getBranch().add(str);
							}else { 
								all.getTypes().add(str); 
							}   
						}
						j++;
					}
					 
				}catch(Exception e){
					e.printStackTrace();
					 
					all.setFilename("第"+ (i+1) + "行附近有问题，请检查");
					
					return all;
				}
			}
	        wb.close();
			
			return all;
		}
		 
		 
		public List<Product> readProductXML(String path,String fileName,String categoryID){
			List<Product> list = new ArrayList<Product>(); 
			//Map<String, Set<String>> map = b.getMaplist();
			if(fileName == null || path == null){
				return null;  
			}   
			//logger.info(path);  
			String filepath = path.replace("\\", "/");
			logger.info(filepath); 
			//List<String> list =   new ArrayList<String>();
			File srcFile = new File(filepath,fileName); 
			logger.info(srcFile.getAbsolutePath()); 
			logger.info(srcFile.getPath());  
			Workbook wb = null; 
			try { 
				wb = Workbook.getWorkbook(srcFile);
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			Sheet sheet0 = wb.getSheet(0);
             
			int row = sheet0.getRows();
			int co = sheet0.getColumns();
			//logger.info(row);  
			//logger.info(co);   
			//logger.info(sheet0.getColumns()); 
			for(int i = 1 ; i < row; i ++){ 
				Product p = new Product();
				p.setCategoryID(Integer.valueOf(categoryID));
				//boolean flag = false ;
				try{

					for(int j=0;j < co;j++){  
						String str = sheet0.getCell(j,i).getContents().trim();
						if(j==0){
							if(StringUtill.isNull(str)){
								p.setType("第"+j+1+"行,第"+i+"列型号不能为空");
								return list;
								//flag = true ;
							}else {     
	                        	p.setType(str);
								
							};
						}else if(j == 1){
							if(StringUtill.isNull(str)){
								p.setType("第"+j+1+"行,第"+i+"列商品编码不能为空");
								return list; 
							}else {   
								p.setEncoded(str); 
								
							};
						}else if(j == 2){
							try{
								 double b = Double.parseDouble(str);
								  p.setSize(b);
								}catch(Exception e)
								{  
									logger.info(e);
								}  
						}else if(j == 3){ 
							try{
								 double b = Double.parseDouble(str);
								  p.setStockprice(b);
								}catch(Exception e)
								{   
								 logger.info(e);
								}  
						}else if(j == 4){
							try{
								int b = Integer.valueOf(str);
								p.setSaleType(b);
								}catch(Exception e)
								{ 
									logger.info(e);
								}
						}	
					} 
					
					boolean flag = true ;
					if(StringUtill.isNull(p.getType())){
						flag = false; 
					}
					for(int m=0;m<list.size();m++){
						Product pro  = list.get(m);
						if(pro.getType().equals(p.getType())){
							//logger.info(3);
							flag = false;
							break ;  
						}
					} 	
					 
					if(flag){
						list.add(p);
					}
				

				}catch(Exception e){
					logger.info(e);
				} 
			}
	        wb.close();
			return list;
		}
		
}
