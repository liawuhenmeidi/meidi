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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import goodsreceipt.GoodsReceipt;
import goodsreceipt.GoodsReceitManager;
import goodsreceipt.OrderSN;
import goodsreceipt.OrderSNManager;
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

import com.csvreader.CsvReader;

import utill.DBUtill;
import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
public class OrderDownLoad extends HttpServlet implements DownLoad {
	/**     
	 *   
	 */ 
	private static final long serialVersionUID = 1L; 
	protected static Log logger = LogFactory.getLog(OrderDownLoad.class);
	
	private static String url = "http://scs.suning.com/sps/PurchaseOrderConfirm/downLoadpurchaseOrderDetails.action"; 
	
	public static Map<String,SNInventory> getMap(String startTime,String endTime) {
		// startTime = "2015-05-03"; 
		
		Map<String,SNInventory> map = new HashMap<String,SNInventory>();
		save(startTime, endTime); 
		if(StringUtill.isNull(startTime) || StringUtill.isNull(endTime)){
			return map ;
		}  
		try {
			String tempPath =PathUtill.getXMLpath();
			tempPath +="data" + File.separator
					+ "DownloadSale"; 
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}
 
			File file2 = new File(tempPath + File.separator + "sale.csv");
			// file2.createNewFile();

			CsvReader reader = new CsvReader(file2.getPath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {
					
					SNInventory in = new SNInventory();
					
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						if (i == 2) {
							// logger.info(str);
							in.setBranchNum(str);
						} else if (i == 3) {
							in.setBranchName(str);
						} else if (i == 6) {
							in.setGoodpName(str);
							 
						} else if (i == 7) { 
							in.setGoodNum(str);
						} else if (i == 8) {
							in.setSaleNum(Integer.valueOf(str));
						} 

					}
					String key = in.getBranchNum()+"_"+in.getGoodNum();
					//logger.info(key);
					SNInventory inmap = map.get(key);
					
					if(null == inmap){ 
						map.put(key, in);
					}else {
						inmap.setSaleNum(inmap.getSaleNum()+in.getSaleNum());
					}
					
				} 
			} 
 
			logger.info(map.size());
			reader.close();
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 跳过表头 如果需要表头的话，不要写这句。
		return map; 
	}        
	        
	public static void saveDB(String starttime,String endtime){
		 List<OrderSN > list = get(starttime,endtime); 
		//logger.info(StringUtill.GetJson(list));      
		 List<String> listsql = OrderSNManager.save(list,starttime,endtime);
		    
		// logger.info(StringUtill.GetJson(listsql)); 
		     
		 DBUtill.sava(listsql ); 
		  
	} 
	
	public static List<OrderSN > geteffective(String starttime,String endtime) {
		// startTime = "2015-05-03"; 
		effectivesave(starttime, endtime);  
		  
		List<OrderSN> list = new ArrayList<OrderSN>();
		
		if(StringUtill.isNull(starttime) || StringUtill.isNull(endtime)){
			return list ;
		} 
		try { 
			String tempPath =PathUtill.getXMLpath();
 
			tempPath += "data" + File.separator
					+ "DownloadOrder"+File.separator+"SuNing";    
			logger.info(tempPath);   
			 
			File file = new File(tempPath);
			if (!file.exists()) { 
				file.mkdirs(); 
			}
 
			File file2 = new File(tempPath + File.separator+"effective.csv");
			// file2.createNewFile();
 
			CsvReader reader = new CsvReader(file2.getPath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {
					OrderSN in = new OrderSN();
					String num = "";
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						// logger.info(str);
						if (i == 0) { 
							
							in.setOrderNum(str);
						} else if (i == 1) {
							num = str ;
						} else if (i == 2) {
							in.setGoodNum(str);
							
						} else if (i == 3) {
							in.setGoodpName(str);
						} else if (i == 6) {
							in.setBranchName(str);
						} else if (i == 7) {
						in.setGoodType(str);
						} else if (i == 8) {
							in.setStarttime(str);
						} else if (i == 9) {
							in.setEndtime(str);
						}else if (i == 11) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							in.setNum(re);
						} else if (i == 12) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							in.setInNum(re);
						}else if (i == 13) {
							in.setStatuesName(str);
						}  
                        
					}
					in.setUuid(in.getOrderNum()+"_"+num);
					list.add(in); 
				}
			} 
  
			logger.info(list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);  
		} // 跳过表头 如果需要表头的话，不要写这句。
		return list;
	}
	
	
	public static List<OrderSN > get(String starttime,String endtime) {
		// startTime = "2015-05-03"; 
		save(starttime, endtime);  
		  
		List<OrderSN> list = new ArrayList<OrderSN>();
		
		if(StringUtill.isNull(starttime) || StringUtill.isNull(endtime)){
			return list ;
		} 
		try { 
			String tempPath =PathUtill.getXMLpath();
 
			tempPath += "data" + File.separator
					+ "DownloadOrder"+File.separator+"SuNing";    
			logger.info(tempPath);  
			
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs(); 
			}
 
			File file2 = new File(tempPath + File.separator+"order.csv");
			// file2.createNewFile();
 
			CsvReader reader = new CsvReader(file2.getPath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {
					OrderSN in = new OrderSN();
					String num = "";
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						// logger.info(str);
						if (i == 0) { 
							
							in.setOrderNum(str);
						} else if (i == 1) {
							num = str ;
						} else if (i == 2) {
							in.setGoodNum(str);
							
						} else if (i == 3) {
							in.setGoodpName(str);
						} else if (i == 6) {
							in.setBranchName(str);
						} else if (i == 7) {
						in.setGoodType(str);
						} else if (i == 8) {
							in.setStarttime(str);
						} else if (i == 9) {
							in.setEndtime(str);
						}else if (i == 11) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							in.setNum(re);
						} else if (i == 12) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							in.setInNum(re);
						}else if (i == 13) {
							in.setStatuesName(str);
						}  
                        
					}
					in.setUuid(in.getOrderNum()+"_"+num);
					list.add(in); 
				}
			} 
  
			logger.info(list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);  
		} // 跳过表头 如果需要表头的话，不要写这句。
		return list;
	}
	
	 
	public static void effectivesave(String starttime,String endtime){  
    	URI uri;  
		try { 
			uri = new URI(url); 
    	 HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("orderEffectiveDate1", starttime) 
					.addParameter("orderEffectiveDate2", endtime)
					.addParameter("choosedtab", "1")
					.addParameter("tabClick", "false")
					  
					// .addParameter("buyerProductcode", "101123403")
					.build();  
 //logger.info(123); 	      
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(request); 
		
			
			
			
			int statusCode = response2.getStatusLine().getStatusCode();
 logger.info(statusCode); 
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
			
			String tempPath = PathUtill.getXMLpath(); 
			logger.info(tempPath); 
			tempPath +="data" + File.separator + "DownloadOrder"+File.separator+"SuNing";  
		 
			
			logger.info(tempPath);   
			  
			File file = new File(tempPath);
			if (!file.exists()) { 
				file.mkdirs();  
			}  
    
			File file2 = new File(tempPath + File.separator + 
					"effective.csv");   
			   
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
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
     }
	
	
     public static void save(String starttime,String endtime){  
    	URI uri;  
		try { 
			uri = new URI(url); 
    	 HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("orderCreateDate1", starttime) 
					.addParameter("orderCreateDate2", endtime)
					.addParameter("choosedtab", "1")
					.addParameter("tabClick", "false")
					  
					// .addParameter("buyerProductcode", "101123403")
					.build();  
 //logger.info(123); 	      
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(request); 
		
			
			
			
			int statusCode = response2.getStatusLine().getStatusCode();
 logger.info(statusCode); 
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
			
			String tempPath = PathUtill.getXMLpath(); 
			logger.info(tempPath); 
			tempPath +="data" + File.separator + "DownloadOrder"+File.separator+"SuNing";  
		 
			
			logger.info(tempPath);   
			  
			File file = new File(tempPath);
			if (!file.exists()) { 
				file.mkdirs();  
			}  
    
			File file2 = new File(tempPath + File.separator + 
					"order.csv");  
			   
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
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
     }
}
