package wilson.upload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;


public class XLSReader {
	
	//读取指定位置的xls内容，并解析成UploadOrder对象进行返回
	public List<UploadOrder> readSuningXLS(String path,String fileName){
		//D:/software/apache-tomcat-7.0.21/webapps/meidi/data/xls/2014092412.xls
		if(fileName == null || path == null){
			return null;
		}
		
		String filepath = path.replace("\\", "/");
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrder uo = new UploadOrder();
		try{
			File srcFile = new File(filepath,fileName); 
			Workbook wb = Workbook.getWorkbook(srcFile);
			Sheet sheet0 = wb.getSheet(0);
			
			String name = sheet0.getCell(1,0).getContents();
			for(int i = 2 ; i < sheet0.getRows(); i ++){
				if(sheet0.getCell(0,i).getContents().equals("")){
					continue;
				}
				uo.setName(name);
				uo.setShop(sheet0.getCell(0,i).getContents());
				uo.setPosNo(sheet0.getCell(1,i).getContents());
				uo.setSaleTime(sheet0.getCell(2,i).getContents());
				uo.setDealTime(sheet0.getCell(3,i).getContents());
				uo.setType(sheet0.getCell(4,i).getContents());
				uo.setNum(Integer.parseInt(sheet0.getCell(5,i).getContents()));
				uo.setSalePrice(Double.parseDouble(sheet0.getCell(6,i).getContents()));
				uo.setBackPoint(Double.parseDouble(sheet0.getCell(7,i).getContents()));
				uo.setFileName(srcFile.getName());
				UploadOrders.add(uo);
				uo = new UploadOrder();
			}
	        wb.close();
		}catch (Exception e){
			e.printStackTrace();
			return null;
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
			try{
				File srcFile = new File(filepath,fileName); 
				Workbook wb = Workbook.getWorkbook(srcFile);
				Sheet sheet0 = wb.getSheet(0);
				String tempString = "{";
				Double tempInt = 0.0;
				
				String name = sheet0.getCell(1,0).getContents();
				String startTime = "";
				String endTime = "";
				String shop = sheet0.getCell(7,0).getContents();
				
				//excel取到格式有问题，做下特殊处理
				SimpleDateFormat s1 = new SimpleDateFormat("M/dd/yy HH:mm");
				Date date = s1.parse(sheet0.getCell(3,0).getContents());
				SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
				startTime = s2.format(date);
				
				if(sheet0.getCell(5,0).getContents().equals("/")){
					endTime = "3014-01-01 00:00:00";
				}else{
					endTime = s2.format(s1.parse(sheet0.getCell(5,0).getContents()));
				}
				
				
				
				
				
				for(i = 2 ; i < sheet0.getRows(); i ++){
					usm.setStartTime(startTime);
					usm.setName(name);
					usm.setEndTime(endTime);
					usm.setShop(shop);
					if(sheet0.getCell(1,i).getContents().equals("")||sheet0.getCell(2,i).getContents().equals("")){
						break;
					}
					usm.setCatergory(sheet0.getCell(1,i).getContents());
					usm.setType(sheet0.getCell(2,i).getContents());
					for(j = 3 ; j < sheet0.getColumns();j = j+2){
						if(sheet0.getCell(j,i).getContents()==""||sheet0.getCell(j,i).getContents().equals("")){
							break;
						}
						
						if(sheet0.getCell(j+1,i).getContents()==""||sheet0.getCell(j+1,i).getContents().equals("")){
							throw new Exception();
						}
						
						tempString += "\"" + sheet0.getCell(j,i).getContents() + "\"" + ":" + "\"" + sheet0.getCell(j+1,i).getContents() + "\""+",";
						
						//判断是否是完整的价格区间
						boolean zone = Double.parseDouble(sheet0.getCell(j,i).getContents().split("-")[0]) == tempInt; 
						if(zone){
							if(sheet0.getCell(j,i).getContents().split("-")[1].equals("/")){
								break;
							}else{
								tempInt = Double.parseDouble(sheet0.getCell(j,i).getContents().split("-")[1]);
							}
						}else{
							throw new Exception();
						}
					}
					
					if(tempString.endsWith(",")){
						tempString = tempString.substring(0,tempString.length() - 1);
					}
					tempString += "}";
					usm.setContent(tempString);
					usm.setFileName(srcFile.getName());
					uploadSalaryModelList.add(usm);
					usm = new UploadSalaryModel();
					tempInt = 0.0;
					tempString = "{";
				}
		        wb.close();
			}catch (Exception e){
				e.printStackTrace();
				uploadSalaryModelList = new ArrayList<UploadSalaryModel>();
				usm = new UploadSalaryModel();
				usm.setId(-1);
				usm.setContent("第"+ (i+1) + "行第"+ (j+1) + "列附近有问题，请检查");
				uploadSalaryModelList.add(usm);
			}
			return uploadSalaryModelList;
		}
		
		//读取指定位置的xls内容，并解析成UploadOrder对象进行返回(已经checked的uploadOrder对象)
		public List<UploadOrder> readSalesXLS(String path,String fileName){
			if(fileName == null || path == null){
				return null;
			}
			
			String filepath = path.replace("\\", "/");
			List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
			UploadOrder uo = new UploadOrder();
			try{
				File srcFile = new File(filepath,fileName); 
				Workbook wb = Workbook.getWorkbook(srcFile);
				Sheet sheet0 = wb.getSheet(0);
				SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat s1 = new SimpleDateFormat("yy-MM-dd");
				SimpleDateFormat s2 = new SimpleDateFormat("yyyyMMdd");
				String name = sheet0.getCell(1,0).getContents();
				for(int i = 2 ; i < sheet0.getRows(); i ++){
					if(sheet0.getCell(0,i).getContents().equals("")){
						continue;
					}
					uo.setName(name);
					uo.setShop(sheet0.getCell(1,i).getContents());
					uo.setType(sheet0.getCell(2,i).getContents());
					uo.setSaleManName(sheet0.getCell(3,i).getContents());
					uo.setSalePrice(Double.parseDouble(sheet0.getCell(4,i).getContents()));
					uo.setNum(Integer.parseInt(sheet0.getCell(5,i).getContents()));
					uo.setSaleTime(s2.format(s1.parse(sheet0.getCell(6,i).getContents())));
					uo.setFileName(srcFile.getName());
					uo.setChecked(0);
					uo.setCheckedTime(fmt.format(new Date()));
					uo.setCheckOrderId(-1);
					UploadOrders.add(uo);
					uo = new UploadOrder();
				}
		        wb.close();
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
			return UploadOrders;
		}
}
