package httpClient.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import httpClient.MyLogin;
import httpClient.MyMainClient;
import httpClient.inventoryOrder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import product.Product;
import product.ProductService;
import branch.Branch;
import branch.BranchService;

import com.csvreader.CsvReader;

import enums.SaleModel;

import user.User;
import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
public class InventoryModelDownLoad extends HttpServlet implements DownLoad {
	/**
	 *   
	 */
	private static final long serialVersionUID = 1L;
	protected static Log logger = LogFactory.getLog(InventoryModelDownLoad.class);  
	private static String url = "http://scs.suning.com/sps/SampleMachineReport/downLoadSampleMachineReport.action"; 
	public static void main(String args[]){  
		InventoryModelDownLoad id = new InventoryModelDownLoad();
		//id.get();   
		String nowpath=System.getProperty("user.dir");
		logger.info(nowpath);   
		logger.info(new File("").getAbsolutePath());
	}   
	    
	// 型号 , 状态
		public static Map<String,List<Inventory>> getMapBranchType(User user ,String startTime,int branchid) {
			// startTime = "2015-05-03"; 
			// List<Inventory> list = new ArrayList<Inventory>();
			Branch branch = BranchService.getMap().get(branchid);
			String bnum = branch.getEncoded();   
			//logger.info()  
			Map<String, Product> mapp = ProductService.gettypeNUmmap(); 
			  
			//logger.info(mapp);   
			
			Map<String,List<Inventory>> map = new HashMap<String,List<Inventory>>();
			if (branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
					.getValue()) {
			try {  
				String tempPath =PathUtill.getXMLpath();
				tempPath +=  "data" + File.separator
						+ "DownloadInventory" + File.separator + startTime;
				logger.info(tempPath);
				File file = new File(tempPath);
				if (!file.exists()) { 
					file.mkdirs();
				}
	  
				File file2 = new File(tempPath + File.separator + "model.csv");
				// file2.createNewFile();
 
				CsvReader reader = new CsvReader(file2.getPath(), ',',
						Charset.forName("GBK")); // 一般用这编码读就可以了

				reader.readHeaders();

				while (reader.readRecord()) { // 逐行读入除表头的数据
					String[] strs = reader.getValues();
					if (null != strs) {
						Inventory in = new Inventory();
						for (int i = 0; i < strs.length; i++) {
							// logger.info(i);
							String str = strs[i];
							//logger.info(str);
							if (i == 0) {   
								// logger.info(str);
								in.setGoodType("样机");
							} else if (i == 2) {
								
								in.setBranchName(str);
							}  else if (i == 3) { 
								in.setBranchNum(str); 
							}else if (i == 6) {
								in.setGoodGroupName(str);
							} else if (i == 7) {
								in.setGoodGroupNum(str);
							} else if (i == 10) {
								in.setGoodpName(str);
							} else if (i == 11) {
								in.setGoodNum(str); 
							} else if (i == 12) { 
								double realnum = Double.valueOf(str);
								int re = (int) realnum; 
								in.setNum(re);
							} else if (i == 13) {  
								in.setSerialNumber(str);  
							} 
						}     
	     
						//logger.info(in.getBranchNum()); 
						String bnu = in.getBranchNum();
						//  logger.info(bnu); 
						 // logger.info(in.getBranchNum());
						if(bnum.equals(bnu)){ 
							String key = in.getGoodNum();
							//logger.info(key);
							Product p = mapp.get(key);
							//logger.info(p);  
							if(null != p ){ 
								String pname = mapp.get(key).getType();
						 
							  
									 
									List<Inventory> inmap = map.get(pname);
									         
									if (inmap == null) {
										inmap = new  ArrayList<Inventory>(); 
										map.put(pname, inmap); 
									} 
									 
									inmap.add(in);
							
								
							}
							}
							
						
						
					}
				}
 
				logger.info(map.size());
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();  
			} // 跳过表头 如果需要表头的话，不要写这句。
			}
			return map;
		}
		 
     public static void save(){  
    	 URI uri; 
		try {
			uri = new URI(url);
    	 HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("productCode", "")  
					//.addParameter("formName", "reportQueryConditon")
					//.addParameter("receiveCode", "")
					
					// .addParameter("buyerProductcode", "101123403")
					.build(); 
    	  
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(request); 
			
			int statusCode = response2.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {

				try {
					MyLogin.loginpost(new URI(MyLogin.url));
				} catch (URISyntaxException e) {
					logger.info(e);
				}
				response2 = MyMainClient.getHttpclient().execute(request);
 
			}
			
HttpEntity entity = response2.getEntity();
			
			//EntityUtils.consume(entity);
			
			InputStream in = entity.getContent(); 
			
			String tempPath =PathUtill.getXMLpath();          
			tempPath+="data" + File.separator + "DownloadInventory"+File.separator+TimeUtill.getdateString();  
		    
			logger.info(tempPath);  
			   
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs(); 
			} 
    
			File file2 = new File(tempPath + File.separator + 
					 "model.csv"); 
			
			if(file2.exists()){
				return ; 
			}
			file2.createNewFile();  
			     
		    try {   
		        FileOutputStream fout = new FileOutputStream(file2);  
		        int l = -1;  
		        byte[] tmp = new byte[1024];  
		        while ((l = in.read(tmp)) != -1) {  
		            fout.write(tmp, 0, l);  
		            // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试  
		        }   
		        fout.flush();  
		        fout.close();  
		        response2.close();
		    } finally {  
		        // 关闭低层流。  
		        in.close();  
		    }  
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} 
			
     }
}
