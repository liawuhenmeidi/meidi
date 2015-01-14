package wilson.salaryCalc;

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

import org.apache.poi.hssf.record.formula.functions.Rows;

import jxl.Sheet;
import jxl.Workbook;

import utill.StringUtill;
import wilson.catergory.HiddenCatergoryMapingManager;
import wilson.download.ExcelGenerator;
import wilson.upload.UploadManager;

public class HiddenFileManager {
	public static String basePath = "";
	
	public static String dataPath = "";
	public static String hiddenFilePath = "";
	public static String properFilePath = "";
	
	public static final String downloadPath="data/hiddenFile/";
	public static final String fileType = ".xls";
	
	public static File indexFile;
	public static File hiddenFileDIR;
	
	public static void initPath(String inputBasePath){
		basePath = inputBasePath;
		dataPath = basePath + "data";
		hiddenFilePath = dataPath + File.separator + "hiddenFile";
		properFilePath = hiddenFilePath + File.separator + "index.properties";
		indexFile = new File(properFilePath);
		hiddenFileDIR = new File(hiddenFilePath);
	}
	
	public static boolean checkFile(){
		boolean result = false;
		try {
			//确认目录存在，不存在的时候创建目录
			if (!hiddenFileDIR.exists()) {
				hiddenFileDIR.mkdirs();
			}
			//确认index.properties文件存在，不存在时候，创建
			if (!indexFile.exists()) {
				indexFile.createNewFile();
			}
			
			
			result = true;
		} catch (IOException e) {
			result  = false;
		}
		return result;
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
	
	public static List<String> getFileNames(){
		List<String> result = new ArrayList<String>();
		//判断存在性
		if(!checkFile()){
			return result;
		}
		
		Properties props = getProperFile();
		Iterator it = props.keySet().iterator();
		while(it.hasNext()){
			result.add(props.getProperty((String)it.next()));
		}
		
		return result;
	}
	
	public static boolean delFile(String value){
		boolean result = false;
		
		//判断存在性
		if(!checkFile()){
			return false;
		}
		Properties props = getProperFile();
		boolean isContain = false;
		isContain = props.contains(value);
		
		if(!isContain){
			return true;
		}
		try{
			String delFileName = getKey(props, value);
			
			File delFile = new File(hiddenFilePath + File.separator + delFileName);

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
	
	public static boolean transferFile(String name) throws IOException{
		boolean result = false;
		
		if(StringUtill.isNull(name) || name.trim().equals("")){
			return result;
		}
		
		//判断存在性
		if(!checkFile()){
			return false;
		}
		
		//是否有重名问题，有重名问题，返回false
		Properties props = getProperFile();

		boolean isContain = false;
		isContain = props.contains(name);
		
		if(isContain){
			return false;
		}
		//数据库中文件取出
		List<SalaryResult> lists = new ArrayList<SalaryResult>();
		lists = SalaryCalcManager.getSalaryResultByName(name);
		
		//排序
		lists = SalaryCalcManager.sortSalaryResult(lists,true);
		//清理catergoryMaping文件
		HiddenCatergoryMapingManager.delCatergoryMaping(name);
		
		//生成excel,写文件
		String randomName = UUID.randomUUID().toString() + fileType;
		if(!ExcelGenerator.generateSalaryResultExcel(hiddenFilePath,randomName, lists)){
			return false;
		}
		props.setProperty(randomName,name);
		
		//写properties
		saveProperFile("Update " + randomName,props);

		//清数据库
		result = UploadManager.deleteUploadOrderByName(name);

		return result;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static ExcelShowModel getFileContent(String value){
		ExcelShowModel result = new ExcelShowModel();
		Workbook wb = null ;
		//判断存在性
		if(!checkFile()){
			return result;
		}
		try{
			Properties props = getProperFile();
			String filename = getKey(props, value);
			File srcFile = new File(hiddenFilePath,filename);
			if(!srcFile.exists()){
				return result;
			}
			wb = Workbook.getWorkbook(srcFile);
			Sheet sheet0 = wb.getSheet(0);
			
			result.setColc(sheet0.getColumns());
			result.setRows(sheet0.getRows());
			String [] [] tmp;
			tmp = new String[result.getRows()][result.getColc()];
			
			for(int i = 0 ; i < result.getRows() ; i ++){
				for(int j = 0  ; j < result.getColc(); j++){
					tmp[i][j] = sheet0.getCell(j,i).getContents();
				}
			}
			result.setContent(tmp);
		}catch(Exception e){
			e.printStackTrace();
			result = new ExcelShowModel();
			return result;
		}finally{
			if(wb != null){
				wb.close();
			}
		}
		
		return result;
	}
	
	public static String getDownloadPath(String value){
		String result = "";
		//判断存在性
		if(!checkFile()){
			return result;
		}
		
		//是否有重名问题，有重名问题，返回false
		Properties props = getProperFile();

		boolean isContain = false;
		isContain = props.contains(value);
		if(!isContain){
			return result;
		}
		result = downloadPath + getKey(props, value);
		
		return result;
	}
}
