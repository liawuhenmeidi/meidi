package wilson.upload;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class XLSReader {
	
	//读取指定位置的xls内容，并解析成UploadOrder对象进行返回
	public List<UploadOrder> readXLS(String path,String fileName){
		//D:/software/apache-tomcat-7.0.21/webapps/meidi/data/xls/2014092412.xls
		String filepath = path.replace("\\", "/");
		
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrder uo = new UploadOrder();
		try{
			File srcFile = new File(filepath,fileName); 
			Workbook wb = Workbook.getWorkbook(srcFile);
			Sheet sheet0 = wb.getSheet(0);
			for(int i = 1 ; i < sheet0.getRows(); i ++){
				uo.setShop(sheet0.getCell(0,i).getContents());
				uo.setSaleNo(sheet0.getCell(1,i).getContents());
				uo.setPosNo(sheet0.getCell(2,i).getContents());
				uo.setSaleTime(sheet0.getCell(3,i).getContents());
				uo.setDealTime(sheet0.getCell(4,i).getContents());
				uo.setType(sheet0.getCell(5,i).getContents());
				uo.setNum(Integer.parseInt(sheet0.getCell(6,i).getContents()));
				uo.setSalePrice(Double.parseDouble(sheet0.getCell(7,i).getContents()));
				uo.setBackPoint(Double.parseDouble(sheet0.getCell(8,i).getContents()));
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
}
