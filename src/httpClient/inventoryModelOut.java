package httpClient;

import goodsreceipt.GoodsReceitManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvReader;

import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;
  
public class inventoryModelOut { 
	protected static Log logger = LogFactory.getLog(inventoryModelOut.class); 
	public static String urlOutModel = "http://scs.suning.com/sps/rtOrderReport/querySendModelReport.action";
	
	public static String downurlOutModel = "http://scs.suning.com/sps/rtOrderReport/downLoadReport.action";
	 
	public static String getHtmlStringOut(URI uri, String starttime, String endtime,int page,MyMainClient mc) {
		String str = "";   
		//logger.info(starttime);
		//logger.info(endtime); 
		try {        
			HttpUriRequest selectPost = RequestBuilder
					.post()    
					.setUri(uri)   
					.addParameter("flage", "4") 
					.addParameter("formName", "reportQueryConditon")
					.addParameter("receiveCode","")
					.addParameter("receivedDate1", 
							starttime) 
					.addParameter("receivedDate2", endtime)
					.addParameter("page", page+"")  
					//.addParameter("buyerProductcode", "101123403")
					.build();

			CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(
					selectPost); 
			
			 int statusCode = response2.getStatusLine().getStatusCode();
             
             if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
            	  
            	 try { 
					MyLogin.loginpost(new URI(MyLogin.url));
				} catch (URISyntaxException e) {
					logger.info(e);
				}
            	 response2 = MyMainClient.getHttpclient()
     					.execute(selectPost);
            	 
             }
             
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				
	            
	             
				str = EntityUtils.toString(entity2, "UTF-8");
				
			}
		 
			response2.close();
			
		} catch (ClientProtocolException e) {
			logger.info(e);
		} catch (IOException e) {
			logger.info(e);
		}

		return str;
	}
	 
	public static String getHtmlStringOutModel(URI uri, String starttime, String endtime,int page,MyMainClient mc) {
		String str = "";   
		//logger.info(starttime);
		//logger.info(endtime);
		try {     
			HttpUriRequest selectPost = RequestBuilder
					.post()  
					.setUri(uri)    
					.addParameter("flage", "4") 
					.addParameter("formName", "reportModelConditon")
					.addParameter("receiveCode","") 
					.addParameter("receivedDate1",  
							starttime) 
					.addParameter("receivedDate2", endtime)
					.addParameter("page", page+"")  
					//.addParameter("buyerProductcode", "101123403")
					.build();

			CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(
					selectPost); 
			int statusCode = response2.getStatusLine().getStatusCode();
            
            if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
           	  
           	 try { 
					MyLogin.loginpost(new URI(MyLogin.url));
				} catch (URISyntaxException e) {
					logger.info(e);
				}
           	 response2 = MyMainClient.getHttpclient()
    					.execute(selectPost);
           	 
            }
            
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				//EntityUtils.consume(entity2); 
	             
				str = EntityUtils.toString(entity2, "UTF-8");
				
			}
		 
			response2.close();
			
		} catch (ClientProtocolException e) {
			logger.info(e);
		} catch (IOException e) {
			logger.info(e);
		}
		return str;
	}
	 
	public static  int getNum(String responseContent){
		Document doc = MyJsoup.getDocumnetByStr(responseContent);
		String num = "0";
		Elements enpan = doc.getElementsByTag("span");
		if (!enpan.isEmpty()) {
			Iterator<Element> it = enpan.iterator();
			while (it.hasNext()) {
				Element en = it.next(); 
				String str = en.attr("class");
				if ("to".equals(str)) {
					str = en.text(); 
					int start = str.indexOf("共") + 1;
					str = str.substring(start,
							str.length());
					  
					int end = str.indexOf("页"); 
					 
					num = str.substring(0,
							end); 
					 
					//logger.info(end); 
					//logger.info(str.substring(0,
					//		end));  
					//logger.info(str); 
					 
					//logger.info(num); 
				}
			}
		}
		return Integer.valueOf(num);  
	}
	  
	
	
	
	public static void get(String starttime, String endtime) {
		// startTime = "2015-05-03"; 
		save(starttime, endtime);
		if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
			return; 
		}
		try { 
			String tempPath = PathUtill.getXMLpath();

			tempPath += "data" + File.separator + "InventoryOutModel"
					;
			logger.info(tempPath);
 
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator
					+ "InventoryOutModel.csv");
			// file2.createNewFile();

			CsvReader reader = new CsvReader(file2.getPath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了
 
			GoodsReceitManager.saveOutModel(reader, starttime, endtime);

			reader.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} // 跳过表头 如果需要表头的话，不要写这句。

	}

	
	public static void save(String starttime,String endtime) {
		URI uri;     
		try {  
			uri = new URI(downurlOutModel);    
			HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("formName", "reportModelConditon")
					.addParameter("puteEnddate1", starttime)
					.addParameter("puteEnddate2", endtime)
.addParameter("flage", "2")
					// .addParameter("buyerProductcode", "101123403")
					.build();   
  
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(request);
			
			int statusCode = response2.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					MyLogin.loginpost(new URI(MyLogin.url));
				
				response2 = MyMainClient.getHttpclient().execute(request);

			} 
			 
			MyMainClient.mapreturn.put("InventoryModeloutstatue",statusCode+"");  
			
			HttpEntity entity = response2.getEntity();

			//EntityUtils.consume(entity);
			/*
			 * if (entity != null) { String str = EntityUtils.toString(entity,
			 * "UTF-8"); if(StringUtill.isNull(str)){ MyLogin.loginpost(new
			 * URI(MyLogin.url)); response2 = MyMainClient.getHttpclient()
			 * .execute(request); entity = response2.getEntity(); } }
			 */ 
			
			InputStream in = entity.getContent();
 
			String tempPath = PathUtill.getXMLpath(); 
			
			tempPath += "data" + File.separator + "InventoryOutModel"
				;
   
			logger.info(tempPath); 
 
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "InventoryOutModel.csv");
 
			
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
	 
	
	public static boolean getinventoryOutModel(String starttime, String endtime,MyMainClient mc) {
		try {  
			logger.info("getinventoryINReturnModel"); 
			URI uri = new URI(urlOutModel);
			String responseContent = getHtmlStringOut(uri, starttime, endtime,1,mc);
			if(StringUtill.isNull(responseContent)){  
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlStringOut(uri, starttime, endtime,1,mc);
			}     
			int num = getNum(responseContent); 
			logger.info(num); 
			for(int i=1;i<=num;i++){     
				responseContent = getHtmlStringOut(uri, starttime, endtime,i,mc);
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en); 
				Elements tr = en.getElementsByTag("tr");
				//logger.info(tr); 
				GoodsReceitManager.saveOutModel(tr,starttime, endtime);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
}
