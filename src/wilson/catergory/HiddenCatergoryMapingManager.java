package wilson.catergory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import jxl.Sheet;
import jxl.Workbook;

import utill.StringUtill;
import wilson.download.ExcelGenerator;
import wilson.upload.UploadManager;

public class HiddenCatergoryMapingManager {
	public static String basePath = "";
	
	public static String dataPath = "";
	public static String hiddenCatergoryMapingPath = "";
	public static String properFilePath = "";
	
	public static final String fileType = ".xls";
	
	public static File indexFile;
	public static File hiddenCatergoryMapingDIR;
	
	public static void initPath(String inputBasePath){
		basePath = inputBasePath;
		dataPath = basePath + "data";
		hiddenCatergoryMapingPath = dataPath + File.separator + "hiddenCatergoryMaping";
		properFilePath = hiddenCatergoryMapingPath + File.separator + "index.properties";
		indexFile = new File(properFilePath);
		hiddenCatergoryMapingDIR = new File(hiddenCatergoryMapingPath);
	}
	public static Properties getProperFile(){
		//是否有重名问题，有重名问题，返回false
		Properties props = new Properties();
		try{
			InputStream fis = new FileInputStream(indexFile);
			props.load(fis);
			fis.close();
		}catch(Exception e){
			return null;
		}
		return props;
	}
	
	
	public static boolean saveProperFile(String comment,Properties props){
		boolean result = false;
		OutputStream out;
		try {
			out = new FileOutputStream(indexFile);
			props.store(out, comment);
			out.close();
			result = true;
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getKey(Properties props,String value){
		String result = "";
		String tmpVal = "";
		String tmpKey = "";
		Iterator it = props.keySet().iterator();
		while(it.hasNext()){
			tmpKey = (String)it.next();
			tmpVal = (String) props.get(tmpKey);
			if(tmpVal.equals(value)){
				result = tmpKey;
				break;
			}
		}
		return result;
	}
	
	public static boolean checkFile(){
		boolean result = false;
		try {
			//确认目录存在，不存在的时候创建目录
			if (!hiddenCatergoryMapingDIR.exists()) {
				hiddenCatergoryMapingDIR.mkdirs();
			}
			//确认index.properties文件存在，不存在时候，创建
			if (!indexFile.exists()) {
				indexFile.createNewFile();
			}
			
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			result  = false;
		}
		return result;
	}
	
	public static boolean saveCatergoryMaping(String filename,String catergoryMapingName){
		boolean result = false;
		
		if(StringUtill.isNull(filename)|| StringUtill.isNull(catergoryMapingName)){
			return result;
		}
		
		//判断存在性
		if(!checkFile()){
			return result;
		}
		Properties props = getProperFile();
		boolean isContain = false;
		isContain = props.contains(filename);
		
		if(isContain){
			return result;
		}
		
		//从数据库取数据
		List<CatergoryMaping> mapings = CatergoryManager.getCatergory(catergoryMapingName);
		
		//生成excel,写文件
		String randomName = UUID.randomUUID().toString() + fileType;
		if(!ExcelGenerator.generateCatergoryMapingExcel(hiddenCatergoryMapingPath,randomName, mapings)){
			return false;
		}
		props.setProperty(randomName,filename);
		
		//写properties
		result = saveProperFile("Update " + randomName,props);
		
		
		return result;
	}
	
	public static boolean delCatergoryMaping(String filename){
		boolean result = false;
		
		//判断存在性
		if(!checkFile()){
			return false;
		}
		Properties props = getProperFile();
		boolean isContain = false;
		isContain = props.contains(filename);
		
		if(!isContain){
			return true;
		}
		try{
			String delFileName = getKey(props, filename);
			
			File delFile = new File(hiddenCatergoryMapingPath + File.separator + delFileName);

			if(delFile.exists()){
				boolean deleteResult = delFile.delete();
				System.out.println("删除" + (deleteResult?"成功":"失败"));
			}
			props.remove(delFileName);
			saveProperFile("del " + delFileName, props);
			result = true;
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	
	public static List<CatergoryMaping> getCatergoryMapings(String filename){
		List<CatergoryMaping> result = new ArrayList<CatergoryMaping>();
		//判断存在性
		if(!checkFile()){
			return result;
		}
		Properties props = getProperFile();
		boolean isContain = false;
		isContain = props.contains(filename);
		
		if(!isContain){
			return result;
		}
		Workbook wb = null ;
		try{
			String key = getKey(props, filename);
			File srcFile = new File(hiddenCatergoryMapingPath,key);
			if(!srcFile.exists()){
				return result;
			}
			wb = Workbook.getWorkbook(srcFile);
			Sheet sheet0 = wb.getSheet(0);
			
			CatergoryMaping cm = new CatergoryMaping();
			for(int i = 1 ; i < sheet0.getRows() ; i++){
				//cm.setId(Integer.parseInt(sheet0.getCell(0, i).getContents()));
				cm.setName(sheet0.getCell(1,i).getContents());
				cm.setShop(sheet0.getCell(2,i).getContents());
				cm.setContent(sheet0.getCell(3,i).getContents());
				cm.setModifyTime(sheet0.getCell(4,i).getContents());
				result.add(cm);
				cm = new CatergoryMaping();
			}

		}catch(Exception e){
			e.printStackTrace();
			result.clear();
		}finally{
			if(wb != null){
				wb.close();
			}
		}
		
		return result;
	}
}
